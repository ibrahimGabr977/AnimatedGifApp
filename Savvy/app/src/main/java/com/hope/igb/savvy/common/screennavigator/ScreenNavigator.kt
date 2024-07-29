package com.hope.igb.savvy.common.screennavigator

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.hope.igb.savvy.screens.main.MainFragmentDirections


class ScreenNavigator(navHostFragment: NavHostFragment){
    private val navController: NavController = navHostFragment.navController

    fun toFavoriteList(){
        navController.navigate(MainFragmentDirections.actionMainFragmentToFavoritesFragment())
    }

}