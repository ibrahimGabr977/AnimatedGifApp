package com.hope.igb.savvy.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hope.igb.savvy.R
import com.hope.igb.savvy.adhelper.AdmobHelper
import com.hope.igb.savvy.common.messagehelper.SystemMessageHelper
import com.hope.igb.savvy.common.presistData.SharedData
import com.hope.igb.savvy.common.screennavigator.ScreenNavigator
import com.hope.igb.savvy.util.MediaLoader
import com.hope.igb.savvy.util.PermissionHandler

class BaseActivity : AppCompatActivity()     {


    private lateinit var permissionHandler: PermissionHandler
    private var screenNavigator: ScreenNavigator? = null
    private var reference: DatabaseReference? = null
    private var sharedData: SharedData? = null
    private var systemMessageHelper: SystemMessageHelper? = null
    private var mediaLoader: MediaLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHandler  = PermissionHandler(this)


        if (savedInstanceState == null)
            AdmobHelper.initializeAdmob(this)

        setContentView(R.layout.activity_base)


    }

    fun getPermissionHandler(): PermissionHandler {
        return permissionHandler
    }



    fun screenNavigator(): ScreenNavigator {
        if (screenNavigator == null)
            screenNavigator = ScreenNavigator(getNavHostFragment()!!)

        return screenNavigator!!
    }


    private fun getNavHostFragment(): NavHostFragment? {
        return supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment?
    }







    fun databaseReference(): DatabaseReference {
        if (reference == null)
            reference = FirebaseDatabase.getInstance().reference

        return reference!!
    }

    fun sharedData(): SharedData{
        if (sharedData == null)
            sharedData = SharedData(this)

        return sharedData!!
    }


    fun systemMessageHelper(): SystemMessageHelper{
        if (systemMessageHelper == null)
            systemMessageHelper = SystemMessageHelper(this)

        return systemMessageHelper!!
    }


    fun mediaLoader(): MediaLoader{
        if (mediaLoader == null)
            mediaLoader = MediaLoader(this)

        return mediaLoader!!
    }

}