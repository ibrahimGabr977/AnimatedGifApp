package com.hope.igb.catgif.screens.gifstore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.hope.igb.catgif.networking.firebasedatabase.FetchGIFsUseCase;
import com.hope.igb.catgif.networking.models.OnlineGif;
import com.hope.igb.catgif.screens.comman.base.BaseActivity;
import com.hope.igb.catgif.screens.comman.screennavigator.ScreenNavigator;
import com.hope.igb.catgif.screens.comman.toasthelper.ToastHelper;
import com.hope.igb.catgif.gifcontroller.GifPlayer;

import java.util.ArrayList;

public class GifStoreActivity extends AppCompatActivity implements
        FetchGIFsUseCase.FetchGIFsListener,
        GifStoreViewMvc.GifStoreListener
{


    private GifStoreViewMvc viewMvc;
    private FetchGIFsUseCase fetchUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity.setFullScreen(this);

        GifPlayer gifPlayer = new GifPlayer(this);
        viewMvc = new GifStoreViewMvc(LayoutInflater.from(this), null, BaseActivity.getScreen_width(),
                gifPlayer);


        setContentView(viewMvc.getRootView());



        if (savedInstanceState == null){
            fetchUseCase = new FetchGIFsUseCase();
            fetchUseCase.fetchAllGIFs();
            fetchUseCase.registerListener(this);

        }


    }


    @Override
    protected void onStop() {
        super.onStop();

        viewMvc.unregisterListener(this);
        fetchUseCase.unregisterListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();

        viewMvc.registerListener(this);

    }





    @Override
    public void onGIFsFetched(ArrayList<OnlineGif> onlineGIFs) {
        viewMvc.bindRecyclerAdapter(onlineGIFs);
        fetchUseCase.unregisterListener(this);
    }

    @Override
    public void onFetchedCanceled(String error_message) {
        fetchUseCase.unregisterListener(this);
        ToastHelper toastHelper = new ToastHelper(this);
        toastHelper.showCertainMessage(error_message);
    }







    @Override
    public void onGifClicked(String gif_id) {
        ScreenNavigator screenNavigator = new ScreenNavigator(this);
        screenNavigator.toPlayerActivity(gif_id);
    }

    @Override
    public void onCategoryTabSelected(String category) {
        switch (category) {
            case "top":
                fetchUseCase.fetchTopGIFs();
                break;
            case "funny":
                fetchUseCase.fetchGIFsByCategory("Funny");
                break;
            case "cute":
                fetchUseCase.fetchGIFsByCategory("Cute");
                break;
            default:
                fetchUseCase.fetchAllGIFs();
                break;
        }

        fetchUseCase.registerListener(this);


    }

    @Override
    public void onBackClicked() {
        finish();
    }
}