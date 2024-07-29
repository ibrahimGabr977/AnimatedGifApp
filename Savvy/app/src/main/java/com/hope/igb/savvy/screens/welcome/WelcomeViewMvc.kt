package com.hope.igb.savvy.screens.welcome

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.views.BaseObservableViewMvc
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("InflateParams")
class WelcomeViewMvc(inflater: LayoutInflater) :
    BaseObservableViewMvc<WelcomeViewMvc.WelcomeListener>() {


    interface WelcomeListener {
        fun onGetStartedClicked()
    }


    private val indicators: ArrayList<ImageView>
    private var currentPage = 0
    private val next: ImageView
    private val letsGo: TextView
    private val skip: TextView
    private val viewPager: ViewPager


    init {
        setRootView(inflater.inflate(R.layout.activity_welcome, null, false))
        viewPager = findViewById(R.id.welcomeViewPager)


        indicators = ArrayList()

        skip = findViewById(R.id.skip)

        skip.setOnClickListener {
            onNextClicked(
                indicators.size - 1
            )
        }
        next = findViewById(R.id.next_page)
        letsGo = findViewById(R.id.letsGo)

        initPagesIndicators()
    }


    fun bindWelcomeView() {
        val adapter = WelcomeAdapter(imagesRes(), titles(), contents())
        viewPager.adapter = adapter
        viewPager.clipToPadding = false
        viewPager.setPadding(paddingPx, 0, paddingPx, 0)
        viewPager.setPageTransformer(false, transformer)
        onNormalPagesBind()
        onPageChanged()
    }


    private fun initPagesIndicators() {
        val indicatorsContainer: LinearLayout = findViewById(R.id.pages_indicators_container)
        indicators.add(indicatorsContainer.findViewById(R.id.welcome_indicator_page1))
        indicators.add(indicatorsContainer.findViewById(R.id.welcome_indicator_page2))
        indicators.add(indicatorsContainer.findViewById(R.id.welcome_indicator_page3))
        indicators.add(indicatorsContainer.findViewById(R.id.welcome_indicator_page4))
        indicators.add(indicatorsContainer.findViewById(R.id.welcome_indicator_page5))
    }


    private fun onPageChanged() {
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {


                if (currentPage - position == 1)
                    indicators[position + 1].setImageResource(R.drawable.a_welcome_unselected_paged)

                else if (currentPage - position == -1)
                    indicators[position - 1].setImageResource(R.drawable.a_welcome_unselected_paged)
                else
                    indicators[currentPage].setImageResource(R.drawable.a_welcome_unselected_paged)



                indicators[position].setImageResource(R.drawable.a_welcome_selected_paged)
                currentPage = position




                if (position == indicators.size - 1)
                    onLastPageBind()
                else
                    onNormalPagesBind()


            }
        })
    }

    private fun onNormalPagesBind() {
//        next.text = getContext().getString(R.string.welcome_content_description_next_page)
        letsGo.visibility = View.INVISIBLE
        next.visibility = View.VISIBLE
        next.setOnClickListener {
            onNextClicked(
                currentPage + 1
            )
        }
    }

    private fun onNextClicked(position: Int) {
        if (position < indicators.size) {
            viewPager.setCurrentItem(position, true)
        }

    }

    private fun onLastPageBind() {
        letsGo.visibility = View.VISIBLE
        next.visibility = View.GONE

        letsGo.setOnClickListener { for (listener in getListeners()) listener.onGetStartedClicked() }
    }

    private fun imagesRes(): ArrayList<Int> {
        return ArrayList(
            listOf(
                R.drawable.a_welcome_page1_image, R.drawable.a_welcome_page2_image,
                R.drawable.a_welcome_page3_image, R.drawable.a_welcome_page4_image,
                R.drawable.a_welcome_page5_image
            )
        )
    }


    private fun titles(): ArrayList<String> {
        return ArrayList(
            listOf(
                getContext().getString(R.string.welcome_page1_title),
                getContext().getString(R.string.welcome_page2_title),
                getContext().getString(R.string.welcome_page3_title),
                getContext().getString(R.string.welcome_page4_title),
                getContext().getString(R.string.welcome_page5_title)
            )
        )
    }


    private fun contents(): ArrayList<String> {
        return ArrayList<String>(
            listOf(
                getContext().getString(R.string.welcome_page1_content),
                getContext().getString(R.string.welcome_page2_content),
                getContext().getString(R.string.welcome_page3_content),
                getContext().getString(R.string.welcome_page4_content),
                getContext().getString(R.string.welcome_page5_content)
            )
        )
    }


    private val paddingPx = 0
    private val minScale = 0.65f
    private val maxScale = 1f


    private val transformer: ViewPager.PageTransformer =
        ViewPager.PageTransformer { page, position ->
            val pagerWidthPx = (page.parent as ViewPager).width.toFloat()
            val pageWidthPx: Float = pagerWidthPx - 2 * paddingPx
            val maxVisiblePages = pagerWidthPx / pageWidthPx
            val center = maxVisiblePages / 2f
            val scale: Float = if (position + 0.5f < center - 0.5f || position > center) {
                minScale
            } else {
                val coef: Float = if (position + 0.5f < center) {
                    (position + 1 - center) / 0.5f
                } else {
                    (center - position) / 0.5f
                }
                coef * (maxScale - minScale) + minScale
            }
            page.scaleX = scale
            page.scaleY = scale
        }




}