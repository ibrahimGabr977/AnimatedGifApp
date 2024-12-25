package com.hope.igb.catgif.screens.gifplayer.offline;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;
import com.hope.igb.catgif.gifcontroller.GifPlayer;

public class GifPlayerFragmentViewMvc extends BaseObservableMvc<GifPlayerFragmentViewMvc.GifPlayerViewMvcListener>
        implements GifPlayer.GifPlayerListener {



    protected interface GifPlayerViewMvcListener {
        void onBackClicked();
        void onBrowseClicked();
    }



    private final GifPlayer gifPlayer;
    private final ImageView gif_view, gif_sign, browse, back;



    protected GifPlayerFragmentViewMvc(LayoutInflater inflater, ViewGroup parent, GifPlayer gifPlayer) {
        setRootView(inflater.inflate(R.layout.gif_player_fragment, parent, false));


        this.gifPlayer = gifPlayer;


        gif_view = findViewById(R.id.player_image_view);
        back = findViewById(R.id.player_back_image_btn);
        browse = findViewById(R.id.player_browse_image_btn);

        gif_sign = findViewById(R.id.player_gif_sign_image);
    }





    public void bindPlayerView(Uri gif){

        //prepare gif uri to play
        prepareGif(gif);

        //play gif when gif_sign clicked
        gif_sign.setOnClickListener(v -> playGif());

        //set on click on back image button
        back.setOnClickListener(v -> onBackClicked());



        //set on browse image button clicked
        browse.setOnClickListener(v -> onBrowseClicked());


    }




    //on click on back image button
    private void onBackClicked(){
        for (GifPlayerViewMvcListener listener : getListeners())
            listener.onBackClicked();
    }



    //on browse image button clicked
    //this browse button for browse a new gif and replace it with the current one
    private void onBrowseClicked(){

        //release gif and player before opening gallery to get new one
        releasePlayer();

        for (GifPlayerViewMvcListener listener :  getListeners())
            listener.onBrowseClicked();
    }



    //prepare gif uri to play
    private void prepareGif(Uri gif){
            gifPlayer.prepareGif(gif);
            gifPlayer.setNormalImage().into(gif_view);
            gif_sign.setVisibility(View.VISIBLE);

    }



    //play the prepared gif and listen for end
    private void playGif(){
        gifPlayer.registerListener(this);
        gifPlayer.playGif().into(gif_view);
        gif_sign.setVisibility(View.GONE);

    }



    //release gif and player and back them to normal
    private void releasePlayer(){
        gifPlayer.unregisterListener(this);
        gifPlayer.setNormalImage().into(gif_view);
        gif_sign.setVisibility(View.VISIBLE);
    }




    @Override
    public void onGifEnded() {
        releasePlayer();
    }


}
