package com.hope.igb.catgif.imagescontroller;

import android.app.Activity;
import android.net.Uri;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import com.hope.igb.catgif.comman.Constants;
import com.hope.igb.catgif.util.OpenGalleryHelper;
import java.util.ArrayList;





//this class for get images from gallery
public class BrowseImagesUseCase extends OpenGalleryHelper<BrowseImagesUseCase.BrowseImagesListener> implements
        ActivityResultCallback<ActivityResult> {


    public interface BrowseImagesListener {
        void onImagesSelected(ArrayList<Uri> selected_images);
        void onNoImagesSelected(String problem);
        void onInvalidNumberOfImagesSelected();
    }





    public BrowseImagesUseCase(Activity activity) {
        super(activity);
        registerLauncher(this);


    }





    //open gallery and to get images
    public   void browseImagesFromGallery() {
       openGalleryForImages();
    }








    @Override
    public void onActivityResult(ActivityResult result) {


        for (BrowseImagesListener listener : getListeners())


        //ensure that the user press ok and data selected not equal null
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {



                //ensure that the user selected multiple images not 1
                if (result.getData().getClipData() != null) {//wil not equal null if only the user selected multiple images


                       //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        int count = result.getData().getClipData().getItemCount();


                        if (count > Constants.MAX_ALLOWED_NUMBER_OF_Images){
                            //notify on the images selected exceed the allowed number
                            listener.onInvalidNumberOfImagesSelected();


                        }else {
                            ArrayList<Uri> images = new ArrayList<>(count);

                            for (int i = 0; i < count; i++) {
                                Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                images.add(imageUri);

                            }

                            //notify that images selected correctly
                            listener.onImagesSelected(images);
                        }




                    } else
                        //notify if user select only one image
                        listener.onNoImagesSelected("Not enough");



            } else
                //notify if user didn't select any images or something wrong
                listener.onNoImagesSelected("Nothing");


    }






}
