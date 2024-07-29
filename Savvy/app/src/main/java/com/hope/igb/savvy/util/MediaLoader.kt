package com.hope.igb.savvy.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.AudioRecord.MetricsConstants.SOURCE
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.hope.igb.savvy.R

class MediaLoader(private val context: Context) {


    private fun customProgress(): CircularProgressDrawable {

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 6f
        circularProgressDrawable.centerRadius = 35f
        circularProgressDrawable.setColorSchemeColors(
            ContextCompat.getColor(
                context,
                R.color.blue_color
            )
        )
        circularProgressDrawable.start()

        return circularProgressDrawable
    }


    fun loadGif(gif: String): RequestBuilder<GifDrawable> {
        return Glide.with(context)
            .asGif()
            .load(gif)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(customProgress())
            .error(R.drawable.z_post_load_failedd)


    }

    fun loadImage(image: String): RequestBuilder<Drawable> {
        return Glide.with(context)
            .load(image)
            .placeholder(customProgress())
            .error(R.drawable.z_error_image)
    }

}