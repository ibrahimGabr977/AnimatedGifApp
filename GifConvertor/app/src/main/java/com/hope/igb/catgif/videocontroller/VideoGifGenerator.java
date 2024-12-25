package com.hope.igb.catgif.videocontroller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;
import com.hope.igb.catgif.gifcontroller.AnimatedGIFWriter;
import com.hope.igb.catgif.util.MyAsyncTask;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * this class for generating gif from videos
 * GifVideoGenerationListener listen for frames extraction progress, saving progress and saving finished
 */

public class VideoGifGenerator extends MyAsyncTask<VideoGifGenerator.GifVideoGenerationListener>
        implements MyMediaRetriever.FramesExtractionProgressListener {

    //the target generated gif full path without extension like /sdcard/test
    private final String file_full_path;
    //media retriever for getting videos lengths and frames
    private final MyMediaRetriever myRetriever;




    private boolean canceled;
    private final Activity activity;

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }


    public interface GifVideoGenerationListener {

        void onStartGenerating();
        void onFramesExtractionProgressChanged(int progress);
        void onFramesExtractionProgressFinished();
        void onGifCreatingProgressChanged(int progress);
        void onGifSaved(String file_name);

    }


    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void onFramesExtractionProgressChanged(int progress) {
        for (GifVideoGenerationListener listener: getListeners())
        listener.onFramesExtractionProgressChanged(progress);
    }

    @Override
    public void onFramesExtractionProgressFinished() {
        for (GifVideoGenerationListener listener: getListeners())
            listener.onFramesExtractionProgressFinished();
    }





    //briefly_version specify the target gif version
    //video the targeted video
    public VideoGifGenerator( Activity activity, Uri video, String file_full_path,
                             boolean briefly_version){
        super(activity);
        this.activity = activity;

        this.file_full_path = file_full_path;
        myRetriever = new MyMediaRetriever(activity, video, briefly_version);
        myRetriever.registerListener(this);

    }





    @Override
    public void doInBackground() {//video here represent the selected video (only one allowed not array)

        //listen for start the whole process
        for (GifVideoGenerationListener listener: getListeners())
            listener.onStartGenerating();



        //getting delay from the required frame rate
        int delay = (int) (1000/ (float) myRetriever.getDesiredGifFrameRate());

        AnimatedGIFWriter writer = new AnimatedGIFWriter(true);



        //myRetriever.videoToImages() will return null if canceled
            //convert the video to bitmap images by its frames
        ArrayList<Bitmap> images = myRetriever.videoToImages();




        if (images != null){

            try {

                OutputStream os = new FileOutputStream(file_full_path+".gif");

                // Use -1 for both logical screen width and height to use the first frame dimension
                writer.prepareForWrite(os, -1, -1);

                //progress for creation
                int progress;


                for (int i = 0; i < images.size(); i++) {

                    if (!canceled) {


                        //convert uri to bitmap then add to gif
                        writer.writeFrame(os, images.get(i), delay);


                        progress = (int) ((i * 1.0 / images.size() * 1.0) * 100.0);



                        //listen for progress
                        for (GifVideoGenerationListener listener: getListeners())
                            listener.onGifCreatingProgressChanged(Math.min(progress, 100));



                    } else {//if the user cancel the process before finished
                        break;
                    }




                }

                writer.finishWrite(os);


            } catch (Exception e) {
                Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


        myRetriever.unregisterListener(this);

    }




    @Override
    public void onPostExecute() {
        for (GifVideoGenerationListener listener: getListeners())
            listener.onGifSaved(file_full_path +".gif");
    }























}
