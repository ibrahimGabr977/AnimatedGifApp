package com.hope.igb.catgif.screens.generate.fromvideo;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.videocontroller.BrowseVideoUseCase;
import com.hope.igb.catgif.videocontroller.VideoGifGenerator;
import com.hope.igb.catgif.videocontroller.VideoLoader;
import com.hope.igb.catgif.screens.comman.base.BaseFragment;
import com.hope.igb.catgif.screens.comman.screennavigator.ScreenNavigator;
import com.hope.igb.catgif.screens.comman.toasthelper.ToastHelper;
import com.hope.igb.catgif.screens.comman.dialoghelper.ProgressPopupWindowViewMvc;
import com.hope.igb.catgif.util.PermissionHandler;

import java.io.File;



public class GenerateFromVideoFragment extends BaseFragment implements
        GenerateFromVideoViewMvc.Listener,
        BrowseVideoUseCase.Listener ,
        VideoGifGenerator.GifVideoGenerationListener,
        ProgressPopupWindowViewMvc.PopupWindowListener,
        PermissionHandler.PermissionGrantedListener{

    //the fragment view Mvc
    private GenerateFromVideoViewMvc viewMvc;
    //is_first_launch check if this is this fragment launched for the first time
    private  boolean is_first_launch = true;
    //screen navigator to navigate for other activities
    private ScreenNavigator screenNavigator;
    //show system messages to the user
    private ToastHelper toastHelper;
    private Uri selected_video;

    //the full path with name and extension of the gif file that will be created
    private String gif_file_name;

    //the generator instance
    private VideoGifGenerator videoGifGenerator;

    //generating and saving progress window
    private ProgressPopupWindowViewMvc progressViewMvc;

    //browse video from gallery class
    private BrowseVideoUseCase browseUseCase;

    //for granting storage permission for saving gif
    private PermissionHandler permissionHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        VideoLoader videoLoader = new VideoLoader(getContext());


        viewMvc = new GenerateFromVideoViewMvc(inflater, container, videoLoader);
        browseUseCase = new BrowseVideoUseCase(requireActivity());



        screenNavigator = new ScreenNavigator(getActivity());
        toastHelper = new ToastHelper(getContext());


        //on the first launch of fragment browse video automatically
        if (savedInstanceState == null){
            //call browse use case and register its listener
            browseUseCase.browseVideoFromGallery();
            browseUseCase.registerListener(this);

        }





        return viewMvc.getRootView();

    }




    @Override
    public void onStart() {
        super.onStart();
        viewMvc.registerListener(this);




    }

    @Override
    public void onStop() {
        super.onStop();
        viewMvc.unregisterListener(this);


        if (viewMvc.isPlaying())
        viewMvc.pauseVideo();



        //unregister progress and generator listeners if their instances already created
        // and delete the incomplete file if stopped during generated
        unregisterProcess();

    }




    //=========================================view mvc listener implementation============================



    
    @Override
    public void generateGIFFromVideo() {
        permissionHandler = new PermissionHandler(requireActivity());

        if (permissionHandler.isStoragePermissionGranted()){
            generateTheGif();
        }else
            permissionHandler.registerListener(this);
    }


    private void generateTheGif(){

        progressViewMvc = new ProgressPopupWindowViewMvc(LayoutInflater.from(getContext()));

        boolean isFolderCreated = true;


        if (!getBase_folder().exists())
            isFolderCreated = getBase_folder().mkdir();




        if (isFolderCreated){


            gif_file_name = getBase_folder().getAbsolutePath()+"/"+System.currentTimeMillis();


            //viewMvc.isImproved() check if the desired gif for creation is improved version
            // or default version from the radio group
            //the different between versions discussed on MyMediaRetriever class

            videoGifGenerator = new VideoGifGenerator(requireActivity(), selected_video, gif_file_name,  viewMvc.isBriefly());

            videoGifGenerator.execute();

            videoGifGenerator.registerListener(this);


        }else
            toastHelper.appFolderNotFound();

    }


    //==============================permission handler implementation=============================
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

    @Override
    public void onReverseGifClicked() {
        toastHelper.comingSoon("Reverse Gif");
    }

    @Override
    public void onChangeVideoClicked() {
        browseUseCase.browseVideoFromGallery();
        browseUseCase.registerListener(this);
    }


    //=========================================================================================================






    //======================================browse video use case implementation================================




    @Override
    public void onVideoSelected(Uri videoUri) {
        selected_video = videoUri;
        viewMvc.bindVideoView(videoUri);
        is_first_launch = false;

        browseUseCase.unregisterListener(this);

    }

    @Override
    public void onNoVideoSelected() {
        browseUseCase.unregisterListener(this);

        //close the activity if this is first launch of the fragment and no video selected
        if (getActivity() != null && is_first_launch){
            toastHelper.noMediaSelected("video");
            getActivity().finish();

        }

    }



    @Override
    public void onInvalidVideoSelected() {
        //message if the selected video duration exceed the allowed one
        toastHelper.invalidVideo();
        browseUseCase.unregisterListener(this);

        if (getActivity() != null && is_first_launch)
            getActivity().finish();

    }


    //==================================================================================================





    //===================================gif from video generator implementation=========================


    //on start the whole generating process
    @Override
    public void onStartGenerating() {
        setIn_progress(true);
        requireActivity().runOnUiThread(this.progressViewMvc::showProgressWindow);

        progressViewMvc.registerListener(this);
    }


    //extracting the video frames
    @Override
    public void onFramesExtractionProgressChanged(int progress) {
        progressViewMvc.setOnProgressChanged(progress);
        progressViewMvc.setOnProcessChanged(getString(R.string.extracting));

    }


    //on extracting process finished
    @Override
    public void onFramesExtractionProgressFinished() {
        progressViewMvc.setOnProgressChanged(0);
        progressViewMvc.setOnProcessChanged(getString(R.string.preparing));

    }


    //generating gif process progress listener
    @Override
    public void onGifCreatingProgressChanged(int progress) {

        progressViewMvc.setOnProgressChanged(progress);
        progressViewMvc.setOnProcessChanged(getString(R.string.generating));

    }


    //on give saved to storage listener
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



    //========================================================================================================





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

        if (videoGifGenerator != null){
            videoGifGenerator.unregisterListener(this);
            videoGifGenerator.setCanceled(true);
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
