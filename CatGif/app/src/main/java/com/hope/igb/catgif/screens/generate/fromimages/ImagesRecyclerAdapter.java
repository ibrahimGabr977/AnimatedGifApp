package com.hope.igb.catgif.screens.generate.fromimages;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;

class ImagesRecyclerAdapter extends RecyclerView.Adapter<ImagesRecyclerAdapter.ImagesViewHolder>
        implements
        ImageItemViewMvc.Listener {

    private final ArrayList<Uri> images;
    private final Context context;
    private final int screenWidth;
    private final Listener listener;

    ImagesRecyclerAdapter(Context context, ArrayList<Uri> images, int screenWidth, Listener listener) {
        this.context = context;
        this.images = images;
        this.screenWidth = screenWidth;
        this.listener = listener;
    }



    interface Listener {
        void onAddImageClickListener();
        void onImageClickedListener(int selected, int last_selected);

    }




    @Override
    public void onViewDetachedFromWindow(@NonNull ImagesViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.viewMvc.unregisterListener(this);
    }


    @Override
    public void onViewAttachedToWindow(@NonNull ImagesViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.viewMvc.registerListener(this);
    }

    @NonNull
    @Override
    public ImagesRecyclerAdapter.ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ImageItemViewMvc viewMvc = new ImageItemViewMvc(LayoutInflater.from(context), parent, screenWidth);
        viewMvc.registerListener(this);
        return new ImagesViewHolder(viewMvc);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesRecyclerAdapter.ImagesViewHolder holder, int position) {

        //pass null in case of position 0 because it's not image but (add_image button)
        // the length of list equal the list length + 1 so it need to back to normal
        holder.viewMvc.bindViewImages(position == 0 ? null : images.get(position - 1), position);



    }




    protected void moveItem(int from_index, int to_index) {

        if (from_index < getItemCount()) {



            Collections.swap(this.images, (from_index - 1), (to_index - 1));
            notifyItemMoved(from_index , to_index);

            if (from_index < to_index) {
                notifyItemRangeChanged(from_index, (to_index - from_index) + 1);
            } else {
                notifyItemRangeChanged(to_index, (from_index - to_index) + 1);

            }
        }
    }



    public void removeItem(int index) {
        //the length of all_images list is less than adapter list count because there is and extra item (add_image)
        images.remove(index - 1);

        notifyItemRemoved(index);
        notifyItemRangeChanged(index, getItemCount());
    }






    @Override
    public int getItemCount() {
        return images.size() + 1;
    }



    @Override
    public void onAddImageClickListener() {
        listener.onAddImageClickListener();
    }


    @Override
    public void onImageClicked(int selected, int last_selected) {
        listener.onImageClickedListener(selected, last_selected);


    }






    static class ImagesViewHolder extends RecyclerView.ViewHolder {

        private final ImageItemViewMvc viewMvc;

        public ImagesViewHolder(ImageItemViewMvc viewMvc) {
            super(viewMvc.getRootView());

            this.viewMvc = viewMvc;


        }

        public ImageItemViewMvc getViewMvc() {
            return viewMvc;
        }
    }
}
