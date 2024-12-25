package com.hope.igb.catgif.videocontroller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import com.hope.igb.catgif.comman.BaseObservable;
import com.hope.igb.catgif.comman.Constants;
import com.hope.igb.catgif.util.BitmapHelper;
import com.hope.igb.catgif.util.FilePathHelper;
import java.io.FileDescriptor;
import java.util.ArrayList;
import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * this class for getting length of videos and videos' frames
 */
public class MyMediaRetriever extends BaseObservable<MyMediaRetriever.FramesExtractionProgressListener> {

    //frames from video library
    private final FFmpegMediaMetadataRetriever retriever;
    private final Activity activity;
    //briefly_version specify the target gif version
    private final   boolean briefly_version;
    private double gif_frame_rate;


    interface FramesExtractionProgressListener{
        void onFramesExtractionProgressChanged(int progress);
        void onFramesExtractionProgressFinished();
        boolean isCanceled();
    }



    protected MyMediaRetriever(Activity activity, Uri video, boolean briefly_version) {
        this.activity = activity;
        this.briefly_version = briefly_version;
        this.retriever  = new FFmpegMediaMetadataRetriever();

        setValidDataSource(video);

    }






    //this method for getting a valid data source from the provided video uri
    private void setValidDataSource(Uri video){


        //some videos' uri causing crash so use video path in that case
        try {
            FileDescriptor fileDescriptor = activity.getContentResolver().openFileDescriptor(video, "rw").getFileDescriptor();
            retriever.setDataSource(fileDescriptor);
        } catch (Exception e) {
            retriever.setDataSource(FilePathHelper.getPath(activity, video));
        }

    }



     protected long getVideoDuration(){
        return Long.parseLong(retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
    }









    protected ArrayList<Bitmap> videoToImages(){

        //convert video milliseconds to seconds
        int duration_seconds = (int) (getVideoDuration() / Constants.MILLIS_RATIO);


        //if the normal version
        if (!briefly_version){

            //the selected video duration is short
            if (getVideoDuration() <= Constants.SHORT_VIDEO_DURATION){

                return getFrames( duration_seconds * Constants.SHORTS_FRAME_RATE ,
                        duration_seconds, Constants.ADDITIONAL_FRAME_RATIO);


            }

            //the selected video duration is medium or long
            return getFrames(Constants.MINUTE_FRAMES, duration_seconds, Constants.ADDITIONAL_FRAME_RATIO);


            //the briefly version
        }else {

            if (getVideoDuration() <= Constants.SHORT_VIDEO_DURATION){

                return getFrames( duration_seconds ,
                        duration_seconds, Constants.ADDITIONAL_FRAME_RATIO_BRIEFLY);


            }
            return getFrames(Constants.MINUTE_FRAMES_BRIEFLY, duration_seconds, Constants.ADDITIONAL_FRAME_RATIO_BRIEFLY);
        }


    }




    /**
     * there are 2 possible used of the below method getFrames() normal version and briefly version
     * the briefly version is like a summary of a video with tiny size
     *
     * the frame ratio is the number of taken frames per second to generate the gif
     *
     * the total frame count from on videos' lengths [1~30 seconds] = [10~300 frames], [5~30 frames in briefly version]
     * with frame ratio = 10, 1 in briefly version
     *
     * on videos' lengths [31~60 seconds] also equal to 300 frames, 30 frames in briefly version but both different frame ratio
     * with frame ratio = [10~5], [1~0.5 in briefly version]
     *
     * videos with length greater than 60 sec = [300~960 frames], [30~96 in briefly version]
     * with frame ratio = [5~1], [0.5~0.1 in briefly version]
     *
     * max allowed videos length equal 16 minute.
     *
     * additional_frame_ratio represent that each a certain duration the frames count increased by 1
     */


    private ArrayList<Bitmap> getFrames(int frames_count , int video_duration, double additional_frame_ratio){

        ArrayList<Bitmap> images = new ArrayList<>();
        Bitmap frame;
        BitmapHelper helper = new BitmapHelper(activity); //class for compressing bitmap


        //the number of additional frames above 300 (30 in briefly version) if video's length greater than 60 seconds,
        //it's equal to 0 if the video length equal or less than 60 seconds
        int extra_frames_count = video_duration > Constants.MINUTE_RATIO ?
                (int) ((video_duration - Constants.MINUTE_RATIO) / additional_frame_ratio) : 0;


        //the total allowed frames on the whole video length
        int total_frames_count = frames_count + extra_frames_count;

        //the number of taken frames per specific duration
        double frame_ratio =  (total_frames_count * 1.0) / (video_duration * 1.0);

        //round the ratio to 2 decimal point value
        frame_ratio = Constants.roundTwoDecimal(frame_ratio);


        //ensure that on the briefly version frame ratio >= 0.1 and on normal version >= 1
        frame_ratio = additional_frame_ratio == Constants.ADDITIONAL_FRAME_RATIO_BRIEFLY?
                Math.max(Constants.MIN_FRAME_RATIO_BRIEFLY, frame_ratio) : Math.max(Constants.MIN_FRAME_RATIO, frame_ratio);



        //step_duration mean take/get frame each xxx-xxx microsecond
        int step_duration = (int) (Constants.MICROS_RATIO / frame_ratio);




        int progress;

        for (int i =1; i <= total_frames_count ; i++){

            for (FramesExtractionProgressListener listener: getListeners())

                //listen for canceled the process
                if (!listener.isCanceled()){

                    frame = retriever.getFrameAtTime(i * step_duration,
                            FFmpegMediaMetadataRetriever.OPTION_CLOSEST);



                    //max progress 100 so we need to know how many frames that 1 progress represented

                    progress = (int) ((i * 1.0 / total_frames_count * 1.0) * 100.0);

                    listener.onFramesExtractionProgressChanged(Math.min(progress, 100));


                    //FFmpegMediaMetadataRetriever sometime doesn't get a frame in correct way so that it need null condition
                    if (frame != null)
                        images.add(helper.compressBitmap(frame));


                }else //return null if canceled
                    return null;


        }


        retriever.release();

        for (FramesExtractionProgressListener listener: getListeners())
                listener.onFramesExtractionProgressFinished();

        //the gif_frame_rate may changed on the gif generation
          this.gif_frame_rate = frame_ratio;

        return images;
    }






    //if the frame ratio greater or equal to 2.0 return it, else return 1.5
    // that's to make the gif images series able to recognized by human eye in case of different seconds' frames
    public double getDesiredGifFrameRate() {
        return Math.max(gif_frame_rate, Constants.MIN_FRAME_RATE);
    }
}
