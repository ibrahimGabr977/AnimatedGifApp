package com.hope.igb.savvy.common.presistData

import android.content.Context
import android.content.SharedPreferences


class SharedData(private val context: Context) {



    private val favoritesList = "favoritesList"
    private val likedPosts = "likedList"

    private fun getSharedPreferences(): SharedPreferences? {
        return context.applicationContext.getSharedPreferences("postsPref",0)
    }


    fun isNewUser(): Boolean{
        return getAppContentList().isEmpty()
    }

    fun saveNewAppContents(appContent: ArrayList<String>){
        val editor = getSharedPreferences()?.edit()

        var appContentStr = ""
        for (content in appContent)
            appContentStr += ("${content},")


        editor?.putString("appContentCategories", appContentStr)
        editor?.apply()

    }




    fun getAppContentList(): ArrayList<String> {
        val appContentCategories = getSharedPreferences()?.getString("appContentCategories", "newUser")

        return if (appContentCategories?.trim()?.isEmpty()!!)
             ArrayList()
        else
           ArrayList(appContentCategories.split(","))
    }







    fun addToFavorite(model: String){
        val editor = getSharedPreferences()?.edit()
        val oldSet = getFavoriteList()
        val newSet: MutableSet<String> = LinkedHashSet()
        newSet.add(model)
        newSet.addAll(oldSet!!)

        editor?.putStringSet(favoritesList, newSet)
        editor?.apply()

    }




    fun removeFromFavorite(model: String){
        val editor = getSharedPreferences()?.edit()
        val oldSet = getFavoriteList()
        val newSet: MutableSet<String> = LinkedHashSet()

        for (ele in oldSet!!)
            if (ele != model)
                newSet.add(ele)


        editor?.putStringSet(favoritesList, newSet)
        editor?.apply()
    }

    fun isBookmarked(model: String): Boolean{
        return getFavoriteList()!!.contains(model)
    }


    fun getFavoriteList(): MutableSet<String>? {
        return getSharedPreferences()?.getStringSet(favoritesList, LinkedHashSet<String>())
    }



    fun likePost(postId: String){
        val editor = getSharedPreferences()?.edit()
        val set = getLikedPosts()
        set?.add(postId)
        editor?.putStringSet(likedPosts, set)
        editor?.apply()

    }




    fun unlikePost(postId: String){
        val editor = getSharedPreferences()?.edit()
        val set = getLikedPosts()
        set?.remove(postId)
        editor?.putStringSet(likedPosts, set)
        editor?.apply()
    }

    fun isLiked(postId: String): Boolean{
        return getLikedPosts()!!.contains(postId)
    }


    private fun getLikedPosts(): MutableSet<String>? {
        return getSharedPreferences()?.getStringSet(likedPosts, LinkedHashSet<String>())
    }



}