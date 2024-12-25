package com.hope.igb.catgif.comman;

public final class Constants {

    /**
     * time ratio variables for conversion to and from second
     * durations in milliseconds
     */



    //max number of selected images that allowed to create the gif from images
    public final static int MAX_ALLOWED_NUMBER_OF_Images = 60;

    //second to millisecond conversion ratio
    public final static int MILLIS_RATIO = 1000;
    //second to microsecond conversion ratio
    public final static int MICROS_RATIO = 1000000;
    //minute to second conversion ratio
    public static final int MINUTE_RATIO = 60;



    //any video has duration shorter than or equal 30 second considered short video
    public final static int SHORT_VIDEO_DURATION = 30000; //30s

    //the number of frame of the short videos per second
    public final static int SHORTS_FRAME_RATE = 10;


    //max duration of selected video that allowed to create gif from video
    public final static int MAX_ALLOWED_VIDEO_DURATION = 960000; //16 min


    /**
     * Normal Version
     */

    //the number of frames of the only first 1 minute not per minute
    public static final int MINUTE_FRAMES = 300;

    //this constant say that each a certain duration increase the total number of frames above the MINUTE_FRAMES by 1
    public static final double ADDITIONAL_FRAME_RATIO = 1.5;

    //the minimum number of frames taken per second (able to reached on the long videos)
    public final static double MIN_FRAME_RATIO = 1.0;




    /**
     * Briefly Version
     */

    //the number of frames of the only first 1 minute not per minute
    public static final int MINUTE_FRAMES_BRIEFLY = 30;

    //this constant say that each a certain duration increase the total number of frames above the MINUTE_FRAMES_BRIEFLY by 1
    public static final double ADDITIONAL_FRAME_RATIO_BRIEFLY = 15.0;


    //the minimum number of frames taken per second (able to reached on the long videos)
    public final static double MIN_FRAME_RATIO_BRIEFLY= 0.1;


    //Generate gif from video
    public static final String FROM_VIDEO = "FROM_VIDEO";

    //Generate gif from images
    public static final String FROM_IMAGES = "FROM_IMAGES";

    //the selected media type (images or video)
    public static final String GIF_SOURCE = "GIF_SOURCE";

    //the minimum possible frame rate
    public static final double MIN_FRAME_RATE = 2.35;


    //round double to two decimal number like 1.23456 to 1.23
    public static  double roundTwoDecimal(double x){ return  ((int)(Math.round(x * 100.0))) / 100.0; }






}
