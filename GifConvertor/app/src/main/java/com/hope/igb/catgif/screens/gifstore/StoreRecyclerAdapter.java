package com.hope.igb.catgif.screens.gifstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hope.igb.catgif.networking.models.OnlineGif;
import com.hope.igb.catgif.gifcontroller.GifPlayer;

import java.util.ArrayList;

public class StoreRecyclerAdapter extends RecyclerView.Adapter<StoreRecyclerAdapter.StoreViewHolder>
        implements StoreItemViewMvc.StoreItemListener {

    private final Context context;
    private final ArrayList<OnlineGif> onlineGIFs;
    private final GifPlayer gifPlayer;
    private final int screenWidth;
    private final StoreAdapterListener listener;



    protected interface StoreAdapterListener{
        void onGifClicked(String gif_id);
    }


    public StoreRecyclerAdapter(Context context, ArrayList<OnlineGif> onlineGIFs, GifPlayer gifPlayer, int screenWidth,
                                StoreAdapterListener listener) {
        this.context = context;
        this.onlineGIFs = onlineGIFs;
        this.gifPlayer = gifPlayer;
        this.screenWidth = screenWidth;
        this.listener = listener;
    }




    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StoreItemViewMvc viewMvc = new StoreItemViewMvc(LayoutInflater.from(context), parent,
                gifPlayer, screenWidth);
        viewMvc.registerListener(this);
        return new StoreViewHolder(viewMvc);
    }





    @Override
    public void onViewDetachedFromWindow(@NonNull StoreViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.viewMvc.unregisterListener(this);
    }


    @Override
    public void onViewAttachedToWindow(@NonNull StoreViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.viewMvc.registerListener(this);
    }


    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        holder.viewMvc.bindOnlineGIFs(onlineGIFs.get(position));

    }

    @Override
    public int getItemCount() {
        return onlineGIFs.size();
    }






    //view holder components

    @Override
    public void onOnlineGifClicked(String gif_id) {
        listener.onGifClicked(gif_id);
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {

        private final StoreItemViewMvc viewMvc;

        public StoreViewHolder(@NonNull StoreItemViewMvc viewMvc) {
            super(viewMvc.getRootView());
            this.viewMvc = viewMvc;
        }
    }
}
