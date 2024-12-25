package com.hope.igb.catgif.util;

import android.content.Context;

import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

public class ReviewUsHelper {

    private final Context context;

    public ReviewUsHelper(Context context) {
        this.context = context;
        ReviewManager manager = ReviewManagerFactory.create(context);

    }
}
