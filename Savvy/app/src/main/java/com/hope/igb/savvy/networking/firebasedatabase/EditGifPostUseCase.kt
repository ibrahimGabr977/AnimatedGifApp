package com.hope.igb.savvy.networking.firebasedatabase

import com.google.firebase.database.DatabaseReference

class EditGifPostUseCase(private val postRef: DatabaseReference) {



    fun editLikes(postID: String, newValue: Int) {
        postRef.child("all_gif_posts")
            .child(postID)
            .child("likes")
            .setValue(newValue)
    }

    fun editDownloads(postID: String, newValue: Int) {
        postRef.child("all_gif_posts")
            .child(postID)
            .child("downloads")
            .setValue(newValue)
    }

}