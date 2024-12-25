package com.hope.igb.catgif.screens.comman.base;



import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hope.igb.catgif.screens.comman.dialoghelper.InfoDialogHelper;
import com.hope.igb.catgif.screens.comman.screennavigator.ScreenNavigator;
import com.hope.igb.catgif.screens.comman.toasthelper.ToastHelper;
import com.hope.igb.catgif.util.CheckNetworkState;

public class BaseActivity  extends AppCompatActivity implements
        BaseActivityViewMvc.BaseListener ,
        InfoDialogHelper.ReviewUsClickListener{

    //screen_width represents the screen width for percentage dimensions purpose
    private static int screen_width;

    private BaseActivityViewMvc baseViewMvc;
    //current child activity
    private String current_activity;
    private InfoDialogHelper infoDialogHelper;
    private ScreenNavigator screenNavigator;
    private ToastHelper toastHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    public void setCurrent_activity(String current_activity) {
        this.current_activity = current_activity;
    }

    @Override
    public void setContentView(View view) {
        baseViewMvc = new BaseActivityViewMvc(getLayoutInflater(), null, current_activity);

        super.setContentView(baseViewMvc.getRootView());



        //add current child activity to the base frame layout
        baseViewMvc.getActivity_container().addView(view);


        screenNavigator = new ScreenNavigator(this);
        toastHelper = new ToastHelper(this);



    }



    @SuppressWarnings("deprecation")
    //set activity full screen ---> remove title bar
    public static void setFullScreen(Activity activity){
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = activity.getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }







    //activity screen width for percentage views purpose
    public static int getScreen_width() {
        return screen_width;
    }

    protected static void setScreen_width(int screen_width) {
        BaseActivity.screen_width = screen_width;
    }


    @Override
    protected void onStart() {
        super.onStart();
        baseViewMvc.registerListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

            if (baseViewMvc.isDrawerOpen())
            baseViewMvc.closeDrawer();

            if (baseViewMvc != null) {
                baseViewMvc.unregisterListener(this);
            }



    }



    @Override
    public void onLibrarySelected() {
        if (screen_width == 0)
            screen_width = this.getWindow().getDecorView().getWidth();


        if (!current_activity.equals("LibraryActivity")){
            screenNavigator.toLibraryActivity(null);
        }

    }

    @Override
    public void onPlayerSelected() {
        //passing OFFLINE to gif_id parameter to opening offline player
        screenNavigator.toPlayerActivity("OFFLINE");
    }



    @Override
    public void onStoreSelected() {
        if (CheckNetworkState.isInternetAvailable(this)){
            setScreen_width(this.getWindow().getDecorView().getWidth());
            screenNavigator.toStoreActivity();
        }else{
            toastHelper.noInternetConnection();
        }


    }

    @Override
    public void onCompressorSelected() {
        toastHelper.comingSoon("Gif compressor");
    }




    @Override
    public void onReviewUsSelected() {
        //TODO: after the app released on google play
    }

    //all application details from the navigation drawer
    @Override
    public void onHelpSelected() {
        infoDialogHelper = new InfoDialogHelper(this);
        infoDialogHelper.registerListener(this);
        infoDialogHelper.appHelpDialog().show();
    }





    //=======================================Toolbar implementation==========================================


    //help from the toolbar related to the current activity
    @Override
    public void onHelpClickedListener() {

        infoDialogHelper = new InfoDialogHelper(this);

        if (current_activity.equals("SelectActivity"))
            infoDialogHelper.selectActivityDialog().show();
        else if (current_activity.equals("LibraryActivity")){
            infoDialogHelper.libraryHelpDialog().show();
            infoDialogHelper.registerListener(this);
        }else
            infoDialogHelper.generateActivityDialog(current_activity).show();
    }


    //open navigation drawer menu
    @Override
    public void onMenuClickedListener() {
        baseViewMvc.openDrawer();

    }



    //=========================================InfoDialogHelper implementation====================================

    @Override
    public void onReviewUsClicked() {
        onReviewUsSelected();
        infoDialogHelper.registerListener(this);
        //TODO: after the app released on google play
    }
}
