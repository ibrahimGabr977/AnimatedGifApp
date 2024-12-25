package com.hope.igb.catgif.screens.gifstore;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hope.igb.catgif.R;
import com.hope.igb.catgif.networking.models.OnlineGif;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;
import com.hope.igb.catgif.gifcontroller.GifPlayer;

import java.util.ArrayList;


class GifStoreViewMvc extends BaseObservableMvc<GifStoreViewMvc.GifStoreListener> implements
         StoreRecyclerAdapter.StoreAdapterListener,
         StoreCustomTabLayout.MyTabSelectedListener{



     private final RecyclerView recyclerView;
     private final int screenWidth;
     private final GifPlayer gifPlayer;
     private final StoreCustomTabLayout tabLayout;


    protected interface GifStoreListener {
         void onGifClicked(String gif_id);
         void onCategoryTabSelected(String category);
         void onBackClicked();
    }



    protected GifStoreViewMvc(LayoutInflater inflater, ViewGroup parent, int screenWidth, GifPlayer gifPlayer){



        this.screenWidth = screenWidth;
        this.gifPlayer = gifPlayer;

        setRootView(inflater.inflate(R.layout.activity_gif_store, parent, false));

        recyclerView = findViewById(R.id.storeRecyclerView);

        tabLayout = new StoreCustomTabLayout(getRootView());


        ImageView back = findViewById(R.id.store_back_image_btn);


        back.setOnClickListener(v -> {
            for (GifStoreListener listener: getListeners())
                listener.onBackClicked();

        });

    }



    protected void bindRecyclerAdapter( ArrayList<OnlineGif> onlineGIFs){
         StoreRecyclerAdapter adapter = new StoreRecyclerAdapter(getContext(), onlineGIFs, gifPlayer, screenWidth, this);
         GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3,
                GridLayoutManager.VERTICAL, false);


         recyclerView.setLayoutManager(layoutManager);
         recyclerView.setAdapter(adapter);

         adapter.notifyDataSetChanged();
    }


    @Override
    public void registerListener(GifStoreListener listener) {
        super.registerListener(listener);
        tabLayout.registerListener(this);
    }

    @Override
    public void unregisterListener(GifStoreListener listener) {
        super.unregisterListener(listener);
        tabLayout.unregisterListener(this);
    }




    @Override
     public void onGifClicked(String gif_id) {
         for (GifStoreListener listener: getListeners())
             listener.onGifClicked(gif_id);
     }

    @Override
    public void onTabSelected(String tab_name) {
        for (GifStoreListener listener: getListeners())
            listener.onCategoryTabSelected(tab_name);
    }

}
