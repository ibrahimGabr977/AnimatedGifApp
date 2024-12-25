package com.hope.igb.savvy.screens.favorites

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.views.BaseObservableViewMvc
import com.hope.igb.savvy.networking.models.PostModelHelper
import com.hope.igb.savvy.util.MediaLoader

class FavoriteAdapterItemViewMvc (inflater: LayoutInflater,
                                  parent: ViewGroup,
                                  private val mediaLoader: MediaLoader)
    : BaseObservableViewMvc<FavoriteAdapterItemViewMvc.ItemListener>() {



    interface ItemListener {
        fun onPostClicked(position: Int)

    }


    private val  imageView:ImageView


    init {
        setRootView(inflater.inflate(R.layout.view_holder_favorites, parent, false))

        val params = getRootView().layoutParams as ViewGroup.LayoutParams


        val itemWidth = ((getScreenWidth() * 0.925 / 2.0) - 19.0).toInt()



        params.height = (itemWidth * 3)/2



        getRootView().layoutParams = params






        imageView = findViewById(R.id.favorite_post_view)
        imageView.clipToOutline = true






    }

    private fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }


    fun bindAdapterItem(postInString: String, position: Int){


         mediaLoader.loadGif(PostModelHelper.getDownloadUrl(postInString)).into(imageView)

        getRootView().setOnClickListener {
            for (listener in getListeners()) listener.onPostClicked(position)
        }


     }








}