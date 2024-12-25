package com.hope.igb.catgif.util;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.hope.igb.catgif.comman.BaseObservable;


/**
 *##error  that i faced which is the activity launcher must be declared inside the onCreate or onCreateView
 * this class for open activity and listen for result to browse images, image or video
 */

public class OpenGalleryHelper<BROWSE_LISTENER> extends BaseObservable<BROWSE_LISTENER>   {





    private final Activity activity;
    private  ActivityResultLauncher<Intent> resultLauncher;
    private final Intent intent;


    public OpenGalleryHelper(Activity activity) {

        this.activity = activity;
        intent = new Intent(Intent.ACTION_GET_CONTENT);


    }


    protected void registerLauncher(ActivityResultCallback<ActivityResult> callback){
        resultLauncher = ((ActivityResultCaller) activity)
                .registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }


    protected void openGalleryForImages(){

        //allows any image file type. Change * to specific extension to limit it
        intent.setType("image/*");
        //allow multiple images choice
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        resultLauncher.launch(Intent.createChooser(intent, "Select Pictures"));

        //SELECT_PICTURES is simply a global int used to check the calling intent in onActivityResult
    }



    protected void openGalleryForVideo(){
        intent.setType("video/*"); //allows any video file type. Change * to specific extension to limit it
        resultLauncher.launch(Intent.createChooser(intent, "Select Video"));
    }



    protected void openGalleryForGif(){
        intent.setType("image/gif"); //select only gif files
        resultLauncher.launch(Intent.createChooser(intent, "Select Gif"));
    }


}
