package com.hope.igb.catgif.screens.comman.base;

import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;

import androidx.fragment.app.Fragment;

import java.io.File;

public class BaseFragment extends Fragment {

    //check if there is a process running right now to handle user interactions (e.g onBackPressed()) during process
    private boolean in_progress;



    //the application folder
    protected File getBase_folder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
           return new File(requireContext().getExternalFilesDir(null).getAbsolutePath(),"Cat Gif");
        else
            return new File(Environment.getExternalStorageDirectory(),"Cat Gif");
    }



    protected void setIn_progress(boolean in_progress) {
        this.in_progress = in_progress;
    }

    public boolean isIn_progress() {
        return in_progress;
    }



    //for refreshing gallery after downloaded or generating a new gif to show it
    protected void scanNewGif(String gif_path){
        MediaScannerConnection.scanFile(getContext(),
                new String[] { gif_path }, new String[] {"image/gif"}, null);
    }


}
