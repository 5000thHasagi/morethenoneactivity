package com.example.morethenoneactivityapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.BitSet;

public class PhotosUtils {

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if(srcHeight > destHeight || srcWidth > destWidth){
            if(srcWidth > srcHeight){
                inSampleSize = Math.round(srcHeight/destHeight);
            } else {
                inSampleSize = Math.round(srcWidth/destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path, options);
    }
}
