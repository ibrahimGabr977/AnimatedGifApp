package com.hope.igb.savvy.common.customtabslayout


import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.views.BaseObservableViewMvc




class TagsFilteringTabLayout(rootView: View) :
    BaseObservableViewMvc<TagsFilteringTabLayout.Listener>(),
    View.OnClickListener {


    interface Listener {
        fun onTagSelected(tag_id: Int)
    }

    companion object {
        private var last_selected_tab: Int = R.id.tab_all
    }


    private val tabs: HashMap<Int, TextView> = HashMap(2)


    init {
        setRootView(rootView)

        initTabs()

    }


    private fun initTabs() {
        tabs[R.id.tab_all] = findViewById(R.id.tab_all)
        tabs[R.id.tab_trending] = findViewById(R.id.tab_trending)



        tabSelectedUiChanges(last_selected_tab)


        for (tab_id in tabs.keys)
            tabs[tab_id]!!.setOnClickListener(this)
    }


    private fun tabSelectedUiChanges(selected_tab: Int) {

        val tab = tabs[selected_tab]
        if (tab != null) {
            tab.setBackgroundResource(R.drawable.b_main_selected_tagbg)
            tab.setTextColor(ContextCompat.getColor(getContext(), R.color.darker_color))
            tab.compoundDrawablesRelative[0].setTint(
                ContextCompat.getColor(
                    getContext(),
                    R.color.darker_color
                )
            )
        }
        if (selected_tab != last_selected_tab)
            lastSelectedTabUiChanges()
        last_selected_tab = selected_tab

    }

    private fun lastSelectedTabUiChanges() {
        val tab = tabs[last_selected_tab]
        if (tab != null) {
            tab.setBackgroundResource(R.drawable.b_main_unselected_tagbg)
            tab.setTextColor(ContextCompat.getColor(getContext(), R.color.white_color))
            tab.compoundDrawablesRelative[0].setTint(
                ContextCompat.getColor(
                    getContext(),
                    R.color.white_color
                )
            )

        }
    }

    override fun onClick(v: View) {
        if (v.id != last_selected_tab) {
            tabSelectedUiChanges(v.id)

            for (listener in getListeners()) listener.onTagSelected(v.id)
        }

    }

    fun check(tab_id: Int) {
        tabSelectedUiChanges(tab_id)
    }

    fun hide() {
        getRootView().visibility = View.GONE
    }

    fun show(){
        getRootView().visibility = View.VISIBLE
    }


}