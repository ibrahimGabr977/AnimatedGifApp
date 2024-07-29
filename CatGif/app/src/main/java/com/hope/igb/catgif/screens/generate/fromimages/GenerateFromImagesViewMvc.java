package com.hope.igb.catgif.screens.generate.fromimages;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;

import java.util.ArrayList;
import java.util.Objects;

class GenerateFromImagesViewMvc extends BaseObservableMvc<GenerateFromImagesViewMvc.Listener> implements
        ImagesRecyclerAdapter.Listener,
        View.OnClickListener {


    private final RecyclerView recyclerView;
    //the current selected image position
    private static int current_index = 1;
    private ImagesRecyclerAdapter adapter;

    interface Listener{
        void generateGIFFromImages();
        void onAddImageClickListener();
        void onImageRemoved();
        void onImagesListReversed();
    }


    GenerateFromImagesViewMvc(LayoutInflater inflater, ViewGroup parent){


        setRootView(inflater.inflate(R.layout.fragment_generate_from_images, parent, false));

        recyclerView = findViewById(R.id.generateImagesRecyclerView);


        initView();



    }



    private void initView(){
        ImageView generate = findViewById(R.id.generate_images_image_btn);
        ImageView delete_image = findViewById(R.id.generate_images_delete);
        ImageView order_up = findViewById(R.id.generate_images_order_up);
        ImageView order_down = findViewById(R.id.generate_images_order_down);
        ImageView reverse_order = findViewById(R.id.generate_images_reverse);


        //generate the gif button
        generate.setOnClickListener(this);
        //delete all images list button
        delete_image.setOnClickListener(this);
        //swap image to right = increase the position of image on list by 1
        order_up.setOnClickListener(this);
        //swap image to left = decrease the position of image on list by 1
        order_down.setOnClickListener(this);
        //reverse the items order
        reverse_order.setOnClickListener(this);

    }





    protected void bindRecyclerView(ArrayList<Uri> images, int screenWidth){


        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),4 ,LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImagesRecyclerAdapter(getContext(), images, screenWidth, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }


    @Override
    public void onAddImageClickListener() {
        for (Listener listener: getListeners())
            listener.onAddImageClickListener();
    }



    @Override
    public void onImageClickedListener(int selected, int last_selected) {
        recyclerView.scrollToPosition(selected);

        current_index = selected > 0 ? selected : 1;

        //on image selected shaded the last selected one
        getImageHolderViewMvcAtPosition(last_selected).getView_hider().setVisibility(View.VISIBLE);

    }




    private ImageItemViewMvc getImageHolderViewMvcAtPosition(int position){

        //findViewHolderForAdapterPosition return null if the item didn't draw yet
        return ((ImagesRecyclerAdapter.ImagesViewHolder)
                Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)))
                .getViewMvc();
    }




    //to ensure that the same selected image stay selected after change it position
    private void onImageMoved(int to_index){

        getImageHolderViewMvcAtPosition(current_index).getView_hider().setVisibility(View.VISIBLE);

        getImageHolderViewMvcAtPosition(to_index).getView_hider().setVisibility(View.GONE);

        ImageItemViewMvc.setLast_selected(to_index);


        current_index = to_index;

    }



    //notify the fragment with image moved to a specified
    private void notifyImageMoved(int to_index){

        adapter.moveItem(current_index , to_index);
        recyclerView.scrollToPosition(to_index);

        //ui shaded changed handler method
        onImageMoved(to_index);


    }










    //handle ui on last item removed
    private void onLastImageRemoved(){

        current_index = adapter.getItemCount() - 1;

        //manually unshaded the new last (index) image
        getImageHolderViewMvcAtPosition(current_index).getView_hider().setVisibility(View.GONE);

        //manually set the last_selected to the new last index
        ImageItemViewMvc.setLast_selected(current_index);
    }




    //notify the controller (fragment) that an image removed
    private void notifyImageRemoved(){

        adapter.removeItem(current_index);

        if (current_index == adapter.getItemCount())
            onLastImageRemoved();



        for (Listener listener: getListeners())
            listener.onImageRemoved();

    }




    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.generate_images_image_btn){
            for (Listener listener: getListeners())
                listener.generateGIFFromImages();


        }else if (v.getId() == R.id.generate_images_delete){

            for (Listener listener: getListeners())
                listener.onImagesListReversed();


        }else if (v.getId() == R.id.generate_images_reverse){

            notifyImageRemoved();



        }else if (v.getId() == R.id.generate_images_order_up){

            if (current_index < adapter.getItemCount() - 1){

               notifyImageMoved(current_index + 1);
            }

        }else if (v.getId() == R.id.generate_images_order_down){
            if (current_index > 1){
               notifyImageMoved(current_index - 1);

            }

        }


    }


}
