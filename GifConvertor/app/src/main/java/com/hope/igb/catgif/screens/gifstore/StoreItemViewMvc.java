package com.hope.igb.catgif.screens.gifstore;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hope.igb.catgif.R;
import com.hope.igb.catgif.networking.models.OnlineGif;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;
import com.hope.igb.catgif.gifcontroller.GifPlayer;

class StoreItemViewMvc extends BaseObservableMvc<StoreItemViewMvc.StoreItemListener> {

     protected interface StoreItemListener {
         void onOnlineGifClicked(String gif_id);
     }

     private final ImageView gif_view;
     private final GifPlayer gifPlayer;



     StoreItemViewMvc(LayoutInflater inflater, ViewGroup parent, GifPlayer gifPlayer, int screenWidth) {

         setRootView(inflater.inflate(R.layout.store_item_view_holder, parent, false));
         this.gifPlayer = gifPlayer;





         //set the height and width of view by percentage value to make exactly 3 items per row
         ViewGroup.LayoutParams params = getRootView().getLayoutParams();

         //the view percentage dimensions
         params.width = (int) ((screenWidth * 0.915) / 3) - 10;
         params.height = (int) ((screenWidth * 0.915) / 3) - 10;



         getRootView().setLayoutParams(params);


         gif_view = findViewById(R.id.store_item_imageView);

     }



     public void bindOnlineGIFs(OnlineGif onlineGif){

         gifPlayer.playOnlineGif(onlineGif.getDownloadUrl())
                 .into(gif_view);


         for (StoreItemListener listener : getListeners())
             listener.onOnlineGifClicked(onlineGif.getId());

     }






}
