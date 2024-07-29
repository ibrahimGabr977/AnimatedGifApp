package com.hope.igb.catgif.screens.gifplayer.online;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * a shared preference class that contained a list of all liked GIFs' ids
 */

class LikedGIFsList {

    private final SharedPreferences preferences;

    private final String LIKED_LIST_KEY = "LIKED_LIST";


    protected LikedGIFsList(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("GIF_PREFERENCE", 0);

    }




    private Set<String> getLikedList(){
        return preferences.getStringSet(LIKED_LIST_KEY, new HashSet<>()); }




    protected boolean isAlreadyLiked(String gif_id){
        return getLikedList().contains(gif_id); }



    protected void putLike(String gif_id){
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> updated_list = getLikedList();
        updated_list.add(gif_id);
        editor.putStringSet(LIKED_LIST_KEY, updated_list);
        editor.apply();
    }

    protected void removeLike(String gif_id){
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> updated_list = getLikedList();
        updated_list.remove(gif_id);
        editor.putStringSet(LIKED_LIST_KEY, updated_list);
        editor.apply();
    }


}
