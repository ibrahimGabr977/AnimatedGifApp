package com.hope.igb.savvy.screens.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.views.BaseViewMvc

class WelcomeItemViewMvc(inflater: LayoutInflater, parent: ViewGroup) : BaseViewMvc() {

    private val title: TextView
    private  val content: TextView
    private val imageView: ImageView


    init {
        setRootView(inflater.inflate(R.layout.activity_welcome_adapter, parent, false))
        imageView = findViewById(R.id.welcome_adapter_image)
        title = findViewById(R.id.welcome_adapter_title_text)
        content = findViewById(R.id.welcome_adapter_content_text)
    }


     fun bindView(imageRes: Int, title: String?, content: String?) {
        imageView.setImageResource(imageRes)
        this.title.text = title
        this.content.text = content
    }
}