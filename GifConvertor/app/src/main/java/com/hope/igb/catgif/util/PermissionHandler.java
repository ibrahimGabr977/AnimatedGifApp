package com.hope.igb.catgif.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.hope.igb.catgif.comman.BaseObservable;

public class PermissionHandler extends BaseObservable<PermissionHandler.PermissionGrantedListener> implements
        ActivityResultCallback<Boolean> {




    public interface PermissionGrantedListener {
        void onPermissionGranted();
        void onPermissionNotGranted();
    }


    private final Activity activity;




    public PermissionHandler(Activity activity) {
        this.activity = activity;
    }





    private ActivityResultLauncher<String> launcher(){
      return  ((ActivityResultCaller)activity)
                .registerForActivityResult(new ActivityResultContracts.RequestPermission(), this);
    }



    public boolean isStoragePermissionGranted(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            //check read external storage permission
            launcher().launch(Manifest.permission.READ_EXTERNAL_STORAGE);

        }else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            //check write external storage permission
            launcher().launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);


        }else
            return true;

        return false;
    }






    @Override
    public void onActivityResult(Boolean isGranted) {
        for (PermissionGrantedListener listener : getListeners())
            if (isGranted)
                listener.onPermissionGranted();
            else
                listener.onPermissionNotGranted();

    }




}
