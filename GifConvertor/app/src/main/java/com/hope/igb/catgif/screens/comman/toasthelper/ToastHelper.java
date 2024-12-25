package com.hope.igb.catgif.screens.comman.toasthelper;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

    private final Context context;

    public ToastHelper(Context context) {
        this.context = context;
    }


    public void invalidVideo(){
        Toast.makeText(context, "Please select a video with 16 minute duration or less.", Toast.LENGTH_LONG).show();
    }

    public void invalidImagesNumber(){
        Toast.makeText(context, "The maximum allowed number of images are 60.", Toast.LENGTH_LONG).show();
    }


    public void noMediaSelected(String media_type){
        Toast.makeText(context, "You didn't select any "+media_type+" or you chose an invalid media",
                Toast.LENGTH_LONG).show();
    }

    public void invalidOrErrorFileSelected(String media_type){
        Toast.makeText(context, "You chose an invalid "+media_type+" file or something went wrong, please reselect",
                Toast.LENGTH_SHORT).show();
    }




    public void notEnoughImagesSelected(){
        Toast.makeText(context, "Please select at least 2 images to start generating",
                Toast.LENGTH_LONG).show();
    }



    public void notEnoughImagesToGenerate(){
        Toast.makeText(context, "There aren't enough images to generate gif please add more.",
                Toast.LENGTH_SHORT).show();
    }




    public void successfullyGeneration(){
        Toast.makeText(context, "The gif was generated successfully", Toast.LENGTH_SHORT).show();
    }

    public void successfullyDeleted(){
        Toast.makeText(context, "The generation process successfully canceled and the generated file successfully deleted",
                Toast.LENGTH_SHORT).show();
    }


    public void appFolderNotFound(){
        Toast.makeText(context, "Cannot locate the application folder please try again or check app's permissions"
                , Toast.LENGTH_SHORT).show();
    }


    public void comingSoon(String feature) {
        Toast.makeText(context, feature+" is coming soon"
                , Toast.LENGTH_SHORT).show();
    }

    public void showCertainMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void noInternetConnection() {
        Toast.makeText(context, "Network error, please check your internet connection and try again",
                Toast.LENGTH_SHORT).show();
    }


    public void successfullyDownloadedTo(String destination) {
        Toast.makeText(context, "Gif successfully downloaded to: "+destination,
                Toast.LENGTH_SHORT).show();
    }

    public void requiredStoragePermission(String process) {
        Toast.makeText(context, "Required storage permission for "+process,
                Toast.LENGTH_SHORT).show();
    }
}
