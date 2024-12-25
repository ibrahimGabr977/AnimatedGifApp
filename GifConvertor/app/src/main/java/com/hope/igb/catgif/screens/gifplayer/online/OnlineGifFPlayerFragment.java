package com.hope.igb.catgif.screens.gifplayer.online;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.networking.firebasedatabase.FetchGifDetailsUseCase;
import com.hope.igb.catgif.networking.firebasestorage.FileDownloader;
import com.hope.igb.catgif.networking.models.OnlineGif;
import com.hope.igb.catgif.screens.comman.base.BaseFragment;
import com.hope.igb.catgif.screens.comman.dialoghelper.InfoDialogHelper;
import com.hope.igb.catgif.screens.comman.dialoghelper.ProgressPopupWindowViewMvc;
import com.hope.igb.catgif.screens.comman.toasthelper.ToastHelper;
import com.hope.igb.catgif.gifcontroller.GifPlayer;
import com.hope.igb.catgif.util.PermissionHandler;
import java.io.File;


/**
 * online here meaning that this class for playing gif from url from store server database
 */


public class OnlineGifFPlayerFragment extends BaseFragment implements
        OnlineGifPlayerFragmentViewMvc.Listener,
        FetchGifDetailsUseCase.FetchGifDetailsListener,
        FileDownloader.FileDownloaderListener,
        PermissionHandler.PermissionGrantedListener,
        ProgressPopupWindowViewMvc.PopupWindowListener {



    //the view mvc instance
    private OnlineGifPlayerFragmentViewMvc viewMvc;
    //the model class object
    private OnlineGif onlineGif;
    //fetch gif details from server database object
    private FetchGifDetailsUseCase fetchDetailsUseCase;
    //shared preference class for storing the liked gif list
    private LikedGIFsList likedGIFsList;
    //the targeted gif that sent from previous activity
    private String gif_id;
    //the downloaded gif destination file
    private File dest_downloaded_file;
    //to displaying toasts
    private ToastHelper toastHelper;
    //for downloading the gif if user asked
    private FileDownloader downloader;
    //for granting storage permission for downloading(saving)
    private PermissionHandler permissionHandler;
    //for handling saving progress
    private ProgressPopupWindowViewMvc progressViewMvc;


    //get the targeted gif_id from the previous activity on new instance created
    public static OnlineGifFPlayerFragment newInstance(String gif_id){
        Bundle args = new Bundle();
        args.putString("GIF_ID", gif_id);
        OnlineGifFPlayerFragment fragment = new OnlineGifFPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null)
            gif_id = getArguments().getString("GIF_ID");



        GifPlayer gifPlayer = new GifPlayer(getContext());

        likedGIFsList = new LikedGIFsList(requireContext());

        viewMvc = new OnlineGifPlayerFragmentViewMvc(inflater, container, gifPlayer, likedGIFsList.isAlreadyLiked(gif_id));

        fetchDetailsUseCase = new FetchGifDetailsUseCase(gif_id);

        toastHelper = new ToastHelper(getContext());


        return viewMvc.getRootView();
    }




    @Override
    public void onStart() {
        super.onStart();

        viewMvc.registerListener(this);

        fetchDetailsUseCase.fetchGifDetails();
        fetchDetailsUseCase.registerListener(this);
    }



    @Override
    public void onStop() {
        super.onStop();
        viewMvc.unregisterListener(this);

        fetchDetailsUseCase.unregisterListener(this);

        //release incomplete processes
        releaseProcesses();

    }





    //========================================fetchDetailsUseCase implementation=============================================

    @Override
    public void onGifDetailsFetched(OnlineGif onlineGif) {
        //listen for gif fetched
        this.onlineGif = onlineGif;
        viewMvc.bindPlayerView(onlineGif.getDownloadUrl(), onlineGif.getLikes_count());
    }



    @Override
    public void onFetchedCanceled(String message) {
        toastHelper.showCertainMessage(message);
    }

  //=========================================================================================================================




    //==========================================the view mvc implementation==================================================



    @Override
    public void onBackClicked() {
        requireActivity().onBackPressed();
    }



    @Override
    public void onShareClicked() {
        Intent sharing_intent = new Intent(Intent.ACTION_SEND);
        sharing_intent.setType("text/plain");
        sharing_intent.putExtra(Intent.EXTRA_TEXT, onlineGif.getDownloadUrl());
        startActivity(Intent.createChooser(sharing_intent, "Share via"));
    }




    @Override
    public void onGifInfoClicked() {
        //display a dialog with the gif file information
        InfoDialogHelper infoDialogHelper = new InfoDialogHelper(getContext());
        infoDialogHelper.onlineGifInfo(onlineGif.getFileInfo() + onlineGif.getLikes_count());

    }





    @Override
    public void onLikeClicked() {
        int new_value;

        if (likedGIFsList.isAlreadyLiked(gif_id)){ //if this gif already liked then remove like from the preference list

            likedGIFsList.removeLike(gif_id);
            new_value = onlineGif.getLikes_count() - 1;



        }else { //if this gif hasn't already liked then put like on the preference list

            likedGIFsList.putLike(gif_id);
            new_value = onlineGif.getLikes_count() + 1; }



        //update the server with the new value then update the current object
        fetchDetailsUseCase.editLikes(new_value);
        onlineGif.setLikes_count(new_value);



    }



    @Override
    public void onSaveClicked() {

        permissionHandler = new PermissionHandler(requireActivity());


        if (permissionHandler.isStoragePermissionGranted()){
            saveFile();
        }else
            permissionHandler.registerListener(this);



    }


    private void saveFile(){

        boolean isFolderCreated = true;
        //check if the app folder created to create it
        if (!getBase_folder().exists())
            isFolderCreated = getBase_folder().mkdir();

        //if the app folder created start downloading
        if (isFolderCreated){

            //for telling activity that there is an active process working right now
            setIn_progress(true);

            //the destination for downloading file and its name random
            dest_downloaded_file = new File(getBase_folder().getAbsolutePath()+"/gif_"+System.currentTimeMillis()+".gif");
            //file downloader from firebase storage class
            downloader = new FileDownloader(onlineGif.getDownloadUrl());
            //start downloading
            downloader.downloadGifToFile(dest_downloaded_file);
            //listen for downloading process
            downloader.registerListener(this);


            //handle downloading progress
            progressViewMvc = new ProgressPopupWindowViewMvc(LayoutInflater.from(getContext()));
            progressViewMvc.registerListener(this);
        }

    }

    //=======================================================================================================================


    //===========================================File saving downloader implementation========================================

    @Override
    public void onDownloaded() {
        //refresh gallery to show the downloaded gif
        scanNewGif(dest_downloaded_file.getPath());

        toastHelper.successfullyDownloadedTo(dest_downloaded_file.getAbsolutePath());
        downloader.unregisterListener(this);
        setIn_progress(false);
    }

    @Override
    public void onFailedDownloaded(String error_message) {
        toastHelper.showCertainMessage(error_message);
        downloader.unregisterListener(this);
        setIn_progress(false);
    }

    @Override
    public void onProgressChanged(int progress) {

        //notify the popup window with the progress
        progressViewMvc.setOnProgressChanged(progress);
        //notify the popup window with the current process
        progressViewMvc.setOnProcessChanged(getString(R.string.saving));

    }


    //progress view mvc implementation
    @Override
    public void onProgressCanceled() {
        releaseProcesses();
    }



    //======================permission handler class implementation for storage permission===========================

    @Override
    public void onPermissionGranted() {
        saveFile();
        permissionHandler.unregisterListener(this);
    }

    @Override
    public void onPermissionNotGranted() {
        toastHelper.requiredStoragePermission("saving gif.");
        permissionHandler.unregisterListener(this);
    }



    @SuppressWarnings("ResultOfMethodCallIgnored")
    //unregister unneeded listeners and release any incomplete process
    private void releaseProcesses(){
        //unregister storage file downloader
        if (downloader != null)
            downloader.unregisterListener(this);

        //dismiss and unregister progress window
        if (progressViewMvc != null){
            progressViewMvc.dismissWindow();
            progressViewMvc.unregisterListener(this);
        }


        //if the gif file didn't fully downloaded delete the incomplete file
        if (dest_downloaded_file != null && dest_downloaded_file.exists() && isIn_progress())
            dest_downloaded_file.delete();



        //end in progress status
        setIn_progress(false);




    }

}
