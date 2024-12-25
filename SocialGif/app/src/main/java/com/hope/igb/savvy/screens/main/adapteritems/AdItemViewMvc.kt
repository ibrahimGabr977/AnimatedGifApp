package com.hope.igb.savvy.screens.main.adapteritems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdView
import com.hope.igb.savvy.R
import com.hope.igb.savvy.adhelper.AdBannerHelper
import com.hope.igb.savvy.adhelper.AdmobHelper
import com.hope.igb.savvy.common.views.BaseViewMvc

class AdItemViewMvc(inflater: LayoutInflater, parent : ViewGroup) :
    BaseViewMvc(), AdBannerHelper.AdmobBannerListener {

    private val adView1: AdView
    private val adView2: AdView
    private val adView3: AdView
    private val adView4: AdView
    private val adView5: AdView
    private val adBanner1 : AdBannerHelper
    private val adBanner2 : AdBannerHelper
    private val adBanner3 : AdBannerHelper
    private val adBanner4 : AdBannerHelper
    private val adBanner5 : AdBannerHelper

    private var adIndex: Int = 0


    init {
        setRootView(inflater.inflate(R.layout.view_holder_ad_banner, parent, false))

        adView1 = findViewById(R.id.mainAdView1)
        adBanner1 = AdBannerHelper(AdmobHelper.adBannerOne, adView1)

        adView2 = findViewById(R.id.mainAdView2)
        adBanner2 = AdBannerHelper(AdmobHelper.adBannerTwo, adView2)

        adView3 = findViewById(R.id.mainAdView3)
        adBanner3 = AdBannerHelper(AdmobHelper.adBannerThree, adView3)

        adView4 = findViewById(R.id.mainAdView4)
        adBanner4 = AdBannerHelper(AdmobHelper.adBannerFour, adView4)

        adView5 = findViewById(R.id.mainAdView5)
        adBanner5 = AdBannerHelper(AdmobHelper.adBannerFive, adView5)

    }


    fun bindAdViewWithIndex(adIndex : Int){
        this.adIndex = adIndex

         when (adIndex) {
             0 -> adBanner1.loadAd()
             1 -> adBanner2.loadAd()
             2 -> adBanner3.loadAd()
             3 -> adBanner4.loadAd()
             4 -> adBanner5.loadAd()
         }


    }

    fun registerAd(){
        when (adIndex) {
            0 -> adBanner1.registerListener(this)
            1 -> adBanner2.registerListener(this)
            2 -> adBanner3.registerListener(this)
            3 -> adBanner4.registerListener(this)
            4 -> adBanner5.registerListener(this)
        }

    }


    fun unRegisterAd(){
        when (adIndex) {
            0 -> adBanner1.unregisterListener(this)
            1 -> adBanner2.unregisterListener(this)
            2 -> adBanner3.unregisterListener(this)
            3 -> adBanner4.unregisterListener(this)
            4 -> adBanner5.unregisterListener(this)
        }
    }

    override fun onAdLoadedSuccess(adName: String) {
        getRootView().visibility = View.VISIBLE

        when (adName) {
            AdmobHelper.adBannerOne -> {
                adView1.visibility = View.VISIBLE
                adView2.visibility = View.GONE
                adView3.visibility = View.GONE
                adView4.visibility = View.GONE
                adView5.visibility = View.GONE
            }
            AdmobHelper.adBannerTwo -> {
                adView1.visibility = View.GONE
                adView2.visibility = View.VISIBLE
                adView3.visibility = View.GONE
                adView4.visibility = View.GONE
                adView5.visibility = View.GONE
            }

            AdmobHelper.adBannerThree -> {
                adView1.visibility = View.GONE
                adView2.visibility = View.GONE
                adView3.visibility = View.VISIBLE
                adView4.visibility = View.GONE
                adView5.visibility = View.GONE
            }

            AdmobHelper.adBannerFour -> {
                adView1.visibility = View.GONE
                adView2.visibility = View.GONE
                adView3.visibility = View.GONE
                adView4.visibility = View.VISIBLE
                adView5.visibility = View.GONE
            }

            else -> {
                adView1.visibility = View.GONE
                adView2.visibility = View.GONE
                adView3.visibility = View.GONE
                adView4.visibility = View.GONE
                adView5.visibility = View.VISIBLE
            }
        }
    }

    override fun onAdLoadedFailed(adName: String) {
        getRootView().visibility = View.GONE


    }



}