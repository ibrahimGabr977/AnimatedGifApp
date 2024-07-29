package com.hope.igb.savvy.networking.firebasedatabase

import com.google.firebase.database.*
import com.hope.igb.savvy.common.Constants
import com.hope.igb.savvy.common.views.BaseObservable
import com.hope.igb.savvy.networking.models.PostModel

class FetchGifPostsUseCase(
    private val postRef: DatabaseReference,
    private val content: List<String>
) : BaseObservable<FetchGifPostsUseCase.Listener>() {
    interface Listener {
        fun onPostsFetched(posts: ArrayList<PostModel>)
        fun onFetchedCanceled(error_message: String)
    }


    private val posts: ArrayList<PostModel> = ArrayList()


    private val eventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            onDataSetChanged(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            onCanceled(error.message)
        }
    }


    fun fetchGifPosts() {
        postRef
            .child(Constants.POSTS_ROOT)
            .orderByChild("id")
            .addValueEventListener(eventListener)
    }


    private fun onDataSetChanged(snapshot: DataSnapshot) {

        posts.clear()

        for (dss in snapshot.children)
            try {
                dss.getValue(PostModel::class.java)?.let {


                    if ((content.isNotEmpty() && content.contains(it.category)) || content.isEmpty())
                        posts.add(it)


                }
            } catch (e: Exception) {
                continue
            }



        for (listener in getListeners())
            listener.onPostsFetched(posts)


    }


    private fun onCanceled(message: String) {
        for (listener in getListeners())
            listener.onFetchedCanceled(message)
    }

    fun removeListeners() {
        postRef.removeEventListener(eventListener)

    }


}