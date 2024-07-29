package com.hope.igb.catgif.screens.generate.fromvideo;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.VideoView;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.dialoghelper.InfoDialogHelper;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;
import com.hope.igb.catgif.videocontroller.VideoLoader;

class GenerateFromVideoViewMvc extends BaseObservableMvc<GenerateFromVideoViewMvc.Listener> {

    private final VideoView videoView;
    private final VideoLoader videoLoader;
    private final RadioGroup radioGroup;




    interface Listener{
        void generateGIFFromVideo(); //on generate gif button click
        void onReverseGifClicked();
        void onChangeVideoClicked();


    }


    GenerateFromVideoViewMvc(LayoutInflater inflater, ViewGroup parent, VideoLoader videoLoader){
        this.videoLoader = videoLoader;

        setRootView(inflater.inflate(R.layout.fragment_generate_from_video, parent, false));

        videoView = findViewById(R.id.generate_video_view);
        ImageView generate = findViewById(R.id.generate_video_image_btn);


        generate.setOnClickListener(v -> {
            for (Listener listener: getListeners())
                listener.generateGIFFromVideo();
        });


        radioGroup = findViewById(R.id.generate_video_radio_group);

        //show version details on a dialog on touch the drawable end on each of them
         showVersionInformation();






        ImageView reverse_gif_btn = findViewById(R.id.generate_video_reverse);
        ImageView change_video = findViewById(R.id.generate_video_change_video);





        //video fragment back pressed
        reverse_gif_btn.setOnClickListener(v -> {
            for (Listener listener: getListeners())
                listener.onReverseGifClicked();
        });


        //change the current video from gallery
        change_video.setOnClickListener(v -> {
            for (Listener listener: getListeners())
                listener.onChangeVideoClicked();
        });


    }


    protected void bindVideoView(Uri video_uri){
        videoLoader.loadVideo(videoView, video_uri);

    }

     protected void pauseVideo(){
        videoView.pause();
    }


    protected boolean isPlaying(){
        return videoView.isPlaying();
    }



    //get the desired version by checking the radio group
     protected boolean isBriefly() {
        return radioGroup.getCheckedRadioButtonId() == R.id.generate_video_briefly_version_radio;
    }





    @SuppressLint("ClickableViewAccessibility")
    private void showVersionInformation(){
        RadioButton default_version = findViewById(R.id.generate_video_default_version_radio);
        RadioButton briefly_version = findViewById(R.id.generate_video_briefly_version_radio);





        //show details dialog about default version on drawable clicked
        default_version.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int[] textLocation = new int[2];
                default_version.getLocationOnScreen(textLocation);

                if (event.getRawX() >= textLocation[0] + default_version.getWidth() - default_version.getTotalPaddingRight()) {

                    InfoDialogHelper infoDialogHelper = new InfoDialogHelper(getContext());
                    infoDialogHelper.versionHelpDialog(false).show();

                }


            }
            return false;
        });



        //show details dialog about default version on drawable clicked
        briefly_version.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int[] textLocation = new int[2];
                briefly_version.getLocationOnScreen(textLocation);

                if (event.getRawX() >= textLocation[0] + briefly_version.getWidth() - briefly_version.getTotalPaddingRight()) {

                    InfoDialogHelper infoDialogHelper = new InfoDialogHelper(getContext());
                    infoDialogHelper.versionHelpDialog(true).show();

                }

            }
            return false;
        });



    }


}
