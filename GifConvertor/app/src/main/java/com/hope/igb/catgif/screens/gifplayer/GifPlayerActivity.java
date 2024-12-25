package com.hope.igb.catgif.screens.gifplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.base.BaseFragment;
import com.hope.igb.catgif.screens.gifplayer.offline.GifPlayerFragment;
import com.hope.igb.catgif.screens.gifplayer.online.OnlineGifFPlayerFragment;
import com.hope.igb.catgif.util.FragmentSwitcher;

import static com.hope.igb.catgif.screens.comman.base.BaseActivity.setFullScreen;

public class GifPlayerActivity extends AppCompatActivity {

    private BaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen(this);
        setContentView(R.layout.activity_gif_player);


        String gif_id = getIntent().getStringExtra("GIF_ID");


        if (gif_id.equals("OFFLINE"))
            fragment = new GifPlayerFragment();

        else
            fragment = OnlineGifFPlayerFragment.newInstance(gif_id);


        FragmentSwitcher.switchToFragment(this, R.id.playerFrameLayout, fragment);

    }


    @Override
    public void onBackPressed() {
        if (!fragment.isIn_progress())
        super.onBackPressed();

    }
}