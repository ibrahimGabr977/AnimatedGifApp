package com.hope.igb.catgif.screens.gifplayer.offline;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hope.igb.catgif.screens.comman.base.BaseFragment;
import com.hope.igb.catgif.screens.comman.toasthelper.ToastHelper;
import com.hope.igb.catgif.gifcontroller.GifPlayer;

public class GifPlayerFragment extends BaseFragment implements
        GifPlayerFragmentViewMvc.GifPlayerViewMvcListener,
        BrowseGifUseCase.BrowseGifListener {

    private  GifPlayerFragmentViewMvc viewMvc;
    private BrowseGifUseCase browseUseCase;
    private boolean is_first_launch = true;
    private ToastHelper toastHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        GifPlayer gifPlayer = new GifPlayer(getContext());
        viewMvc = new GifPlayerFragmentViewMvc(inflater, container, gifPlayer);
        browseUseCase = new BrowseGifUseCase(requireActivity());
        toastHelper = new ToastHelper(getContext());

        if (savedInstanceState == null){
            browseUseCase.BrowseGifFromGallery();
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
    }



    @Override
    public void onBackClicked() {
        requireActivity().onBackPressed();

    }

    @Override
    public void onBrowseClicked() {
        browseUseCase.BrowseGifFromGallery();
        browseUseCase.registerListener(this);

    }




    @Override
    public void onGifSelected(Uri gif) {
        viewMvc.bindPlayerView(gif);
        viewMvc.registerListener(this);
        is_first_launch = true;

        browseUseCase.unregisterListener(this);
    }



    @Override
    public void onInvalidOrErrorFileSelected() {

        toastHelper.invalidOrErrorFileSelected("Gif");
        browseUseCase.unregisterListener(this);

        if (is_first_launch)
            requireActivity().finish();

    }




    @Override
    public void onNoGifSelected() {
        toastHelper.noMediaSelected("Gif");
        browseUseCase.unregisterListener(this);

        if (is_first_launch)
            requireActivity().finish();
    }
}
