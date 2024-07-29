package com.hope.igb.catgif.screens.giflibrary;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import androidx.core.content.FileProvider;
import com.hope.igb.catgif.BuildConfig;
import com.hope.igb.catgif.gifcontroller.GifPlayer;
import com.hope.igb.catgif.screens.comman.base.BaseActivity;
import com.hope.igb.catgif.screens.comman.screennavigator.ScreenNavigator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


public class LibraryActivity extends BaseActivity implements GifLibraryViewMvc.LibraryListener {

    private GifLibraryViewMvc viewMvc;
    private ArrayList<Uri> all_gif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen(this);
        GifPlayer gifPlayer = new GifPlayer(this);



        //extract all gif files to all_gif array list
        extractAllFiles();


        viewMvc = new GifLibraryViewMvc(LayoutInflater.from(this), null, gifPlayer, all_gif);

        //for coloring the toolbar to suit the current activity layout
        setCurrent_activity("LibraryActivity");

        setContentView(viewMvc.getRootView());








    }




    private void extractAllFiles(){

        if (!getBase_folder().exists()){
            getBase_folder().mkdir();



        }else {
            // filter to identify images based on their extensions
            FilenameFilter IMAGE_FILTER = (dir, name) -> name.endsWith(".gif");

            File[] all_gif_files = getBase_folder().listFiles(IMAGE_FILTER);

            //all the gif uris
            if (all_gif_files != null) {

                 all_gif = new ArrayList<>(all_gif_files.length);

                for (File file: all_gif_files){
                    all_gif.add(fileToUri(file));
                }
            }

        }


    }


    //get uri from file
    private Uri fileToUri(File file){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
           return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        else
            return Uri.fromFile(file);

    }




    //the base application folder that contains all gif files that created by the application
    private File getBase_folder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new File(getExternalFilesDir(null).getAbsolutePath(),"Cat Gif");
        else
            return new File(Environment.getExternalStorageDirectory(),"Cat Gif");
    }



    @Override
    protected void onStart() {
        super.onStart();

        if (all_gif!= null)
        viewMvc.bindView(getScreen_width());


        viewMvc.registerListener(this);
    }



    @Override
    protected void onStop() {
        super.onStop();
        viewMvc.unregisterListener(this);
        //reset gif player and stop gif playing
        viewMvc.onGifEnded();
    }

    @Override
    public void onAddGifClicked() {
        //go to select activity to start the whole process again
        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        //go to the first activity (select activity) not to the previous one (the second activity)
        ScreenNavigator screenNavigator = new ScreenNavigator(this);
        screenNavigator.toSelectActivity();
        finish();
    }
}