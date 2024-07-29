package com.hope.igb.savvy.screens

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hope.igb.savvy.common.messagehelper.SystemMessageHelper
import com.hope.igb.savvy.common.presistData.SharedData
import com.hope.igb.savvy.common.screennavigator.ScreenNavigator
import com.hope.igb.savvy.screens.BaseActivity
import com.hope.igb.savvy.util.MediaLoader

open class BaseFragment : Fragment() {

    fun screenNavigator(): ScreenNavigator {
        return (requireActivity() as BaseActivity).screenNavigator()
    }



    fun databaseReference(): DatabaseReference {
        return (requireActivity() as BaseActivity).databaseReference()
    }

    fun sharedData(): SharedData{
        return (requireActivity() as BaseActivity).sharedData()
    }


    fun systemMessageHelper(): SystemMessageHelper{
        return (requireActivity() as BaseActivity).systemMessageHelper()
    }


    fun mediaLoader(): MediaLoader{
        return (requireActivity() as BaseActivity).mediaLoader()
    }

}