package com.hope.igb.catgif.util;

import android.app.Activity;

import com.hope.igb.catgif.comman.BaseObservable;


//async task deprecated so this class doing something like it
public abstract class MyAsyncTask<TASK_PROGRESS_LISTENER> extends BaseObservable<TASK_PROGRESS_LISTENER>{


    private final Activity activity;
    public MyAsyncTask(Activity activity) {
        this.activity = activity;
    }

    private void startBackground() {
        new Thread(() -> {

            doInBackground();
            activity.runOnUiThread(this::onPostExecute);
        }).start();
    }

    public void execute(){
        startBackground();
    }

    public abstract void doInBackground();
    public abstract void onPostExecute();


}
