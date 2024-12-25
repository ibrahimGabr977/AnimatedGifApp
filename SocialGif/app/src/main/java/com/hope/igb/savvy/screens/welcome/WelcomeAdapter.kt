package com.hope.igb.savvy.screens.welcome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter

class WelcomeAdapter(private val imagesRes: ArrayList<Int>,
                     private val titles: ArrayList<String>,
                     private val contents: ArrayList<String>)
    : PagerAdapter(){





   override fun getCount(): Int {
        return titles.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewMvc = WelcomeItemViewMvc(LayoutInflater.from(container.context), container)
        container.addView(viewMvc.getRootView())
        viewMvc.bindView(imagesRes[position], titles[position], contents[position])
        return viewMvc.getRootView()
    }

   override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}