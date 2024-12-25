package com.hope.igb.catgif.screens.generate;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.comman.Constants;
import com.hope.igb.catgif.screens.comman.base.BaseActivity;
import com.hope.igb.catgif.screens.comman.base.BaseFragment;
import com.hope.igb.catgif.screens.generate.fromimages.GenerateFromImagesFragment;
import com.hope.igb.catgif.screens.generate.fromvideo.GenerateFromVideoFragment;
import com.hope.igb.catgif.util.FragmentSwitcher;


public class GenerateActivity extends BaseActivity {

    private BaseFragment desired_fragment;



    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen(this);





        String gif_source = getIntent().getStringExtra(Constants.GIF_SOURCE);



        if (gif_source.equals(Constants.FROM_VIDEO)){
            desired_fragment = new GenerateFromVideoFragment();

            //for coloring the toolbar to suit the current activity layout
            setCurrent_activity("GenerateActivityVideo");

        }else {
            desired_fragment = GenerateFromImagesFragment.newInstance(getScreen_width());
            //for coloring the toolbar to suit the current activity layout
            setCurrent_activity("GenerateActivityImages");

        }

        FragmentSwitcher.switchToFragment(this, R.id.generateFrameLayout, desired_fragment);


        //use layout inflater instead of activity layout id to ensure that the called method is setContentView(View view)
        // to call it from base activity for toolbar and navigation drawer.
        setContentView(LayoutInflater.from(this).inflate(R.layout.activity_generate, null, false));

    }


    @Override
    public void onBackPressed() {
        if (!desired_fragment.isIn_progress()){
            super.onBackPressed();
        }

    }




}