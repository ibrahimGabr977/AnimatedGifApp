package com.hope.igb.catgif.videocontroller;


import android.content.Context;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoLoader {


    private final MediaController controller;

    public VideoLoader(Context context) {
        controller = new MediaController(context);
    }


    public void loadVideo(VideoView videoView, Uri video_uri){

        videoView.setVideoURI(video_uri);
        videoView.start();



        //these below lines of code for ensure that the media controller will displayed
        // on bottom of the video view not on bottom of the parent view

        videoView.setOnPreparedListener(mp ->
                mp.setOnVideoSizeChangedListener((mp1, width, height) -> {

                    //setup media controller with video view
                    controller.setMediaPlayer(videoView);
                    videoView.setMediaController(controller);
                    videoView.requestFocus();

                    //set media controller position on bottom of video view
                    controller.setAnchorView(videoView);
                }));



    }






}
