package com.hope.igb.catgif.videocontroller;

import android.app.Activity;
import android.net.Uri;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import com.hope.igb.catgif.comman.Constants;
import com.hope.igb.catgif.util.OpenGalleryHelper;



/**
 * this class for browsing video from gallery
 */
public class BrowseVideoUseCase extends OpenGalleryHelper<BrowseVideoUseCase.Listener> implements
        ActivityResultCallback<ActivityResult> {




    public interface Listener{

        void onNoVideoSelected();
        void onVideoSelected(Uri imageUri);
        void onInvalidVideoSelected();
    }





    private final Activity activity;




    public BrowseVideoUseCase(Activity activity) {
        super(activity);
        registerLauncher(this);

        this.activity = activity;



    }





    public void browseVideoFromGallery(){
        openGalleryForVideo();
    }





    @Override
    public void onActivityResult(ActivityResult result) {


        for (Listener listener : getListeners())

        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {


            Uri video_uri = result.getData().getData();




            //this class for extracting videos information
            //briefly_version ignorable here because all we need here is video duration

            MyMediaRetriever retriever = new MyMediaRetriever(activity, video_uri, false);

            if (retriever.getVideoDuration() > Constants.MAX_ALLOWED_VIDEO_DURATION)
                listener.onInvalidVideoSelected();

            else
                listener.onVideoSelected(video_uri);

        } else
                listener.onNoVideoSelected();

    }










}
