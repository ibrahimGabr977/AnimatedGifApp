package com.hope.igb.savvy.screens.main.trending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hope.igb.savvy.common.Constants
import com.hope.igb.savvy.common.messagehelper.SystemMessageHelper
import com.hope.igb.savvy.networking.models.PostModel
import com.hope.igb.savvy.common.presistData.SharedData
import com.hope.igb.savvy.screens.main.adapteritems.MainAdapterItemViewMvc
import com.hope.igb.savvy.util.DateFormatBuilder
import com.hope.igb.savvy.util.MediaLoader

class TrendingRecyclerAdapter(

    private val postsModels: ArrayList<PostModel>,
    private val listener: MainAdapterItemViewMvc.ItemListener,
    private val mediaLoader: MediaLoader
) :
    RecyclerView.Adapter<TrendingRecyclerAdapter.TrendingViewHolder>() {


    class TrendingViewHolder(val itemViewMvc: MainAdapterItemViewMvc) :
        ViewHolder(itemViewMvc.getRootView())


    var autoScrolled: Boolean = false
    private val dateFormatBuilder = DateFormatBuilder()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        return TrendingViewHolder(
            MainAdapterItemViewMvc(
                LayoutInflater.from(parent.context),
                parent,
                dateFormatBuilder,
                Constants.CATEGORY_ALL,
                mediaLoader,
                false,
                false
            )
        )


    }


    override fun onViewDetachedFromWindow(holder: TrendingViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.itemViewMvc.unregisterListener(listener)


    }


    override fun onViewAttachedToWindow(holder: TrendingViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.itemViewMvc.registerListener(listener)


    }


    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {

        holder.itemViewMvc.bindAdapterItem(postsModels[position], position, autoScrolled)


    }


    override fun getItemCount(): Int {
        return postsModels.size
    }




}