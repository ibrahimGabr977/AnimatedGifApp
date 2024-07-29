package com.hope.igb.savvy.adhelper

import android.content.Context
import com.google.android.gms.ads.MobileAds

class AdmobHelper {


    companion object{
        const val adBannerOne: String = "BANNER_ONE"
        const val adBannerTwo: String = "BANNER_TWO"
        const val adBannerThree: String = "BANNER_THREE"
        const val adBannerFour: String = "BANNER_FOUR"
        const val adBannerFive: String = "BANNER_FIVE"

        fun initializeAdmob(context: Context){
            MobileAds.initialize(context)
        }
    }



}