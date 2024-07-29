package com.hope.igb.catgif.screens.selectmedia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;


public class SelectViewMvc extends BaseObservableMvc<SelectViewMvc.Listener>
        implements
        View.OnClickListener {


    interface Listener{
        void onGifFromImagesClickListener();
        void onGifFromVideosClickListener();
    }





    SelectViewMvc(LayoutInflater inflater, ViewGroup parent){
        setRootView(inflater.inflate(R.layout.activity_select, parent, false));


        TextView from_images = findViewById(R.id.select_media_browse_images);
        TextView from_videos = findViewById(R.id.select_media_browse_videos);



        from_images.setOnClickListener(this);
        from_videos.setOnClickListener(this);


        





    }




    @Override
    public void onClick(View v) {

       if (v.getId() == R.id.select_media_browse_images){

           for (Listener listener: getListeners())
               listener.onGifFromImagesClickListener();

       }else  if (v.getId() == R.id.select_media_browse_videos){


           for (Listener listener: getListeners())
               listener.onGifFromVideosClickListener();

       }

    }







}
