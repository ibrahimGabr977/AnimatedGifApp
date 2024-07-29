package com.hope.igb.savvy.screens.main.adapteritems

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.compose.ui.text.android.InternalPlatformTextApi
import androidx.compose.ui.text.android.style.ShadowSpan
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.Constants
import com.hope.igb.savvy.common.views.BaseObservableViewMvc
import com.hope.igb.savvy.networking.models.PostModel
import com.hope.igb.savvy.util.DateFormatBuilder
import com.hope.igb.savvy.util.DefaultProfilePicturesList
import com.hope.igb.savvy.util.MediaLoader
import de.hdodenhof.circleimageview.CircleImageView
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainAdapterItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val dateBuilder: DateFormatBuilder,
    private val category: String,
    private val mediaLoader: MediaLoader,
    private var isBookmarked: Boolean,
    private var isLiked: Boolean

)

    : BaseObservableViewMvc<MainAdapterItemViewMvc.ItemListener>(),
    RequestListener<GifDrawable> {


    interface ItemListener {
        fun onGifPostEnded()
        fun onDownloadPostClicked(position: Int)
        fun onSharePostClicked(position: Int)
        fun onLikePostClicked(position: Int, newValue: Int)
        fun onBookmarkClicked(position: Int, isBookmarked: Boolean)
        fun onReportPostClicked(position: Int)

    }


    private val posterPicture: CircleImageView
    private val posterName: TextView
    private val postTime: TextView
    private val reportPost: ImageView

    private val imageView: ImageView

    private val download: ImageView
    private val bookmark: ImageView
    private val share: ImageView
    private val like: ImageView

//    private var isBookmarked = false
//    private var isLiked = false

    private val likesCount: TextView
    private val description: TextView


    private var downloadProgress: ProgressBar? = null

    private var autoScrolled: Boolean = false


    init {
        setRootView(inflater.inflate(R.layout.view_holder_post, parent, false))

        val params = getRootView().layoutParams as ViewGroup.LayoutParams

        params.height = (getScreenHeight() * 0.71).toInt()
        getRootView().layoutParams = params



        posterPicture = findViewById(R.id.poster_image)

        posterName = findViewById(R.id.poster_name)

        postTime = findViewById(R.id.post_time)
        setPostTimeTextColor()


        reportPost = findViewById(R.id.report_post)




        imageView = findViewById(R.id.post_view)
        imageView.clipToOutline = true

        like = findViewById(R.id.like_post)
        bookmark = findViewById(R.id.bookmark_post)
        share = findViewById(R.id.share_post)
        download = findViewById(R.id.download_post)

        likesCount = findViewById(R.id.likes_count)
        description = findViewById(R.id.post_description)





    }



    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private fun setPostTimeTextColor() {
        if(category == Constants.CATEGORY_TRENDING.lowercase())
            postTime.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_light_color))
        else
            postTime.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_color))
    }




    fun bindAdapterItem(postModel: PostModel, position: Int, autoScrolled: Boolean) {

        this.autoScrolled = autoScrolled



        posterPicture.setImageResource(DefaultProfilePicturesList.getProfilePictureFrom(postModel.posterPicture))

        posterName.text = postModel.posterName




        postTime.text = if(category == Constants.CATEGORY_TRENDING.lowercase()) {

            "#"+(position + 1).toString()+" in trending"

        }else
            dateBuilder.getPostDate(Constants.CONST_TIME_MILLIS - postModel.id.toLong())





        reportPost.setOnClickListener {
            for (listener in getListeners()) listener.onReportPostClicked(
                position
            )
        }

        mediaLoader.loadGif(postModel.downloadUrl).listener(this).into(imageView)

        //sharedData.isBookmarked(postModelToString(postModel.downloadUrl, postModel.posterPicture))
        if (isBookmarked)
            bookmarkedPost()

        //sharedData.isLiked(postModel.id)
        if (isLiked)
            likedPost()



        download.setOnClickListener {
            onDownloadClicked(position)
        }

        share.setOnClickListener {
            for (listener in getListeners())
                listener.onSharePostClicked(position)
        }


        bookmark.setOnClickListener {
            onAddToFavoriteClicked(position)
        }



        like.setOnClickListener {

            onLikePostClicked(position, postModel.likes)
        }

        displayLikesCount(postModel.likes)
        
        
        description.text = setPostDescription(postModel.description)


    }




    private fun onDownloadClicked(position: Int) {

        onStartDownloading()
        for (listener in getListeners())
            listener.onDownloadPostClicked(position)
    }

    private fun onStartDownloading() {
        downloadProgress = findViewById(R.id.download_post_progress)
        downloadProgress?.visibility = View.VISIBLE
        downloadProgress?.progress = 0
    }

    fun onProgressChanged(progress: Int) {
        downloadProgress?.progress = progress
    }

    fun onDownloaded() {
        downloadProgress?.visibility = View.GONE
        downloadProgress = null
    }


    private fun onAddToFavoriteClicked(position: Int) {

        if (!isBookmarked) bookmarkedPost() else unBookmarkedPost()

        for (listener in getListeners())
            listener.onBookmarkClicked(position, isBookmarked)


    }




    private fun bookmarkedPost() {
        bookmark.setImageResource(R.drawable.b_post_holder_add_favorited)
        isBookmarked = true
    }

    private fun unBookmarkedPost() {
        bookmark.setImageResource(R.drawable.b_post_holder_remove_favorited)
        isBookmarked = false

    }


    private fun onLikePostClicked(position: Int, likes: Int) {
        var likesValue = likes

        likesValue += if (!isLiked) {
            likedPost()
            1
        } else {
            unlikedPost()
            -1
        }


        displayLikesCount(likesValue)

        for (listener in getListeners())
            listener.onLikePostClicked(position, likesValue)
    }


    private fun likedPost() {
        like.setImageResource(R.drawable.b_post_holder_liked)
        isLiked = true


    }

    private fun unlikedPost() {
        like.setImageResource(R.drawable.b_post_holder_unliked)
        isLiked = false

    }

    private fun displayLikesCount(likes: Int) {
        if (likes > 10){
            likesCount.text =  if (likes < 1000)  ("" + likes)  else  ("" + (likes / 100) / 10.0 + "K")

            likesCount.visibility = View.VISIBLE
        }
    }




    @OptIn(InternalPlatformTextApi::class)
    private fun setPostDescription(description: String): SpannableString {
        val spannableString = SpannableString(description)

        val pattern: Pattern = Pattern.compile("#\\w+")
        val matcher: Matcher = pattern.matcher(description)

        while (matcher.find()) {
            val startIndex = matcher.start()
            val endIndex = matcher.end()
            spannableString.setSpan(textShadowSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
        return spannableString
    }

    @OptIn(InternalPlatformTextApi::class)
    private fun textShadowSpan(): ShadowSpan {
        val radius = 1.6f
        val dx = 5.0f
        val dy = 5.0f
        val color: Int = ContextCompat.getColor(getContext(), R.color.blue_light_color)
        return ShadowSpan(color, dx, dy, radius)
    }




    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<GifDrawable>?,
        isFirstResource: Boolean
    ): Boolean {
        return false
    }

    override fun onResourceReady(
        resource: GifDrawable?,
        model: Any?,
        target: Target<GifDrawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {

        if (autoScrolled) {
            resource?.setLoopCount(1)
            resource?.registerAnimationCallback(object :
                Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable) {
                    super.onAnimationEnd(drawable)

                    for (listener in getListeners())
                        listener.onGifPostEnded()
                }
            })
        }



        return false
    }


}