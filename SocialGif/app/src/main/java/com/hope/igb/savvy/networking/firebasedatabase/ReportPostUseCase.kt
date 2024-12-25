package com.hope.igb.savvy.networking.firebasedatabase

import com.google.firebase.database.DatabaseReference
import com.hope.igb.savvy.common.Constants.CONST_TIME_MILLIS
import com.hope.igb.savvy.networking.models.ReportModel

class ReportPostUseCase(private val reference: DatabaseReference) {




    fun reportPost(postID: String, reportReason: String, additionalInfo: String) {

        val reportID = "" + (CONST_TIME_MILLIS - System.currentTimeMillis())

        val reportModel = ReportModel(reportID, postID, reportReason, additionalInfo)

        reference.child("reports")
            .child(reportID)
            .setValue(reportModel)
    }
}