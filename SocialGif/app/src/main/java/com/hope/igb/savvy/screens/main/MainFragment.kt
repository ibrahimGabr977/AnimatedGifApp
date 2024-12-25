package com.hope.igb.savvy.screens.main


import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.google.android.material.snackbar.Snackbar
import com.hope.igb.savvy.R
import com.hope.igb.savvy.adhelper.AdInterstitialHelper
import com.hope.igb.savvy.screens.BaseFragment
import com.hope.igb.savvy.common.Constants
import com.hope.igb.savvy.common.setappcontentwindow.SetAppContentDialogViewMvc
import com.hope.igb.savvy.networking.models.PostModel
import com.hope.igb.savvy.networking.firebasedatabase.EditGifPostUseCase
import com.hope.igb.savvy.networking.firebasedatabase.FetchGifPostsUseCase
import com.hope.igb.savvy.networking.firebasestorage.PostDownloadUseCase
import com.hope.igb.savvy.util.CheckNetworkState
import com.hope.igb.savvy.networking.firebasedatabase.FetchTrendingPostsUseCase
import com.hope.igb.savvy.networking.firebasedatabase.ReportPostUseCase
import com.hope.igb.savvy.networking.models.PostModelHelper
import com.hope.igb.savvy.screens.main.adapteritems.MainAdapterItemViewMvc
import com.hope.igb.savvy.screens.main.reportpostswindow.ReportPostDialogViewMvc


