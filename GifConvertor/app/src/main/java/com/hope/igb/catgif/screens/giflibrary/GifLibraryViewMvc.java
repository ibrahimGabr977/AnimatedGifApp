package com.hope.igb.catgif.screens.giflibrary;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.gifcontroller.GifPlayer;
import com.hope.igb.catgif.screens.comman.customgridlayout.CustomGridLayoutManager;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;

import java.util.ArrayList;
import java.util.Objects;




public class GifLibraryViewMvc extends BaseObservableMvc<GifLibraryViewMvc.LibraryListener>
implements LibraryRecyclerAdapter.Listener, GifPlayer.GifPlayerListener{


    private final RecyclerView recyclerView;
    private final GifPlayer gifPlayer;
    private final ImageView gif_view;
    private final ImageView gif_sign;
    private final ArrayList<Uri> gif_files;





    protected interface LibraryListener {
        void onAddGifClicked();

    }


    public GifLibraryViewMvc(LayoutInflater inflater, ViewGroup parent, GifPlayer gifPlayer,
                             ArrayList<Uri> gif_files){


        setRootView(inflater.inflate(R.layout.activity_library, parent, false));

        this.gif_files = gif_files;
        this.gifPlayer = gifPlayer;



        recyclerView = findViewById(R.id.libraryRecyclerView);
        gif_view = findViewById(R.id.library_gif_view);
        gif_sign = findViewById(R.id.library_gif_sign_image);
    }








    public void bindView(int screen_width){


        if (gif_files.size() != 0){

            //horizontal gridlayout with 2 rows
            CustomGridLayoutManager gridLayoutManager = new CustomGridLayoutManager(getContext(),2,
                    RecyclerView.HORIZONTAL, false);

            //number of column per row
            gridLayoutManager.setColumnPerRow(4);
            //recyclerview width in respect to screen width
            gridLayoutManager.setRecyclerWidth(0.9);


            recyclerView.setLayoutManager(gridLayoutManager);
            LibraryRecyclerAdapter adapter = new LibraryRecyclerAdapter(getContext(), gif_files, screen_width, gifPlayer, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();



            gif_view.setVisibility(View.VISIBLE);



            //update ui with gif file index 0 on first launch
            onGifSelected(0);




            //play gif on gif_sign clicked
            gif_sign.setOnClickListener(v -> {


                gifPlayer.registerListener(this);
                gifPlayer.playGif().into(gif_view);

                gif_sign.setVisibility(View.GONE);




            });

        } else {
            //if there are none gif
            TextView no_gif_view = findViewById(R.id.library_no_gif_to_show_text);
            no_gif_view.setVisibility(View.VISIBLE);
        }



    }





    //update ui on gif selected
    private void onGifSelected(int selected){


        //-1 because of the extra add_gif view
        gifPlayer.prepareGif(gif_files.get(selected - 1));

        gifPlayer.setNormalImage().into(gif_view);

        gif_sign.setVisibility(View.VISIBLE);
    }



    @Override
    public void onAddGifClickListener() {

        onGifEnded();

        for (LibraryListener listener: getListeners())
            listener.onAddGifClicked();
    }




    @Override
    public void onGifClickedListener(int selected, int last_selected) {




        recyclerView.scrollToPosition(selected);




        //findViewHolderForAdapterPosition return null if the item didn't draw yet
        ((LibraryRecyclerAdapter.LibraryViewHolder)
                Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(last_selected)))
                .getViewMvc().getView_hider().setVisibility(View.VISIBLE);



        gifPlayer.unregisterListener(this);

        //update ui with the new selected gif
        onGifSelected(selected);



    }


    @Override
    public void onGifEnded() {
        gifPlayer.unregisterListener(this);
        gifPlayer.setNormalImage().into(gif_view);
        gif_sign.setVisibility(View.VISIBLE);
    }




}
