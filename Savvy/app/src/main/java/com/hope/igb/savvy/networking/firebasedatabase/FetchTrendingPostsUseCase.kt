package com.hope.igb.savvy.networking.firebasedatabase

import com.google.firebase.database.*
import com.hope.igb.savvy.common.views.BaseObservable
import com.hope.igb.savvy.networking.models.PostModel

class FetchTrendingPostsUseCase(private val postRef: DatabaseReference)
    : BaseObservable<FetchTrendingPostsUseCase.Listener>() {
    interface Listener {
        fun onTrendingPostsFetched(trendingPosts: ArrayList<PostModel>)
        fun onFetchedCanceled(error_message: String)
    }






    private val trendingPosts = ArrayList<PostModel>(25)


    private val rankChild = "id"




    private  val eventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            onDataSetChanged(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            onCanceled(error.message)
        }
    }


    fun fetchGifPosts(){
        postRef
            .child("trending")
            .orderByChild(rankChild)
            .limitToFirst(25)
            .addValueEventListener(eventListener)
    }





    private fun onDataSetChanged(snapshot: DataSnapshot) {

        trendingPosts.clear()

        for (dss in snapshot.children)
            try{
                dss.getValue(PostModel::class.java)?.let { trendingPosts.add(it) }

            }catch (e: Exception){
                continue
            }



        for (listener in getListeners())
            listener.onTrendingPostsFetched(trendingPosts)


    }




    private fun onCanceled(message: String){
        for (listener in getListeners())
            listener.onFetchedCanceled(message)
    }

    fun removeListeners() {
        postRef.removeEventListener(eventListener)

    }
}