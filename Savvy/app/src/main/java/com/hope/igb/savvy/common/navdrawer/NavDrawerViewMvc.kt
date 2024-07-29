package com.hope.igb.savvy.common.navdrawer

import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.views.BaseObservableViewMvc


class NavDrawerViewMvc(private val drawerLayout: DrawerLayout) : BaseObservableViewMvc<NavDrawerViewMvc.NavDrawerListener>(),
    NavigationView.OnNavigationItemSelectedListener{


    interface NavDrawerListener {
        fun onNavItemSelected(itemId: Int)
    }





    init {
        setRootView(drawerLayout)
        val mNavigationView: NavigationView = findViewById(R.id.nav_view)
        mNavigationView.setNavigationItemSelectedListener(this)
        mNavigationView.itemIconTintList = null


        findViewById<View>(R.id.nav_dummy_view).setOnClickListener{
            if (isDrawerOpen())
                drawerLayout.closeDrawer(GravityCompat.START)
        }

    }


    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }


    fun isDrawerOpen(): Boolean {
        return drawerLayout.isDrawerOpen(GravityCompat.START)
    }


    fun closeDrawer() {
        //closeDrawers method doesn't work as expected
        drawerLayout.closeDrawer(GravityCompat.START, false)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        closeDrawer()
        for (listener in getListeners()) listener.onNavItemSelected(item.itemId)
        return false
    }

}