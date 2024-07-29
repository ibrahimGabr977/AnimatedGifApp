package com.hope.igb.catgif.screens.generate.fromimages;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;


//The image item (holder) viewMvc class
public class ImageItemViewMvc extends BaseObservableMvc<ImageItemViewMvc.Listener> {

    private final ImageView imageView; // the image item
    private final LinearLayout add_image;  // the add new image clickable view
    private final View view_hider;     // put a shadow on all unselected images
    private static int last_selected = 1; //##didn't work as expected because if was non static




    protected interface Listener{
        void onAddImageClickListener();
        void onImageClicked(int selected, int last_selected);
    }




    ImageItemViewMvc(LayoutInflater inflater, ViewGroup parent, int screenWidth){
        setRootView(inflater.inflate(R.layout.image_view_holder, parent, false));


        //set the height and width of view by percentage value to make exactly 4 items per row
        ViewGroup.LayoutParams params = getRootView().getLayoutParams();

        //the view percentage dimensions
        params.width = (int) ((screenWidth * 0.9) / 4) - 10;
        params.height = (int) ((screenWidth * 0.9) / 4) - 10;



        getRootView().setLayoutParams(params);


        imageView = findViewById(R.id.image_holder_image);
        add_image = findViewById(R.id.image_holder_add_item_text_btn);
        view_hider = findViewById(R.id.image_holder_hider_view);
    }





    void  bindViewImages(Uri image, int position){

        //the inserted image equal null in case of first item
        if (position == 0){

            // show only the add image view as a first item.
            view_hider.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            add_image.setVisibility(View.VISIBLE);



            //add image on click listener
            add_image.setOnClickListener(v -> {
                for (Listener listener :  getListeners())
                listener.onAddImageClickListener();});



        }else { //show the image list as the rest items

            //totally remove the add image view
            add_image.setVisibility(View.GONE);
            add_image.setOnClickListener(null);


            //show the image item and set its image uri
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(image);



            //show the shadow above unselected images
            if (position == last_selected ){

                //don't shading the first image on first startup
                view_hider.setVisibility(View.GONE);



            }else //shaded the rest images

                view_hider.setVisibility(View.VISIBLE);





              //##this wasn't working as expected because i set the click listener after the if statement
                getRootView().setOnClickListener(v -> {

                    //handle image clicked twice cased
                    if (view_hider.getVisibility() != View.GONE) {

                        //unshaded image
                        view_hider.setVisibility(View.GONE);

                        for (Listener listener : getListeners())
                            listener.onImageClicked(position, last_selected);


                        last_selected = position;





                    }
                });


            }


        }







    public View getView_hider() {
        return view_hider;
    }


    public static void setLast_selected(int last_selected) {
        ImageItemViewMvc.last_selected = last_selected;
    }
}
