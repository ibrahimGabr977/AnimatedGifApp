package com.hope.igb.catgif.imagescontroller;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import com.hope.igb.catgif.comman.Constants;
import com.hope.igb.catgif.gifcontroller.AnimatedGIFWriter;
import com.hope.igb.catgif.util.BitmapHelper;
import com.hope.igb.catgif.util.MyAsyncTask;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;


//this class for generating gif from images
//MyAsyncTask alternative for AsyncTask because it's deprecated
public class ImagesGifGenerator extends MyAsyncTask<ImagesGifGenerator.GifImagesGenerationListener> {

    //the target generated gif full path without extension like /sdcard/test
    private final String file_name;
    private final ArrayList<Uri> images;
    private final Activity activity;

    private boolean canceled;

    //listen for saving progress and saving finished
    public interface GifImagesGenerationListener{
        void onStartGenerating();
        void onGifCreatingProgressChanged(int progress);
        void onGifSaved(String file_name);
    }


    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public ImagesGifGenerator(String file_name, Activity activity, ArrayList<Uri> images){
        super(activity);
        this.file_name = file_name;
        this.activity = activity;
        this.images = images;
    }









    @Override
    public void doInBackground() {

        //listen for start generating
        for (GifImagesGenerationListener listener : getListeners())
            listener.onStartGenerating();


        //class for converting and compressing bitmap from uri
        BitmapHelper helper = new BitmapHelper(activity);

        //getting delay from the required frame rate
        int delay = (int) (1000/Constants.MIN_FRAME_RATE);

        AnimatedGIFWriter writer = new AnimatedGIFWriter(true);


        try {

            OutputStream os = new FileOutputStream(file_name+".gif");

            // Use -1 for both logical screen width and height to use the first frame dimension
            writer.prepareForWrite(os, -1, -1);

            //progress for creation
            int progress;


            for (int i = 0; i < images.size(); i++) {

                if (!canceled) {


                    //convert uri to bitmap then add to gif
                    writer.writeFrame(os, helper.uriToCompressedBitmap(images.get(i)), delay);


                    progress = (int) ((i * 1.0 / images.size() * 1.0) * 100.0);



                    //listen for progress
                    for (GifImagesGenerationListener listener : getListeners())
                        listener.onGifCreatingProgressChanged(Math.min(progress, 100));



                } else //if the user cancel the process before finished
                    break;



                 }

            writer.finishWrite(os);


        } catch (Exception e) {
            Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    public void onPostExecute() {
        for (GifImagesGenerationListener listener : getListeners())
        listener.onGifSaved(file_name+".gif");
    }









}
