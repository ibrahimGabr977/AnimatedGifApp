package com.hope.igb.catgif.screens.gifplayer.online;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.comman.Constants;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;
import com.hope.igb.catgif.gifcontroller.GifPlayer;


/**
 * This class is the view mvc of OnlineGifPlayerFragment
 */

public class OnlineGifPlayerFragmentViewMvc extends BaseObservableMvc<OnlineGifPlayerFragmentViewMvc.Listener> implements
        View.OnClickListener {




    protected interface Listener {
        void onBackClicked();
        void onSaveClicked();
        void onShareClicked();
        void onGifInfoClicked();
        void onLikeClicked();
    }


    private final GifPlayer gifPlayer;
    private  ImageView gif_view, back, save,  share, gif_info;
    private  TextView like;
    private  boolean already_liked;
    private int likes_count;



    protected OnlineGifPlayerFragmentViewMvc(LayoutInflater inflater, ViewGroup parent, GifPlayer gifPlayer, boolean already_liked) {

        setRootView(inflater.inflate(R.layout.gif_online_player_fragment, parent, false));

        this.already_liked = already_liked;
        this.gifPlayer = gifPlayer;


        initViewChildren();
        initOnClickListeners();

    }



    private void initViewChildren(){
            gif_view = findViewById(R.id.online_player_image_view);

            back = findViewById(R.id.online_player_back_image_btn);
            save = findViewById(R.id.online_player_save_image_btn);
            share = findViewById(R.id.online_player_share_image_btn);
            gif_info = findViewById(R.id.online_player_info_image_btn);


            like = findViewById(R.id.online_player_like_text_btn);

    }


    private void initOnClickListeners(){
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        share.setOnClickListener(this);
        gif_info.setOnClickListener(this);
        like.setOnClickListener(this);
    }




    public void bindPlayerView(String gif_url, int likes_count){

        //play the gif from url
        playGif(gif_url);

        //get number of likes
        this.likes_count = likes_count;

        //bind like views components
        bindLikesView();


    }





    //play the prepared gif and listen for end
    private void playGif(String gif_url){
        gifPlayer.playOnlineGif(gif_url).into(gif_view);
    }




    //========================================likes view settings==========================================
    private void bindLikesView() {
        if (already_liked)
            setOnLikedView();
        else
            setOnUnLikedView();


        like.setText(likesTextFormat());
    }



    private String likesTextFormat(){
        return likes_count >= 1000 ? ""+Constants.roundTwoDecimal(likes_count / 1000.0) : ""+likes_count; }


    //liked text view style
    private void setOnLikedView(){
        like.setTextColor(getContext().getResources().getColor(R.color.blue_sky_color));
        like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gif_player_liked,
                0 , 0 ,0  );
    }



    //unLiked text view style
    private void setOnUnLikedView(){
        like.setTextColor(getContext().getResources().getColor(R.color.white_color));
        like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gif_player_unliked,
                0 , 0 ,0  );
    }


    private void onLikeClicked(){
        if (already_liked){
            already_liked = false;
            likes_count--;
        }else {
            already_liked = true;
            likes_count++;
        }
        bindLikesView();
    }


    //====================================================================================================================







    @Override
    public void onClick(View v) {
        for (Listener listener : getListeners())

        if (v.getId() == R.id.online_player_back_image_btn)
            listener.onBackClicked();

        else if (v.getId() == R.id.online_player_save_image_btn)
            listener.onSaveClicked();

        else if (v.getId() == R.id.online_player_share_image_btn)
            listener.onShareClicked();

        else if (v.getId() == R.id.online_player_like_text_btn) {
            onLikeClicked();
            listener.onLikeClicked();
        }

        else if (v.getId() == R.id.online_player_info_image_btn)
            listener.onGifInfoClicked();

    }















}
