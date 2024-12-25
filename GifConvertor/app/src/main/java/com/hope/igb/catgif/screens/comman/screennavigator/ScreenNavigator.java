package com.hope.igb.catgif.screens.comman.screennavigator;

import android.app.Activity;
import android.content.Intent;
import com.hope.igb.catgif.screens.generate.GenerateActivity;
import com.hope.igb.catgif.screens.giflibrary.LibraryActivity;
import com.hope.igb.catgif.screens.gifplayer.GifPlayerActivity;
import com.hope.igb.catgif.screens.gifstore.GifStoreActivity;
import com.hope.igb.catgif.screens.selectmedia.SelectActivity;

public class ScreenNavigator {

    private final Activity activity;
    private final Intent intent;


    public ScreenNavigator(Activity activity) {
        this.activity = activity;
        intent = new Intent();

    }




    //generated_file represents a file full path like /sdcard/Pictures/test.gif
    public void toLibraryActivity(String generated_file){
        intent.setClass(activity, LibraryActivity.class);
        intent.putExtra("GENERATED_FILE", generated_file);
        activity.startActivity(intent);
    }


    //gif_source represents the gif required source either from images or from video
    public void toGenerateActivity(String gif_source){
        intent.setClass(activity, GenerateActivity.class);
        intent.putExtra("GIF_SOURCE", gif_source);
        activity.startActivity(intent);
    }

    public void toSelectActivity() {
        intent.setClass(activity, SelectActivity.class);
        activity.startActivity(intent);
    }

    public void toStoreActivity() {
        intent.setClass(activity, GifStoreActivity.class);
        activity.startActivity(intent);
    }

    public void toPlayerActivity(String gif_id) {
        intent.setClass(activity, GifPlayerActivity.class);
        intent.putExtra("GIF_ID", gif_id);
        activity.startActivity(intent);
    }
}
