package com.hope.igb.catgif.screens.comman.navdrawer;


import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;

public class NavDrawerViewMvc extends BaseObservableMvc<NavDrawerViewMvc.NavDrawerListener>
        implements
        NavigationView.OnNavigationItemSelectedListener{


    private final DrawerLayout drawerLayout;



    public NavDrawerViewMvc(DrawerLayout base_layout) {
        this.drawerLayout = base_layout;

        setRootView(base_layout);




        NavigationView mNavigationView = findViewById(R.id.nav_view);


        mNavigationView.setNavigationItemSelectedListener(this);


    }






    public interface NavDrawerListener {
        void onGallerySelected();
        void onPlayerSelected();
        void onStoreSelected();
        void onCompressorSelected();
        void onReviewUsSelected();
        void onHelpSelected();
    }







    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }



    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }


    public void closeDrawer() {
        drawerLayout.closeDrawers();

    }







    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();



        for (NavDrawerListener listener: getListeners())


            if (item.getItemId() == R.id.nav_item_library) { //if the gif gallery choice selected from the nav drawer

                listener.onGallerySelected();

            }else if (item.getItemId() == R.id.nav_item_player){ //if the gif player choice selected from the nav drawer
                listener.onPlayerSelected();

            }else if (item.getItemId() == R.id.nav_item_store){ //if the more apps choice selected from the nav drawer
                listener.onStoreSelected();

            }else if (item.getItemId() == R.id.nav_item_compressor){ //if the review us choice selected from the nav drawer
                listener.onCompressorSelected();


            }else if (item.getItemId() == R.id.nav_item_review_us){ //if the review us choice selected from the nav drawer
                listener.onReviewUsSelected();
            }else { //if the help choice selected from the nav drawer
                listener.onHelpSelected();
            }

        return false;
    }

}
