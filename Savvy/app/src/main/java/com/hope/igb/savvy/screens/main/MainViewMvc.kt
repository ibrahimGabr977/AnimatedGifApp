package com.hope.igb.savvy.screens.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.Constants
import com.hope.igb.savvy.common.customtabslayout.TagsFilteringTabLayout
import com.hope.igb.savvy.common.messagehelper.SystemMessageHelper
import com.hope.igb.savvy.common.navdrawer.NavDrawerViewMvc
import com.hope.igb.savvy.common.presistData.SharedData
import com.hope.igb.savvy.common.views.BaseObservableViewMvc
import com.hope.igb.savvy.networking.models.PostModel
import com.hope.igb.savvy.screens.main.adapteritems.MainAdapterItemViewMvc
import com.hope.igb.savvy.common.customonscrollchange.CustomOnRecyclerViewScrolling
import com.hope.igb.savvy.screens.main.trending.TrendingRecyclerAdapter
import com.hope.igb.savvy.util.MediaLoader
import java.util.*


@SuppressLint("NotifyDataSetChanged")
class MainViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val listener: MainAdapterItemViewMvc.ItemListener,
    private val sharedData: SharedData,
    private val systemMessageHelper: SystemMessageHelper,
    private val mediaLoader: MediaLoader
) :
    BaseObservableViewMvc<MainViewMvc.Listener>(),
    NavDrawerViewMvc.NavDrawerListener,
    CustomOnRecyclerViewScrolling.Listener,
    TagsFilteringTabLayout.Listener {


    interface Listener{
        fun onTryAgainClicked()
        fun onNavItemSelected(itemId: Int)
        fun onRefreshTriggered(): Boolean
        fun onFilterChecked()
    }

    private val customToolbar: ConstraintLayout
    private val tagsLayout: TagsFilteringTabLayout


    private val mainMenu: ImageView
    private val autoScroll: ImageView
    private val reload: ImageView




    private var navDrawerViewMvc: NavDrawerViewMvc? = null
    private var noInternetView: ConstraintLayout? = null
    private val recyclerView: RecyclerView
    private val smoothScroller: SmoothScroller
    private lateinit var postsAdapter: MainRecyclerAdapter
    private var trendingAdapter: TrendingRecyclerAdapter? = null
    private val recyclerViewContainer: SwipeRefreshLayout



    private var progressBar: ProgressBar? = null


    var currentFilter = Constants.CATEGORY_ALL


    init {
        setRootView(inflater.inflate(R.layout.fragment_main, parent, false))

        customToolbar = findViewById(R.id.custom_tool_bar)
        tagsLayout = TagsFilteringTabLayout(getRootView())
        tagsLayout.registerListener(this)
        showProgressBar()


        mainMenu = findViewById(R.id.main_menu)
        autoScroll = findViewById(R.id.posts_auto_scroll)
        reload = findViewById(R.id.posts_reload)


        recyclerView = findViewById(R.id.mainRecyclerView)
        recyclerView.addOnScrollListener(CustomOnRecyclerViewScrolling(this))

        recyclerViewContainer = findViewById(R.id.recyclerViewContainer)
        setOnSwipeToRefreshRecyclerView()


        smoothScroller = object : LinearSmoothScroller(getContext()) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }





        initClickableViews()
        initListView()


    }



    override fun unregisterListener(listener: Listener) {
        super.unregisterListener(listener)
        closeDrawer()
    }


    override fun onRecyclerViewScrolledUp() {
        showTopChildViews()
    }

    private fun showTopChildViews() {
        customToolbar.visibility = View.VISIBLE
        tagsLayout.hide()
        tagsLayout.registerListener(this)
    }

    override fun onRecyclerViewScrolledDown() {
        hideTopChildViews()
    }

    private fun hideTopChildViews() {
        customToolbar.visibility = View.GONE
        tagsLayout.show()
        tagsLayout.registerListener(this)
    }

    override fun onTagSelected(tag_id: Int) {
        this.currentFilter = if (tag_id == R.id.tab_all)
            Constants.CATEGORY_ALL
        else
            Constants.CATEGORY_TRENDING

        for (listener in getListeners())
            listener.onFilterChecked()
    }

    
    

    private fun initClickableViews() {
        mainMenu.setOnClickListener { openDrawer() }
        autoScroll.setOnClickListener { onAutoScrollClicked() }
        reload.setOnClickListener { recyclerViewContainer.isRefreshing = true  }

    }

    private fun onAutoScrollClicked() {
        if (postsAdapter.autoScrolled){
            autoScroll.setImageResource(R.drawable.b_main_auto_scrolling_offd)
            setAutoScrolled(false)
        }else{
            autoScroll.setImageResource(R.drawable.b_main_auto_scrolling_ond)
            setAutoScrolled(true)
        }
    }



 

    private fun openDrawer() {
        val mainLayout: DrawerLayout = findViewById(R.id.main_drawer_layout)
        navDrawerViewMvc = NavDrawerViewMvc(mainLayout)
        navDrawerViewMvc?.openDrawer()
        navDrawerViewMvc?.registerListener(this)
    }

     fun closeDrawer() {
        if (navDrawerViewMvc != null && navDrawerViewMvc!!.isDrawerOpen()) {
            navDrawerViewMvc?.unregisterListener(this)
            navDrawerViewMvc?.closeDrawer()
            navDrawerViewMvc = null
        }
    }


    override fun onNavItemSelected(itemId: Int) {
        for (listener in getListeners())
            listener.onNavItemSelected(itemId)

    }









    private fun setAutoScrolled(autoScroll: Boolean) {
        postsAdapter.autoScrolled = autoScroll
        trendingAdapter?.autoScrolled = autoScroll
    }




    private fun initListView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(15)
        recyclerView.itemAnimator = null
        recyclerView.layoutManager = LinearLayoutManager(getContext())
    }

    fun getCurrentAdapterPosition(): Int{
        return (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    }

    fun bindNoInternetConnectionView() {

        recyclerViewContainer.visibility = View.INVISIBLE
        recyclerView.visibility = View.GONE

        if (noInternetView == null)
            noInternetView = findViewById(R.id.no_internet_connection)


        noInternetView!!.visibility = View.VISIBLE
        noInternetView!!.findViewById<TextView>(R.id.try_again).setOnClickListener {
            for (listener in getListeners())
                listener.onTryAgainClicked()
        }

        hideProgressBar()
    }

    fun bindAllPosts(posts: ArrayList<PostModel>) {

        noInternetView?.visibility = View.GONE
        noInternetView = null


        recyclerViewContainer.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE


        postsAdapter = MainRecyclerAdapter(posts, listener, mediaLoader)
        recyclerView.adapter = postsAdapter
        postsAdapter.notifyDataSetChanged()
        hideProgressBar()
    }


    fun bindPostsByFilter(posts: ArrayList<PostModel>) {

        recyclerViewContainer.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE


        if (currentFilter == Constants.CATEGORY_TRENDING)
            bindTrendingAdapter(posts)
        else
            bindMainAdapter(posts)

        hideProgressBar()
    }

    private fun bindMainAdapter(posts: ArrayList<PostModel>) {
        postsAdapter =
            MainRecyclerAdapter(posts, listener, mediaLoader)
        recyclerView.swapAdapter(postsAdapter, true)
        postsAdapter.notifyDataSetChanged()
    }

    private fun bindTrendingAdapter(posts: ArrayList<PostModel>) {
        trendingAdapter = TrendingRecyclerAdapter(posts, listener, mediaLoader)
        recyclerView.swapAdapter(trendingAdapter, true)
        trendingAdapter?.notifyDataSetChanged()
    }


    private fun setOnSwipeToRefreshRecyclerView() {
        recyclerViewContainer.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getContext(), R.color.darker_color))
        recyclerViewContainer.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.white_color))

        recyclerViewContainer.setOnRefreshListener {
            if (dataListIsNotEmpty()) {
                for (listener in getListeners())
                    recyclerViewContainer.isRefreshing = listener.onRefreshTriggered()

                currentFilter = Constants.CATEGORY_ALL
                tagsLayout.check(R.id.tab_all)
            }

        }

    }



    private fun dataListIsNotEmpty(): Boolean {
        return recyclerView.childCount != 0
    }


    fun onProgressChanged(position: Int, progress: Int) {
        (Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position))
                as MainRecyclerAdapter.MainViewHolder).itemViewMvc.onProgressChanged(progress)
    }

    fun onDownloaded(position: Int) {
        (Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position))
                as MainRecyclerAdapter.MainViewHolder).itemViewMvc.onDownloaded()

    }


    fun scrollToPosition(position: Int) {
        smoothScroller.targetPosition = position

        recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
    }


    private fun showProgressBar() {
        if (progressBar == null)
            progressBar = findViewById(R.id.progressBar)

        progressBar?.visibility = View.VISIBLE


    }


    private fun hideProgressBar() {
        progressBar?.visibility = View.GONE
        progressBar = null

    }

    fun isDrawerOpening(): Boolean{
        return navDrawerViewMvc != null && navDrawerViewMvc?.isDrawerOpen()!!
    }




}