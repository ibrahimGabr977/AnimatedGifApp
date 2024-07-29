package com.hope.igb.catgif.screens.giflibrary;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.gifcontroller.GifPlayer;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;


public class LibraryItemViewMvc extends BaseObservableMvc<LibraryItemViewMvc.Listener> {

    private final ImageView imageView, gif_sign; // the image item and the gif sign above image
    private final LinearLayout add_gif;  // the add new image clickable view
    private final View view_hider;     // put a shadow on all unselected images
    private final GifPlayer gifPlayer;
    private static int last_selected = 1;


    protected interface Listener {
        void onAddGifClickListener();
        void onGifClicked(int selected, int last_selected);

    }

    LibraryItemViewMvc(LayoutInflater inflater, ViewGroup parent, GifPlayer gifPlayer, int screenWidth) {
        this.gifPlayer = gifPlayer;
        setRootView(inflater.inflate(R.layout.gif_view_holder, parent, false));


        //set the height and width of view by percentage value to make exactly 4 items per row
        ViewGroup.LayoutParams params = getRootView().getLayoutParams();

        //the view percentage dimensions
//        params.width = (int) ((screenWidth * 0.9) / 4) - 10;
          params.height = (int) (((screenWidth * 0.9) / 4.0) - 10);





        getRootView().setLayoutParams(params);


        imageView = findViewById(R.id.gif_holder_image);
        gif_sign = findViewById(R.id.gif_holder_gif_sign_image);
        add_gif = findViewById(R.id.gif_holder_add_item_text_btn);
        view_hider = findViewById(R.id.gif_holder_hider_view);

    }


    void bindGIFs(Uri gif, int position) {

        //the inserted image equal null in case of first item
        if (position == 0) {

            // show only the add image view as a first item.
            add_gif.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            gif_sign.setVisibility(View.GONE);
            view_hider.setVisibility(View.GONE);

            //add image on click listener
            add_gif.setOnClickListener(v -> {
                for (Listener listener : getListeners())
                    listener.onAddGifClickListener();
            });


        } else { //show the image list as the rest items

            //totally remove the add gif view
            add_gif.setVisibility(View.GONE);
            add_gif.setOnClickListener(null);


            //shows the image item, gif sign above it and sets its image uri
            gif_sign.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);

            //set normal image from a gif file inside image view
            gifPlayer.prepareGif(gif);
            gifPlayer.setNormalImage().into(imageView);



            //show the shadow above unselected GIFs
            if (position == last_selected) {

                //don't shading the first gif on first startup
                view_hider.setVisibility(View.GONE);



            } else
                view_hider.setVisibility(View.VISIBLE);






            //add image on click listener
            getRootView().setOnClickListener(v -> {

                //handle gif clicked twice cased
                if (position != last_selected) {


                    //unshaded gif
                    view_hider.setVisibility(View.GONE);


                    for (Listener listener : getListeners())
                        listener.onGifClicked(position, last_selected);

                    last_selected = position;
                }
            });
        }


    }


    public View getView_hider() {
        return view_hider;
    }

//    public static void setLast_selected(int last_selected) {
//        LibraryItemViewMvc.last_selected = last_selected;
//    }
}
