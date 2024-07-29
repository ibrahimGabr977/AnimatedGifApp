package com.hope.igb.catgif.screens.comman.dialoghelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;

public class ProgressPopupWindowViewMvc extends BaseObservableMvc<ProgressPopupWindowViewMvc.PopupWindowListener> {


    private PopupWindow progress_window;
    private NumberProgressBar progressBar;
    private TextView progress_description;

    public interface PopupWindowListener {
        void onProgressCanceled();
    }

    @SuppressLint("InflateParams")
    public ProgressPopupWindowViewMvc(LayoutInflater inflater){
        setRootView(inflater.inflate(R.layout.progress_window_layout, null, false));

    }



    //for setting up create post popup window
    @SuppressLint("ClickableViewAccessibility")
    public void showProgressWindow(){


        //Initialize progress layout child views

        //text view for describe the current progress e.g (preparing..., generating and so on)
        progress_description = findViewById(R.id.progress_description_text);
        //the progress bar
        progressBar = findViewById(R.id.number_progress_bar);
        //cancel the progress and remove the popup window
        TextView cancel = findViewById(R.id.progress_cancel_textBtn);
        //disable any click during the process except cancel
        View disable_clicking = findViewById(R.id.progress_disable_clicking_view);


        progress_window = new PopupWindow(getRootView(), ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);


        //show and hide from bottom animation
        progress_window.setAnimationStyle(R.style.Animation);

        //window shape
        progress_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress_window.update(0,0,ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);









        //close create post window
        cancel.setOnClickListener(v -> {

            //notify activity that progress canceled
            for (PopupWindowListener listener : getListeners())
                listener.onProgressCanceled();

            progress_window.dismiss();

        });






        //return true to disable any touch during the process except cancel the process
        disable_clicking.setOnTouchListener((v, event) -> true);






        //show popup window
        progress_window.showAtLocation(getRootView(), Gravity.CENTER, 0, 0);



    }


    public void dismissWindow(){
        if (progress_window != null)
        progress_window.dismiss();
    }


    public void setOnProgressChanged(int progress){
        //get progress from activity
        ((Activity) getContext()).runOnUiThread(() -> progressBar.setProgress(progress));

    }


    public void setOnProcessChanged(String process){
        //get current progress description from activity e.g (preparing..., generating and so on)
        ((Activity) getContext()).runOnUiThread(() -> progress_description.setText(process));
    }



}
