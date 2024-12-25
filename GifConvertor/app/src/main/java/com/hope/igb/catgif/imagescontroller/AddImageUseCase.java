package com.hope.igb.catgif.imagescontroller;

import android.app.Activity;
import android.net.Uri;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import com.hope.igb.catgif.comman.Constants;
import com.hope.igb.catgif.util.OpenGalleryHelper;


/**
 * this class for add image or images from gallery to the current images list
 */
public class AddImageUseCase extends OpenGalleryHelper<AddImageUseCase.AddImageUseCaseListener> implements
        ActivityResultCallback<ActivityResult>  {


    public interface AddImageUseCaseListener{
        void onImageAddedFromGallery(boolean isCountExceedAllowed, Uri... images);
        void onCanceled();

    }


    private final int current_images_count;



    public AddImageUseCase(Activity activity, int current_images_count) {
        super(activity);
        registerLauncher(this);

        this.current_images_count = current_images_count;


    }


    public void addImageFromGallery(){
        //open gallery to get more image/s to the current images list
        openGalleryForImages();

    }


    @Override
    public void onActivityResult(ActivityResult result) {


        for (AddImageUseCaseListener listener : getListeners())


            //check if the user select image/s and clicked ok
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {


                if (result.getData().getClipData() != null){//wil not equal null if only the user selected multiple images



                        //evaluate the count before the for loop
                        int count = result.getData().getClipData().getItemCount();

                        //images array
                        Uri [] images = new Uri[count];


                        //ensure that the added images count doesn't exceed the allowed number
                        for (int i = 0; i < (Constants.MAX_ALLOWED_NUMBER_OF_Images - current_images_count); i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            images[i] = imageUri;

                        }

                        //notify that images added correctly
                        listener.onImageAddedFromGallery(
                                //notify that images count exceeded the allowed number with respect to current count or not
                                Constants.MAX_ALLOWED_NUMBER_OF_Images >= (count + current_images_count),

                                //the images array
                                images);




                    //otherwise, check if the user selected one image and clicked ok
                    } else if (result.getData().getData() != null){


                    Uri imageUri = result.getData().getData();

                    //notify that the image added correctly
                    listener.onImageAddedFromGallery(false, imageUri);


                }

            } else //otherwise notify if the user clicked cancel or any error
                listener.onCanceled();


    }
}
