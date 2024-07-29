package com.hope.igb.catgif.screens.comman.base;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.navdrawer.NavDrawerViewMvc;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;

public class BaseActivityViewMvc extends BaseObservableMvc<BaseActivityViewMvc.BaseListener>
implements NavDrawerViewMvc.NavDrawerListener {


    private final FrameLayout activity_container;
    private final DrawerLayout base_layout;
    private final String current_activity;
    private final ConstraintLayout toolbar_container;
    private  NavDrawerViewMvc navDrawerViewMvc = null;




    public interface BaseListener {
         void onHelpClickedListener();
         void onMenuClickedListener();

        void onLibrarySelected();
        void onPlayerSelected();
        void onStoreSelected();
        void onCompressorSelected();
        void onReviewUsSelected();
        void onHelpSelected();

    }




    public BaseActivityViewMvc(LayoutInflater inflater, ViewGroup parent, String current_activity){
         setRootView(inflater.inflate(R.layout.activity_base, parent, false));

         //the current child activity
         this.current_activity = current_activity;



         //the base layout --->DrawerLayout
        base_layout = findViewById(R.id.base_layout);

        //the frame layout that contained each activity
        activity_container = findViewById(R.id.base_layout_container);

        //the constraint layout that contained the app toolbar
        toolbar_container = findViewById(R.id.app_toolbar);




        initToolbar();
        suitBaseLayoutBackgrounds();



    }

    public FrameLayout getActivity_container() {
        return activity_container;
    }





    //==================================app toolbar=========================================
    private void initToolbar(){
        //help opens a dialog with a details about how to use the application
        ImageView help = toolbar_container.findViewById(R.id.toolbar_help);

        //menu opens a navigation drawer with multi options
        ImageView menu = toolbar_container.findViewById(R.id.toolbar_navigation_drawer);


        help.setOnClickListener(v -> {
            for (BaseListener listener: getListeners())
                listener.onHelpClickedListener();
        });


        menu.setOnClickListener(v -> {
            for (BaseListener listener: getListeners())
                listener.onMenuClickedListener();
        });
    }
    //===========================================================================================================





    //=========================set toolbar and layout backgrounds to suit the current activity====================

    private void suitBaseLayoutBackgrounds(){


        //set the toolbar color to suit the current activity layout
        // and set the main wallpaper
        if (current_activity.equals("SelectActivity")){

            toolbar_container.setBackgroundColor(Color.parseColor("#59050609"));
            base_layout.setBackgroundResource(R.drawable.main_wallpaper);

        }else if (current_activity.equals("GenerateActivityVideo")) {

            toolbar_container.setBackgroundColor(Color.parseColor("#2a3650"));
            base_layout.setBackgroundResource(0);

        }else{

            toolbar_container.setBackgroundColor(Color.parseColor("#354562"));
            base_layout.setBackgroundResource(0);
        }
    }

    //===============================================================================================================







    //==========================================nav drawer implementation==============================================



    public void openDrawer(){
        navDrawerViewMvc = new NavDrawerViewMvc(base_layout);
        navDrawerViewMvc.openDrawer();
        navDrawerViewMvc.registerListener(this);
    }

    public void closeDrawer(){
        navDrawerViewMvc.unregisterListener(this);
        navDrawerViewMvc.closeDrawer();
        navDrawerViewMvc = null;

    }


   public boolean isDrawerOpen(){
        return navDrawerViewMvc != null && navDrawerViewMvc.isDrawerOpen();
   }




    @Override
    public void onGallerySelected() {
        for (BaseListener listener: getListeners())
            listener.onLibrarySelected();
    }

    @Override
    public void onPlayerSelected() {
        for (BaseListener listener: getListeners())
            listener.onPlayerSelected();
    }



    @Override
    public void onStoreSelected() {
        for (BaseListener listener: getListeners())
            listener.onStoreSelected();
    }


    @Override
    public void onCompressorSelected() {
        for (BaseListener listener: getListeners())
            listener.onCompressorSelected();
    }

    @Override
    public void onReviewUsSelected() {
        for (BaseListener listener: getListeners())
            listener.onReviewUsSelected();
    }



    @Override
    public void onHelpSelected() {
        for (BaseListener listener: getListeners())
            listener.onHelpSelected();
    }





}
