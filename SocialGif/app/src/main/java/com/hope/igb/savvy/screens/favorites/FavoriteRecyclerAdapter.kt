package com.hope.igb.savvy.screens.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hope.igb.savvy.util.MediaLoader

class FavoriteRecyclerAdapter(

    private val postModelsInString: ArrayList<String>,
    private val listener: FavoriteAdapterItemViewMvc.ItemListener,
    private val mediaLoader: MediaLoader
) :
    RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder>() {




    class FavoriteViewHolder (val itemViewMvc: FavoriteAdapterItemViewMvc) :
        ViewHolder(itemViewMvc.getRootView())




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemViewMvc = FavoriteAdapterItemViewMvc(
            LayoutInflater.from(parent.context),
            parent,
            mediaLoader
        )
        return FavoriteViewHolder(itemViewMvc)


    }


    override fun onViewDetachedFromWindow(holder: FavoriteViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.itemViewMvc.unregisterListener(listener)


    }


    override fun onViewAttachedToWindow(holder: FavoriteViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.itemViewMvc.registerListener(listener)

    }


    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

        holder.itemViewMvc.bindAdapterItem(postModelsInString[position], position)

    }


    override fun getItemCount(): Int {
        return postModelsInString.size
    }





}