package com.hope.igb.catgif.screens.selectmedia;



import android.os.Bundle;
import android.view.LayoutInflater;
import com.hope.igb.catgif.comman.Constants;
import com.hope.igb.catgif.screens.comman.screennavigator.ScreenNavigator;
import com.hope.igb.catgif.screens.comman.base.BaseActivity;

public class SelectActivity extends BaseActivity implements SelectViewMvc.Listener{

    private ScreenNavigator screenNavigator;
    private SelectViewMvc viewMvc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen(this);

        //for coloring the toolbar to suit the current activity layout
        setCurrent_activity("SelectActivity");

        viewMvc = new SelectViewMvc(LayoutInflater.from(this), null);



        setContentView(viewMvc.getRootView());


        screenNavigator = new ScreenNavigator(this);






    }


    /**
     * screen_width represents the screen width for percentage dimensions purpose
     * screen_width couldn't be on the onCreate because it will return 0 because the view won't be created yet
     */

    @Override
    public void onGifFromImagesClickListener() {
        setScreen_width(this.getWindow().getDecorView().getWidth());
        screenNavigator.toGenerateActivity(Constants.FROM_IMAGES);
    }

    @Override
    public void onGifFromVideosClickListener() {
        setScreen_width(this.getWindow().getDecorView().getWidth());
        screenNavigator.toGenerateActivity( Constants.FROM_VIDEO);

    }


    @Override
    protected void onStart() {
        super.onStart();
        viewMvc.registerListener(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        viewMvc.unregisterListener(this);
    }



}