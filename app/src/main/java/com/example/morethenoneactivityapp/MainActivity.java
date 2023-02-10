package com.example.morethenoneactivityapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.morethenoneactivityapp.utils.PhotosUtils;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private TextView textView;

    private SharedPreferences sharedPreferences;
    private int code = 0;
    private Toast toast;
    public static String MY_PREF = "MORETHENONEACTIVITYAPP_PREFERENCES_FILE";
    public final static String FULLUSERNAME = "fullusername";

    private File photoFile;

    private Button btnSend;
    private Button btnGetContact;

    private ImageButton imBtn;
    private ImageView ivPhoto;
    private Intent intentGetContact;
    private Intent intentGetPhoto;
    private final static int REQUEST_CONTACT = 1;
    private final static int REQUEST_PHOTO = 2;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tvViewFullName);

        btnSend = findViewById(R.id.btnMessage);


        String fullUserName;
        if ((fullUserName = loadUserFullNameFromMyPref()) == null){
            getUserFullNameFromLoginActivity();
        } else {
            textView.setText("Hi "+fullUserName);

            btnSend.setOnClickListener(this);

            intentGetContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            PackageManager packageManager = this.getPackageManager();
            if ((packageManager.resolveActivity(intentGetContact, packageManager.MATCH_DEFAULT_ONLY)) == null){
                btnGetContact.setEnabled(false);
            } else {
                btnGetContact = findViewById(R.id.btnGetContact);
                btnGetContact.setOnClickListener(this);
            }
            btnGetContact.setOnClickListener(this);

            imBtn = findViewById(R.id.imBtnPhoto);
            ivPhoto = findViewById(R.id.ivImageCamera);

            intentGetPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String photoFileName = getPhotoFileName();
            photoFile = getPhotoFile(photoFileName);
            boolean canTakePhoto = false;
            canTakePhoto = photoFile != null && intentGetPhoto.resolveActivity(packageManager) != null;

            imBtn.setEnabled(canTakePhoto);
            if(canTakePhoto){
                Uri uri = FileProvider.getUriForFile(this, "com.example.morethenoneactivityapp.fileprovider", photoFile);
                intentGetPhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            imBtn.setOnClickListener(this);
        }
/*
        Intent intent = getIntent();
        //String login = intent.getStringExtra("login");
        String full_name = intent.getStringExtra("fullname");

        textView.setText("Hello "+full_name);

 */
    }
    private String getPhotoFileName(){
        Date date = new Date();
        return "IMG+"+20231002+"jpg";
    }
    private File getPhotoFile(String fileName){
        File filesDir = this.getFilesDir();
        if(filesDir == null){
            return null;
        }
        //TODO: insert check that file exists, in this case don`t create a new file
        return new File(filesDir, fileName);
    }
    @Override
    public void onClick (View view){

        if(view.getId() == btnSend.getId()){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Wait a minute...");
            intent.putExtra(Intent.EXTRA_SUBJECT, "early report");

            Intent intentChooser = Intent.createChooser(intent, "Send report");
            startActivity(intentChooser);
        }
        if(view.getId() == btnGetContact.getId()){
            startActivityForResult(intentGetContact, REQUEST_CONTACT);
        }
        if(view.getId() == imBtn.getId()){
            startActivityForResult(intentGetPhoto, REQUEST_PHOTO);
        }

    }

    private String loadUserFullNameFromMyPref(){

        sharedPreferences = getApplicationContext().getSharedPreferences(MY_PREF, MODE_PRIVATE);
        String fullUserName = sharedPreferences.getString(FULLUSERNAME, "");
        if (fullUserName.isEmpty()){
            return null;
        } else {
            return fullUserName;
        }
    }
    // -------------------------------------------------------------------
     private void getUserFullNameFromLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, code);
     }

     private void updateSelfPhotoImageView(File filePhoto){
        if (filePhoto == null || !filePhoto.exists()){
            ivPhoto.setImageDrawable(null);
            Log.d(TAG, "updateSelfPhotoImageView: ERROR WITH FILE BAKA");
        } else {
            Point size = new Point();
            this.getWindowManager().getDefaultDisplay().getSize(size);
            Bitmap bitmap = PhotosUtils.getScaledBitmap(photoFile.getPath(), size.x, size.y);
            ivPhoto.setImageBitmap(bitmap);
        }
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == code){
            if(resultCode == RESULT_OK ){
                String fullUserName = data.getStringExtra(FULLUSERNAME);
                saveFullUserNameInMyPref(fullUserName);
                textView.setText("Hi "+fullUserName);

            } else {
                int duration = Toast.LENGTH_SHORT;
                if (toast != null){
                    toast.cancel();

                }
                toast = Toast.makeText(this,"This User not Found!", duration);
                toast.show();
                getUserFullNameFromLoginActivity();
            }
        }

        if (requestCode == REQUEST_CONTACT){
            if(resultCode == RESULT_OK){
                if(data != null){
                    Uri contactUri = data.getData();
                    String[] queryFields = new String[]{
                            ContactsContract.Contacts.DISPLAY_NAME
                    };
                    Cursor cursor = this.getContentResolver().query(contactUri, queryFields, null, null, null);
                    try {
                        if(cursor.getCount() == 0){
                            return;
                        }
                        cursor.moveToFirst();
                        String nameContact = cursor.getString(0);
                        Log.d(TAG, "onActivityResult: "+nameContact);

                    } finally {
                        cursor.close();
                    }
                }
            }
        }

        if (requestCode == REQUEST_PHOTO){
            if(resultCode == RESULT_OK){
                updateSelfPhotoImageView(photoFile);
            }
        }
    }

    private void saveFullUserNameInMyPref(String data){
        sharedPreferences = getApplication().getSharedPreferences(MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FULLUSERNAME, data);
        editor.commit();

    }
}