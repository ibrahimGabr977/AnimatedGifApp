package com.hope.igb.savvy.screens.main.reportpostswindow

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.Constants.MAX_REPORT_INFO_LENGTH
import com.hope.igb.savvy.common.views.BaseObservableViewMvc


@SuppressLint("InflateParams")
class ReportPostDialogViewMvc(inflater: LayoutInflater, private val postID: String) :
    BaseObservableViewMvc<ReportPostDialogViewMvc.Listener>(), TextWatcher {
    interface Listener {
        fun onReportButtonClicked(postID: String, reportReason: String, additionalInfo: String)

    }

    private val  dialog: AlertDialog
    private val characterCountTextView: TextView
    private val additionalInfo: EditText
    private val reportingOptions: RadioGroup
    private var isAnyOptionSelected = false
    private val reportButton: TextView


    init {
        setRootView(inflater.inflate(R.layout.window_reporting, null, false))

        val builder: AlertDialog.Builder = AlertDialog.Builder(getContext())

        getRootView().setBackgroundResource(android.R.drawable.list_selector_background)

        builder.setView(getRootView())

        dialog = builder.create()

        reportingOptions = findViewById(R.id.reportRadioGroup)

        additionalInfo = findViewById(R.id.additional_info_edittext)

        characterCountTextView = findViewById(R.id.character_count_textview)

        reportButton = findViewById(R.id.reporting_report)

    }

    fun showReportOptionsDialog(){


        reportingOptions.setOnCheckedChangeListener { _, p1 ->
            onCheckedOptionsChange(p1)
        }

        additionalInfo.setText(getContext().getString(R.string.report_post_additional_info_character_count, 0))

        additionalInfo.addTextChangedListener(this)

        findViewById<TextView>(R.id.reporting_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun onCheckedOptionsChange(selectedOptionID: Int) {
        val radioButton = findViewById<RadioButton>(selectedOptionID)

        if (!isAnyOptionSelected) {
            reportButton.isEnabled = true
            reportButton.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_color))
            reportButton.setOnClickListener { onReportClicked(radioButton.text.toString()) }
            isAnyOptionSelected = true
        }
    }

    private fun onReportClicked(reportReason: String) {
        for (listener in getListeners())
            listener.onReportButtonClicked(postID, reportReason, additionalInfo.text.toString())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        val remainingChars: Int = MAX_REPORT_INFO_LENGTH - p0?.length!!
        characterCountTextView.text = getContext().getString(
            R.string.report_post_additional_info_character_count,
            remainingChars
        )

        if (p0.length > MAX_REPORT_INFO_LENGTH) {
            val trimmedText: String =
                p0.toString().substring(0, MAX_REPORT_INFO_LENGTH)
            additionalInfo.setText(trimmedText)
            additionalInfo.setSelection(trimmedText.length)
        }
    }

    override fun afterTextChanged(p0: Editable?) {}


    fun dismissReportDialog(){
        dialog.dismiss()
    }


}



