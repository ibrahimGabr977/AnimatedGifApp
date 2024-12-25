package com.hope.igb.catgif.screens.generate.fromimages;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.comman.Constants;
import com.hope.igb.catgif.imagescontroller.AddImageUseCase;
import com.hope.igb.catgif.imagescontroller.BrowseImagesUseCase;
import com.hope.igb.catgif.screens.comman.base.BaseFragment;
import com.hope.igb.catgif.screens.comman.screennavigator.ScreenNavigator;
import com.hope.igb.catgif.imagescontroller.ImagesGifGenerator;
import com.hope.igb.catgif.screens.comman.toasthelper.ToastHelper;
import com.hope.igb.catgif.screens.comman.dialoghelper.ProgressPopupWindowViewMvc;
import com.hope.igb.catgif.util.PermissionHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class GenerateFromImagesFragment extends BaseFragment implements
        GenerateFromImagesViewMvc.Listener,
        BrowseImagesUseCase.BrowseImagesListener,
        ImagesGifGenerator.GifImagesGenerationListener,
        ProgressPopupWindowViewMvc.PopupWindowListener,
        AddImageUseCase.AddImageUseCaseListener,
        PermissionHandler.PermissionGrantedListener {

     //the fragment view Mvc
    private GenerateFromImagesViewMvc viewMvc;
    //open gallery and get images from it
    private BrowseImagesUseCase browseUseCase;
    //open gallery again to add more image/s
    private AddImageUseCase addUseCase;

    //all_images represent all the images it's different from selected images arrayList because
    //there are add image and remove image which will modify the list
    private ArrayList<Uri> all_images;
     //screen navigator to navigate for other activities
     private ScreenNavigator screenNavigator;
     //show system messages to the user
     private ToastHelper toastHelper;



     //the full path with name and extension of the gif file that will be created
     private String gif_file_name;
     private ImagesGifGenerator imagesGifGenerator;



     //generating and saving progress window
    private ProgressPopupWindowViewMvc progressViewMvc;


    //for granting storage permission for saving gif
    private PermissionHandler permissionHandler;




     

     //create one instance that contains the screen width
     public static GenerateFromImagesFragment newInstance(int screen_width) {
         Bundle args = new Bundle();
         args.putInt("SCREEN_WIDTH", screen_width);
         GenerateFromImagesFragment fragment = new GenerateFromImagesFragment();
         fragment.setArguments(args);
         return fragment;
     }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        viewMvc = new GenerateFromImagesViewMvc(inflater, container);

        browseUseCase = new BrowseImagesUseCase(getActivity());




        screenNavigator = new ScreenNavigator(getActivity());
        toastHelper = new ToastHelper(getContext());


        //on the first launch of fragment browse images automatically
         if (savedInstanceState == null){
             browseUseCase.browseImagesFromGallery();
             browseUseCase.registerListener(this);
             all_images = new ArrayList<>();

         }


        addUseCase = new AddImageUseCase(requireActivity(), all_images.size());




        return viewMvc.getRootView();
    }



    //get the screen width to fit the percentage dimensions on the recycler adapter
    private int getScreenWidth(){
        assert getArguments() != null;
        return getArguments().getInt("SCREEN_WIDTH");
    }



     @Override
     public void onStart() {
         super.onStart();
         //register view mvc listener
         viewMvc.registerListener(this);


         //bind images recycler adapter
         viewMvc.bindRecyclerView(all_images, getScreenWidth());

     }


     @Override
     public void onStop() {
         super.onStop();
         //unregister the view mvc  and add image use case listeners
         viewMvc.unregisterListener(this);




         //unregister progress and generator listeners if their instances already created
         // and delete the incomplete file if stopped during generated
         unregisterProcess();

     }




    //============================view mvc implementation====================================================


    //on generate gif click listener
    @Override
    public void generateGIFFromImages() {
         permissionHandler = new PermissionHandler(requireActivity());

        if (permissionHandler.isStoragePermissionGranted()){
            generateTheGif();
        }else
            permissionHandler.registerListener(this);

    }


    private void generateTheGif(){

        if (all_images.size() == 1){
            //the user tries to make gif from only one image
            toastHelper.notEnoughImagesToGenerate();
        }else {


            progressViewMvc = new ProgressPopupWindowViewMvc(LayoutInflater.from(getContext()));


            boolean isFolderCreated = true;

            if (!getBase_folder().exists())
                isFolderCreated = getBase_folder().mkdir();


            if (isFolderCreated){
                gif_file_name = getBase_folder().getAbsolutePath()+"/"+System.currentTimeMillis();

                imagesGifGenerator = new ImagesGifGenerator(gif_file_name, requireActivity(), all_images);

                imagesGifGenerator.execute();
                imagesGifGenerator.registerListener(this);



            }else
                toastHelper.appFolderNotFound();

        }

    }



    //==============================permission handler implementation===============================
    @Override
    public void onPermissionGranted() {
        generateTheGif();
        permissionHandler.unregisterListener(this);
    }

    @Override
    public void onPermissionNotGranted() {
        permissionHandler.unregisterListener(this);
        toastHelper.requiredStoragePermission("generating gif.");
    }
    //=============================================================================================




    //add new image to the list click listener
    @Override
    public void onAddImageClickListener() {

         //check if the number of images in the current list doesn't exceed the allowed one
         if (all_images.size() != Constants.MAX_ALLOWED_NUMBER_OF_Images){
             addUseCase.addImageFromGallery();
             addUseCase.registerListener(this);

         }else
             toastHelper.invalidImagesNumber();

    }



    @Override
    public void onImageRemoved() {
         if (all_images.size() == 1){
             screenNavigator.toSelectActivity();
             requireActivity().finish();
         }

    }

    @Override
    public void onImagesListReversed() {
        toastHelper.comingSoon("Reverse Gif");
    }

    //=============================================================================================================






    //========================================browse images use case implementation================================




    @Override
    public void onImagesSelected(ArrayList<Uri> selected_images) {
        //add all selected images to all_images list
        //on the first launch of fragment the 2 lists will equivalent
        all_images.addAll(selected_images);

        browseUseCase.unregisterListener(this);
    }



    @Override
    public void onNoImagesSelected(String problem) {
        //close the activity if no images selected
        if (getActivity() != null) {

            if (problem.equals("Nothing"))
                toastHelper.noMediaSelected("images");
            else
                toastHelper.notEnoughImagesSelected();

            requireActivity().finish();

        }

    }



     @Override
     public void onInvalidNumberOfImagesSelected() {
         //message if the number of selected images (all_images list) exceed the allowed one
         toastHelper.invalidImagesNumber();
         if (getActivity() != null)
             requireActivity().finish();

     }



     //==============================================================================================================



    //==================================Add image from gallery use case implementation===============================

    @Override
    public void onImageAddedFromGallery(boolean isCountExceedAllowed, Uri... images) {
        if (isCountExceedAllowed)
            toastHelper.invalidImagesNumber();

        Collections.addAll(all_images, images);

        addUseCase.unregisterListener(this);
    }

    @Override
    public void onCanceled() {
        toastHelper.noMediaSelected("images");

        addUseCase.unregisterListener(this);
    }




    //================================================================================================================






    //========================================gif from images generator implementation================================

     //on start generating gif from images listener
    @Override
    public void onStartGenerating() {

         setIn_progress(true);
         requireActivity().runOnUiThread(this.progressViewMvc::showProgressWindow);


        progressViewMvc.registerListener(this);


    }



    @Override
    public void onGifCreatingProgressChanged(int progress) {
        //notify the popup window with the progress
        progressViewMvc.setOnProgressChanged(progress);
        //notify the popup window with the current process
        progressViewMvc.setOnProcessChanged(getString(R.string.generating));

    }


    //on finished generating and saving gif listener
     @Override
     public void onGifSaved(String generated_file) {
         setIn_progress(false);
         progressViewMvc.unregisterListener(this);
         progressViewMvc.dismissWindow();


         //scan gallery to show the generated file
         scanNewGif(generated_file);

         //generated_file represents a file full path like /sdcard/Pictures/test.gif
         toastHelper.successfullyGeneration();
         screenNavigator.toLibraryActivity(generated_file);


     }


     //======================================================================================================




    //========================================progress window implementation==================================

    @Override
    public void onProgressCanceled() {
           unregisterProcess();
    }


    private void unregisterProcess(){


         if (progressViewMvc != null){
             progressViewMvc.unregisterListener(this);
             progressViewMvc.dismissWindow();
         }

         if (imagesGifGenerator != null){
             imagesGifGenerator.unregisterListener(this);
             imagesGifGenerator.setCanceled(true);
         }

         deleteIncompleteFile();


        setIn_progress(false);


    }


    private void deleteIncompleteFile(){
         //delete incomplete file if it's already exist and there is an active process
        // to avoid deleting in onStop that wasn't happened during generating

        if (gif_file_name != null && isIn_progress()){

            File gif_file = new File(gif_file_name+".gif");
            boolean isDeleted = true;


            if (gif_file.exists())
                isDeleted = gif_file.delete();


            if (isDeleted)
                toastHelper.successfullyDeleted();

        }
    }



}
