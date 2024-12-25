package com.hope.igb.savvy.screens.welcome

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.hope.igb.savvy.common.presistData.SharedData
import com.hope.igb.savvy.common.setappcontentwindow.SetAppContentDialogViewMvc
import com.hope.igb.savvy.screens.BaseActivity

class WelcomeActivity : AppCompatActivity(), WelcomeViewMvc.WelcomeListener,
    SetAppContentDialogViewMvc.Listener {
    private lateinit var viewMvc: WelcomeViewMvc
    private lateinit var sharedData: SharedData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)


        sharedData = SharedData(this)

//        if (!sharedData.isNewUser()) {
//            startTheApplication()
//
//        }else{
//            viewMvc = WelcomeViewMvc(LayoutInflater.from(this))
//            setContentView(viewMvc.getRootView())
//        }


        viewMvc = WelcomeViewMvc(LayoutInflater.from(this))
        setContentView(viewMvc.getRootView())

        setFullScreen()


//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

        viewMvc.bindWelcomeView()
    }


    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)


    }


    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)

    }

    override fun onGetStartedClicked() {


        val setAppContentDialogViewMvc = SetAppContentDialogViewMvc(layoutInflater, ArrayList())
        setAppContentDialogViewMvc.registerListener(this)
        setAppContentDialogViewMvc.showSetAppContentDialog()


    }

    override fun onDialogClosedWith(newContents: ArrayList<String>?) {

        sharedData.saveNewAppContents(newContents ?: ArrayList())

        startTheApplication()
    }


    private fun startTheApplication() {


        val intent = Intent(this@WelcomeActivity, BaseActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }



    @Suppress("DEPRECATION")
    private fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }


}