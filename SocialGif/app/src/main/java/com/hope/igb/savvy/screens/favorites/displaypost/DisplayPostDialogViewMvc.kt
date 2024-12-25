package com.hope.igb.savvy.screens.favorites.displaypost

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View

import androidx.appcompat.app.AlertDialog
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.Constants
import com.hope.igb.savvy.common.views.BaseObservable
import com.hope.igb.savvy.databinding.WindowDisplayPostBinding
import com.hope.igb.savvy.networking.models.PostModelHelper
import com.hope.igb.savvy.util.DefaultProfilePicturesList
import com.hope.igb.savvy.util.MediaLoader

@SuppressLint("InflateParams")
class DisplayPostDialogViewMvc(inflater: LayoutInflater, private val postModelInString: String)
    :BaseObservable<DisplayPostDialogViewMvc.Listener>() {

    interface Listener {
        fun onDownloadPostClicked(downloadUrl: String)
        fun onSharePostClicked(downloadUrl: String)
        fun onBookmarkClicked(isBookmarked: Boolean, postModelInStr: String)
        fun onDialogClosed()
    }

    private val  dialog: AlertDialog
    private val binding: WindowDisplayPostBinding
    private val downloadUrl = PostModelHelper.getDownloadUrl(postModelInString)
    private var isBookmarked = true
    private val mediaLoader: MediaLoader


    init {
        binding = WindowDisplayPostBinding.inflate(inflater)
        val builder: AlertDialog.Builder = AlertDialog.Builder(binding.root.context)


        builder.setView(binding.root)

        dialog = builder.create()

        mediaLoader = MediaLoader(binding.root.context)
    }



    fun show(){
        mediaLoader.loadGif(downloadUrl).into(binding.postView)

        initChildViews()

        binding.close.setOnClickListener {
            dialog.dismiss()

        }

        val window = dialog.window

        window?.decorView?.setBackgroundResource(R.drawable.z_dialogs_background)
        window?.setLayout((Constants.SCREEN_WIDTH * 0.9).toInt(), (Constants.SCREEN_HEIGHT * 0.87).toInt())


        window?.setWindowAnimations(R.style.DialogAnimation)

        dialog.show()

        dialog.setOnDismissListener {
            for (listener in getListeners()) listener.onDialogClosed()
        }
    }


    fun dismiss(){
        dialog.dismiss()
    }


    private fun initChildViews(){
        binding.posterImage.setImageResource(
            DefaultProfilePicturesList.getProfilePictureFrom(
            PostModelHelper.getPosterPicture(postModelInString)))


        binding.downloadPost.setOnClickListener {
            onDownloadClicked(downloadUrl)
        }

        binding.sharePost.setOnClickListener {
            for (listener in getListeners())
                listener.onSharePostClicked(downloadUrl)
        }


        binding.bookmarkPost.setOnClickListener {
            onAddToFavoriteClicked()
        }
    }




    private fun onDownloadClicked(downloadUrl: String) {
        onStartDownloading()
        for (listener in getListeners())
            listener.onDownloadPostClicked(downloadUrl)
    }


    private fun onStartDownloading() {
        binding.downloadProgress.visibility = View.VISIBLE
        binding.downloadProgress.progress = 0
    }

    fun onProgressChanged(progress: Int) {
        binding.downloadProgress.progress = progress
    }

    fun onDownloaded() {
        binding.downloadProgress.visibility = View.GONE
    }


    private fun onAddToFavoriteClicked() {
        if (!isBookmarked) bookmarkedPost() else unBookmarkedPost()

        for (listener in getListeners())
            listener.onBookmarkClicked(isBookmarked, postModelInString)


    }

    private fun bookmarkedPost() {
        binding.bookmarkPost.setImageResource(R.drawable.b_post_holder_add_favorited)
        isBookmarked = true
    }

    private fun unBookmarkedPost() {
        binding.bookmarkPost.setImageResource(R.drawable.b_post_holder_remove_favorited)
        isBookmarked = false

    }


}