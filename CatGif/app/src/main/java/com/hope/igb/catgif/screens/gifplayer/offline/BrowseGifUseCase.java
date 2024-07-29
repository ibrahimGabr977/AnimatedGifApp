package com.hope.igb.catgif.screens.gifplayer.offline;

import android.app.Activity;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import com.hope.igb.catgif.util.OpenGalleryHelper;

 class BrowseGifUseCase extends OpenGalleryHelper<BrowseGifUseCase.BrowseGifListener> implements
         ActivityResultCallback<ActivityResult> {



     protected interface BrowseGifListener {
        void onGifSelected(Uri gif);
        void onInvalidOrErrorFileSelected();
        void onNoGifSelected();
    }

    protected BrowseGifUseCase(Activity activity) {
        super(activity);
        registerLauncher(this);
    }


    protected void BrowseGifFromGallery(){
        openGalleryForGif();
    }



     @Override
     public void onActivityResult(ActivityResult result) {

         for (BrowseGifListener listener : getListeners())
         if (result.getResultCode() == Activity.RESULT_OK){

             if (result.getData() != null && result.getData().getData() != null) //correctly selected a gif file from gallery
                 listener.onGifSelected(result.getData().getData());


             else //selected invalid file or unknown error happened
                 listener.onInvalidOrErrorFileSelected();


         }else //canceled or didn't select any gif
             listener.onNoGifSelected();








     }




}