class MainFragment : BaseFragment(), MainViewMvc.Listener,
    FetchGifPostsUseCase.Listener, MainAdapterItemViewMvc.ItemListener,
    FetchTrendingPostsUseCase.Listener, ReportPostDialogViewMvc.Listener,
    SetAppContentDialogViewMvc.Listener {

    private lateinit var viewMvc: MainViewMvc
    private var reportViewMvc: ReportPostDialogViewMvc? = null
    private var fetchUseCase: FetchGifPostsUseCase? = null
    private var fetchTrendingUseCase: FetchTrendingPostsUseCase? = null


    private var posts: ArrayList<PostModel>? = null
    private var trendingPosts: ArrayList<PostModel>? = null

    private lateinit var adInterstitial: AdInterstitialHelper
    private lateinit var  editGifPostUseCase :EditGifPostUseCase



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        viewMvc = MainViewMvc(inflater, container, this, sharedData(), systemMessageHelper(), mediaLoader())
//
        addCustomOnBackPressedCallback()

        editGifPostUseCase = EditGifPostUseCase(databaseReference())

        adInterstitial = AdInterstitialHelper(requireActivity())



        return viewMvc.getRootView()
    }



    private fun addCustomOnBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewMvc.isDrawerOpening())
                        viewMvc.closeDrawer()
                    else if (reportViewMvc != null)
                        releaseReportWindow()
                    else if (viewMvc.getCurrentAdapterPosition() != 0)
                        viewMvc.scrollToPosition(0)
                    else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()

        bindMainView()
        viewMvc.registerListener(this)

        adInterstitial.initAd(getString(R.string.interstitial_ad_id1))


    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
        releaseReportWindow()
    }



    override fun onRefreshTriggered(): Boolean{
        posts?.shuffle()
        viewMvc.bindPostsByFilter(posts!!)
        return false
    }


    override fun onTryAgainClicked() {
        bindMainView()

    }


    override fun onFilterChecked() {
        bindMainView()
    }



    private fun bindMainView() {
        if (!CheckNetworkState.isInternetAvailable(requireContext())) {
            viewMvc.bindNoInternetConnectionView()
        }else if (posts == null) {
            fetchAllPosts(sharedData().getAppContentList())
        }else if (viewMvc.currentFilter == Constants.CATEGORY_TRENDING && trendingPosts == null) {
            fetchTrendingPosts()
        }else {
            viewMvc.bindPostsByFilter(if (viewMvc.currentFilter == Constants.CATEGORY_ALL) posts!! else trendingPosts!!)
        }
    }


    override fun onNavItemSelected(itemId: Int) {
        if (itemId == R.id.navd_item_favorite_list) {
            screenNavigator().toFavoriteList()
        }else if (itemId == R.id.navd_item_set_content){
           showSetContentDialog()
        }
    }



    private fun showSetContentDialog(){

        val setAppContentDialog = SetAppContentDialogViewMvc(layoutInflater, sharedData().getAppContentList())
        setAppContentDialog.registerListener(this)
        setAppContentDialog.showSetAppContentDialog()
    }


    override fun onDialogClosedWith(newContents: ArrayList<String>?) {
        if (newContents != null){
            fetchAllPosts(newContents)
            sharedData().saveNewAppContents(newContents)
        }
    }








    private fun fetchAllPosts(appContentsList: ArrayList<String>) {
        fetchUseCase = FetchGifPostsUseCase(databaseReference(), appContentsList)
        fetchUseCase?.registerListener(this)
        fetchUseCase?.fetchGifPosts()
    }


    private fun fetchTrendingPosts() {
        fetchTrendingUseCase = FetchTrendingPostsUseCase(databaseReference())
        fetchTrendingUseCase?.registerListener(this)
        fetchTrendingUseCase?.fetchGifPosts()
    }








    override fun onPostsFetched(posts: ArrayList<PostModel>) {
        this.posts = posts
        this.posts?.shuffle()

        viewMvc.bindAllPosts(this.posts!!)

        fetchUseCase?.removeListeners()
        fetchUseCase?.unregisterListener(this)
        fetchUseCase = null

    }




    override fun onTrendingPostsFetched(trendingPosts: ArrayList<PostModel>) {
        this.trendingPosts = trendingPosts

        viewMvc.bindPostsByFilter(trendingPosts)
        fetchTrendingUseCase?.removeListeners()
        fetchTrendingUseCase?.unregisterListener(this)
        fetchTrendingUseCase = null
    }


    override fun onFetchedCanceled(error_message: String) {
        Snackbar.make(viewMvc.getRootView(), error_message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.try_again)) {
                onTryAgainClicked()
            }.show()

    }


    override fun onGifPostEnded() {
        viewMvc.scrollToPosition(viewMvc.getCurrentAdapterPosition() + 1)
    }



    override fun onReportPostClicked(position: Int) {
        reportViewMvc = ReportPostDialogViewMvc(layoutInflater, posts?.get(position)?.id!!)
        reportViewMvc?.registerListener(this)
        reportViewMvc?.showReportOptionsDialog()
    }


    override fun onReportButtonClicked(
        postID: String,
        reportReason: String,
        additionalInfo: String
    ) {
        val reportUseCase = ReportPostUseCase(databaseReference())
        reportUseCase.reportPost(postID, reportReason, additionalInfo)
        releaseReportWindow()
        systemMessageHelper().showLongMessage(getString(R.string.success_reporting))

    }

    private fun releaseReportWindow() {
        if (reportViewMvc != null) {
            reportViewMvc?.unregisterListener(this)
            reportViewMvc?.dismissReportDialog()
        }
    }

    override fun onDownloadPostClicked(position: Int) {

        val downloadUseCase = PostDownloadUseCase(posts?.get(position)?.downloadUrl!!)
        downloadUseCase.downloadPost()
        downloadUseCase.registerListener(object : PostDownloadUseCase.DownloadListener {


            override fun onProgressChanged(progress: Int) {
                viewMvc.onProgressChanged(position, progress)
            }

            override fun onDownloaded(filePath: String) {
                systemMessageHelper().showLongMessage("Post downloaded to $filePath .")
                viewMvc.onDownloaded(position)
                downloadUseCase.unregisterListener(this)
                editDownloads(position)
                refreshGalleryToShowTheSavedImage(filePath)
                adInterstitial.showAd()
            }


            override fun onFailedDownloaded(error_message: String?) {
                systemMessageHelper().showLongMessage(error_message!!)
                downloadUseCase.unregisterListener(this)

            }
        })

    }



    private fun editDownloads(position: Int) {
        val newValue = posts?.get(position)?.downloads?.plus(1)

        posts?.get(position)?.downloads = newValue!!

        editGifPostUseCase.editDownloads(posts?.get(position)?.id!!, newValue)
    }



    private fun refreshGalleryToShowTheSavedImage(filePath: String) {
        MediaScannerConnection.scanFile(
            requireContext(), arrayOf(filePath), null,
            null
        )
    }





    override fun onSharePostClicked(position: Int) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(posts?.get(position)?.downloadUrl!!))
        shareIntent.type = "image/gif"
        startActivity(Intent.createChooser(shareIntent, "Share post"))
//        adInterstitial.showAd()
    }


    override fun onBookmarkClicked(position: Int, isBookmarked: Boolean) {
        val postModelInStr = PostModelHelper.postModelToString(posts?.get(position)?.downloadUrl!!,
            posts?.get(position)?.posterPicture!!)


        if (isBookmarked) {
            sharedData().addToFavorite(postModelInStr)
            systemMessageHelper().showShortMessage("Post added to favorite.")
        } else {
            sharedData().removeFromFavorite(postModelInStr)
            systemMessageHelper().showShortMessage("Post removed from favorite.")
        }
    }


    
    override fun onLikePostClicked(position: Int, newValue: Int) {
        val post = posts?.get(position)

        if (newValue > post?.likes!!)
            sharedData().likePost(post.id)
        else
            sharedData().unlikePost(post.id)




        posts?.get(position)?.likes = newValue
        editGifPostUseCase.editLikes(post.id, newValue)


    }









}