package com.hope.igb.savvy.common.setappcontentwindow


import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.Constants
import com.hope.igb.savvy.common.views.BaseObservable
import com.hope.igb.savvy.databinding.WindowSetAppcontentBinding


class SetAppContentDialogViewMvc(inflater: LayoutInflater, private val appContents: ArrayList<String>) :
    BaseObservable<SetAppContentDialogViewMvc.Listener>() {

    interface Listener {
        fun onDialogClosedWith(newContents: ArrayList<String>?)
    }

    private val binding: WindowSetAppcontentBinding
    private var  dialog: AlertDialog


    init {
        binding = WindowSetAppcontentBinding.inflate(inflater)

        val builder: AlertDialog.Builder = AlertDialog.Builder(binding.root.context)

//        binding.root.setBackgroundResource(R.drawable.z_dialogs_background)

        builder.setView(binding.root)

        dialog = builder.create()




    }


    fun showSetAppContentDialog(){
        initClickableViews()
        checkCurrentSelectedContents()
        changeButtonTextInCaseOfEmpty()

        binding.cancel.setOnClickListener {
            dialog.dismiss()

            for (listener in getListeners()) listener.onDialogClosedWith(null)

        }

        val window = dialog.window

        window?.decorView?.setBackgroundResource(R.drawable.z_dialogs_background)
        window?.setLayout(LayoutParams.WRAP_CONTENT, (Constants.SCREEN_HEIGHT * 0.87).toInt())



        dialog.show()



    }




    private fun initClickableViews() {
        binding.addFunny.setOnClickListener { onAddFunnyClicked() }
        binding.addCutie.setOnClickListener{ onAddCutieClicked() }
        binding.addKittens.setOnClickListener{ onAddKittensClicked() }
        binding.addAnimals.setOnClickListener{ onAddAnimalsClicked() }
        binding.addSports.setOnClickListener{ onAddSportsClicked() }
        binding.addOthers.setOnClickListener{ onAddOthersClicked() }

        binding.setContent.setOnClickListener { onSetContentClicked() }
    }



    private fun checkCurrentSelectedContents() {
        for (content in appContents) {
            checkAContent(content)
        }
    }







    private fun onAddFunnyClicked() {
        if (!appContents.contains(Constants.CATEGORY_FUNNY))
            checkAContent(Constants.CATEGORY_FUNNY)
        else
            uncheckAContent(Constants.CATEGORY_FUNNY)



    }

    private fun onAddCutieClicked() {
        if (!appContents.contains(Constants.CATEGORY_CUTIE))
            checkAContent(Constants.CATEGORY_CUTIE)
        else
            uncheckAContent(Constants.CATEGORY_CUTIE)
    }

    private fun onAddKittensClicked() {
        if (!appContents.contains(Constants.CATEGORY_KITTENS))
            checkAContent(Constants.CATEGORY_KITTENS)
        else
            uncheckAContent(Constants.CATEGORY_KITTENS)
    }

    private fun onAddAnimalsClicked() {
        if (!appContents.contains(Constants.CATEGORY_ANIMALS))
            checkAContent(Constants.CATEGORY_ANIMALS)
        else
            uncheckAContent(Constants.CATEGORY_ANIMALS)
    }

    private fun onAddSportsClicked() {
        if (!appContents.contains(Constants.CATEGORY_SPORTS))
            checkAContent(Constants.CATEGORY_SPORTS)
        else
            uncheckAContent(Constants.CATEGORY_SPORTS)
    }

    private fun onAddOthersClicked() {
        if (!appContents.contains(Constants.CATEGORY_VARIOUS))
            checkAContent(Constants.CATEGORY_VARIOUS)
        else
            uncheckAContent(Constants.CATEGORY_VARIOUS)
    }

    private fun onSetContentClicked() {
        for (listener in getListeners())
            listener.onDialogClosedWith(appContents)

        dialog.dismiss()
    }



    private fun checkAContent(content: String) {
        when (content) {
            Constants.CATEGORY_FUNNY -> checkContentUi(binding.contentFunny,binding.funny, binding.addFunny)
            Constants.CATEGORY_CUTIE -> checkContentUi(binding.contentCutie,binding.cutie, binding.addCutie)
            Constants.CATEGORY_KITTENS -> checkContentUi(binding.contentKittens,binding.kittens, binding.addKittens)
            Constants.CATEGORY_ANIMALS -> checkContentUi(binding.contentAnimals,binding.animals, binding.addAnimals)
            Constants.CATEGORY_SPORTS -> checkContentUi(binding.contentSports,binding.sports, binding.addSports)
            Constants.CATEGORY_VARIOUS -> checkContentUi(binding.contentOthers,binding.others, binding.addOthers)

            else -> return
        }
        appContents.add(content)
        changeButtonTextInCaseOfEmpty()
    }

    private fun checkContentUi(contentView: LinearLayout, contentText: TextView, addContent: ImageView) {
        contentView.setBackgroundResource(R.drawable.s_select_category_add_contentbg)
        contentText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.darkest_color))
        addContent.setImageResource(R.drawable.s_select_category_removed)
    }

    private fun uncheckAContent(content: String) {
        when (content) {
            Constants.CATEGORY_FUNNY -> uncheckContentUi(binding.contentFunny,binding.funny, binding.addFunny)
            Constants.CATEGORY_CUTIE -> uncheckContentUi(binding.contentCutie,binding.cutie, binding.addCutie)
            Constants.CATEGORY_KITTENS -> uncheckContentUi(binding.contentKittens,binding.kittens, binding.addKittens)
            Constants.CATEGORY_ANIMALS -> uncheckContentUi(binding.contentAnimals,binding.animals, binding.addAnimals)
            Constants.CATEGORY_SPORTS -> uncheckContentUi(binding.contentSports,binding.sports, binding.addSports)
            Constants.CATEGORY_VARIOUS -> uncheckContentUi(binding.contentOthers,binding.others, binding.addOthers)

            else ->return
        }
        appContents.remove(content)
        changeButtonTextInCaseOfEmpty()

    }
    private fun uncheckContentUi(contentView: LinearLayout, contentText: TextView, addContent: ImageView) {
        contentView.setBackgroundResource(R.drawable.a_welcome_skipbg)
        contentText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white_color))
        addContent.setImageResource(R.drawable.s_select_category_addd)
    }

    private fun changeButtonTextInCaseOfEmpty(){
        if (appContents.isEmpty())
            binding.setContent.text = binding.root.context.getString(R.string.skip_app_content)
        else
            binding.setContent.text = binding.root.context.getString(R.string.set_app_content)

    }







}