package com.hope.igb.catgif.gifcontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hope.igb.catgif.comman.BaseObservable;


public final class GifPlayer extends BaseObservable<GifPlayer.GifPlayerListener> implements RequestListener<Drawable> {


    private Uri gif;


    public interface GifPlayerListener {
        void onGifEnded();
    }



    private final Context context;


    public GifPlayer(Context context) {
        this.context = context;
    }


    public void prepareGif(Uri gif) { this.gif = gif; }



    //get the played gif to view on an image view
    public RequestBuilder<Drawable> playGif(){
        return getGlide()
                .load(gif)
                .listener(this);
    }




    //set normal image inside image when the gif not playing
    public RequestBuilder<Bitmap> setNormalImage(){
//        imageView.setImageResource(0);
//        imageView.setImageBitmap(null);

        return getGlide()
                .asBitmap()
                .load(gif);
    }


    private RequestManager getGlide(){
        return Glide.with(context);

    }




    public RequestBuilder<GifDrawable> playOnlineGif(String url){
        return getGlide()
                .asGif()
                .load(url);
    }







    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

        if (resource instanceof GifDrawable){
             ((GifDrawable) resource).setLoopCount(1);

            ((GifDrawable) resource).registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);

                    for (GifPlayerListener listener:getListeners())
                        listener.onGifEnded();

                }
            });


        }


        return false;
    }


}
