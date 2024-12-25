package com.hope.igb.catgif.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import java.io.IOException;

//this class for converting and compressing bitmap from uri
public class BitmapHelper {

    private final Activity activity;

    public BitmapHelper(Activity activity) {
        this.activity = activity;
    }


    //convert the uri image from gallery to bitmap for gif generating purpose
    public Bitmap uriToCompressedBitmap(Uri image){
        Bitmap bitmap;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(activity.getContentResolver(), image));

            else
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), image);

            return compressBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    //to compress the bitmaps to a suitable size
    public Bitmap compressBitmap(Bitmap bitmap){

        //640 max width and 480 max height

        int height = Math.min(bitmap.getHeight(), 480);
        int width = Math.min(bitmap.getWidth(), 640);

        return Bitmap.createScaledBitmap(bitmap, width, height, true );

    }


}
