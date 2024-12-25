package com.hope.igb.savvy.screens.favorites

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hope.igb.aiwallpapers.screen.main.util.MyItemsMarginsDecoration
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.views.BaseObservableViewMvc
import com.hope.igb.savvy.screens.favorites.displaypost.DisplayPostDialogViewMvc
import com.hope.igb.savvy.util.MediaLoader
import java.util.*
import kotlin.collections.ArrayList

class FavoritesViewMvc(
    private val inflater: LayoutInflater,
    parent: ViewGroup?,
    private val mediaLoader: MediaLoader
) :
    BaseObservableViewMvc<FavoritesViewMvc.FavoritesListener>(){

    interface FavoritesListener {
        fun onBackClicked()
    }


    private var noFavoritesView: ConstraintLayout? = null
    private val recyclerView: RecyclerView
    private var displayPostDialog: DisplayPostDialogViewMvc? = null

    init {
        setRootView(inflater.inflate(R.layout.fragment_favorites, parent, false))

        recyclerView = findViewById(R.id.favoritesRecyclerView)

        initView()
    }


    private fun initView() {
        findViewById<ImageView>(R.id.favorite_back).setOnClickListener {
            for (listener in getListeners()) listener.onBackClicked()
        }


        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(10)
        recyclerView.itemAnimator = null
        recyclerView.layoutManager = GridLayoutManager(getContext(), 2)


        if (noDecorationAddedToRecyclerView())
            recyclerView.addItemDecoration(MyItemsMarginsDecoration(19, 2, false))

    }

    private fun noDecorationAddedToRecyclerView(): Boolean {
        return recyclerView.itemDecorationCount == 0
    }



    fun bindEmptyFavoriteListView() {
        recyclerView.visibility = View.INVISIBLE


        if (noFavoritesView == null)
            noFavoritesView = findViewById(R.id.no_favorites)

        noFavoritesView?.visibility = View.VISIBLE


    }


    @SuppressLint("NotifyDataSetChanged")
    fun bindFavoriteList(modelsToString: ArrayList<String>, listener: FavoriteAdapterItemViewMvc.ItemListener) {
        noFavoritesView?.visibility = View.GONE
        noFavoritesView = null

        recyclerView.visibility = View.VISIBLE

        val favoritesAdapter = FavoriteRecyclerAdapter(modelsToString, listener, mediaLoader)
        recyclerView.adapter = favoritesAdapter
        favoritesAdapter.notifyDataSetChanged()

    }

    fun displayPostDialog(listener: DisplayPostDialogViewMvc.Listener, postModelInStr: String) {
        displayPostDialog = DisplayPostDialogViewMvc(inflater, postModelInStr)
        displayPostDialog?.registerListener(listener)
        displayPostDialog?.show()
    }


    fun removeDisplayDialogViewMvc(listener: DisplayPostDialogViewMvc.Listener){
        displayPostDialog?.unregisterListener(listener)
        displayPostDialog?.dismiss()
        displayPostDialog = null
    }


    fun displayDialogOnProgressChanged(progress: Int){
            displayPostDialog?.onProgressChanged(progress)
    }

    fun displayDialogOnPostDownloaded(){
        displayPostDialog?.onDownloaded()
    }

    fun isDisplayDialogOpening(): Boolean {
        return displayPostDialog != null
    }

}