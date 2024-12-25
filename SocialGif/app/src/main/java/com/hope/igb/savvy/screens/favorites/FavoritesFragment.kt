package com.hope.igb.savvy.screens.favorites


import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.hope.igb.savvy.R
import com.hope.igb.savvy.adhelper.AdInterstitialHelper
import com.hope.igb.savvy.screens.BaseFragment
import com.hope.igb.savvy.networking.firebasestorage.PostDownloadUseCase
import com.hope.igb.savvy.screens.favorites.displaypost.DisplayPostDialogViewMvc

class FavoritesFragment : BaseFragment(), FavoritesViewMvc.FavoritesListener,
    FavoriteAdapterItemViewMvc.ItemListener, DisplayPostDialogViewMvc.Listener {

    private lateinit var viewMvc: FavoritesViewMvc
    private lateinit var favoriteList: ArrayList<String>
    private lateinit var adInterstitial: AdInterstitialHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        viewMvc = FavoritesViewMvc(inflater, container, mediaLoader())


        addCustomOnBackPressedCallback()


        favoriteList = ArrayList()
        favoriteList.addAll(sharedData().getFavoriteList()!!)


        if (favoriteList.isEmpty())
            viewMvc.bindEmptyFavoriteListView()



        adInterstitial = AdInterstitialHelper(requireActivity())

        return viewMvc.getRootView()
    }




    private fun addCustomOnBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewMvc.isDisplayDialogOpening())
                        viewMvc.removeDisplayDialogViewMvc(this@FavoritesFragment)
                    else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            })
    }


    override fun onStart() {
        super.onStart()

        if (favoriteList.isNotEmpty())
            viewMvc.bindFavoriteList(favoriteList, this)

        viewMvc.registerListener(this)

        adInterstitial.initAd(getString(R.string.interstitial_ad_id1))


    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)

    }



    override fun onBackClicked() {
            requireActivity().onBackPressedDispatcher.onBackPressed()

    }

    override fun onPostClicked(position: Int) {
        viewMvc.displayPostDialog(this, favoriteList[position])
    }





    override fun onDownloadPostClicked(downloadUrl: String) {
        val downloadUseCase = PostDownloadUseCase(downloadUrl)
        downloadUseCase.downloadPost()
        downloadUseCase.registerListener(object : PostDownloadUseCase.DownloadListener {


            override fun onProgressChanged(progress: Int) {
                viewMvc.displayDialogOnProgressChanged(progress)
            }

            override fun onDownloaded(filePath: String) {
                systemMessageHelper().showLongMessage("Post downloaded to $filePath .")
                viewMvc.displayDialogOnPostDownloaded()
                downloadUseCase.unregisterListener(this)
                refreshGalleryToShowTheSavedImage(filePath)
                adInterstitial.showAd()
            }


            override fun onFailedDownloaded(error_message: String?) {
                systemMessageHelper().showLongMessage(error_message!!)
                downloadUseCase.unregisterListener(this)

            }
        })

    }

    private fun refreshGalleryToShowTheSavedImage(filePath: String) {
        MediaScannerConnection.scanFile(
            requireContext(), arrayOf(filePath), null,
            null
        )
    }

    override fun onSharePostClicked(downloadUrl: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(downloadUrl))
        shareIntent.type = "image/gif"
        startActivity(Intent.createChooser(shareIntent, "Share post"))
        adInterstitial.showAd()
    }


    override fun onBookmarkClicked(isBookmarked: Boolean, postModelInStr: String) {

        if (isBookmarked) {
            sharedData().addToFavorite(postModelInStr)
            systemMessageHelper().showShortMessage("Post added again to favorite.")
        } else {
            sharedData().removeFromFavorite(postModelInStr)
            systemMessageHelper().showShortMessage("Post removed from favorite.")
        }
    }

    override fun onDialogClosed() {
        viewMvc.removeDisplayDialogViewMvc(this)
    }

}