package com.hope.igb.savvy.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hope.igb.savvy.common.Constants
import com.hope.igb.savvy.common.messagehelper.SystemMessageHelper
import com.hope.igb.savvy.networking.models.PostModel
import com.hope.igb.savvy.common.presistData.SharedData
import com.hope.igb.savvy.screens.main.adapteritems.AdItemViewMvc
import com.hope.igb.savvy.screens.main.adapteritems.MainAdapterItemViewMvc
import com.hope.igb.savvy.util.DateFormatBuilder
import com.hope.igb.savvy.util.MediaLoader

class MainRecyclerAdapter(

    private val postsModels: ArrayList<PostModel>,
    private val listener: MainAdapterItemViewMvc.ItemListener,
    private val mediaLoader: MediaLoader
) :
    RecyclerView.Adapter<ViewHolder>() {


    inner class MainViewHolder(val itemViewMvc: MainAdapterItemViewMvc) :
        ViewHolder(itemViewMvc.getRootView())

    inner class AdBannerHolder(val itemViewMvc: AdItemViewMvc) :
        ViewHolder(itemViewMvc.getRootView())


    object ViewType {
        const val POST = 0
        const val AD_BANNER = 1


    }

    object AdsIndices {
        const val AD1 = 7
        const val AD2 = 13
        const val AD3 = 25
        const val AD4 = 35
        const val AD5 = 45
    }

    var autoScrolled: Boolean = false
    private val dateFormatBuilder = DateFormatBuilder()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ViewType.POST)
            MainViewHolder(
                MainAdapterItemViewMvc(
                    LayoutInflater.from(parent.context),
                    parent,
                    dateFormatBuilder,
                    Constants.CATEGORY_ALL,
                    mediaLoader,
                    true,
                    true
                )
            )
        else
            AdBannerHolder(AdItemViewMvc(LayoutInflater.from(parent.context), parent))


    }


    override fun getItemViewType(position: Int): Int {
        return if (position == AdsIndices.AD1 || position == AdsIndices.AD2 || position == AdsIndices.AD3 ||
            position == AdsIndices.AD4 || position == AdsIndices.AD5
        )

            ViewType.AD_BANNER else ViewType.POST
    }


    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        if (holder is MainViewHolder)
            holder.itemViewMvc.unregisterListener(listener)
        else
            (holder as AdBannerHolder).itemViewMvc.unRegisterAd()


    }


    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (holder is MainViewHolder)
            holder.itemViewMvc.registerListener(listener)
        else
            (holder as AdBannerHolder).itemViewMvc.registerAd()

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val adUnitIndex = when (position) {
            in 0..AdsIndices.AD1 -> 0
            in AdsIndices.AD1..AdsIndices.AD2 -> 1
            in AdsIndices.AD2..AdsIndices.AD3 -> 2
            in AdsIndices.AD3..AdsIndices.AD4 -> 3
            in AdsIndices.AD4..AdsIndices.AD5 -> 4
            else -> 5
        }



        if (holder is MainViewHolder)
            holder.itemViewMvc.bindAdapterItem(
                postsModels[position - adUnitIndex],
                position - adUnitIndex,
                autoScrolled
            )
        else
            (holder as AdBannerHolder).itemViewMvc.bindAdViewWithIndex(adUnitIndex)

    }


    override fun getItemCount(): Int {
        return postsModels.size + 5
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun reSortDataBy(filter: String) {
//
//        wallpaperModels.sortWith { data1, data2 ->
//            if (filter == "recent")
//                if (data1.id.toLong() > data2.id.toLong()) 1
//                else if (data1.id.toLong() < data2.id.toLong()) -1 else 0
//            else
//                if (data1.usageTimes > data2.usageTimes) -1
//                else if (data1.usageTimes < data2.usageTimes) 1 else 0
//
//        }
//
//        notifyDataSetChanged()
//    }


}