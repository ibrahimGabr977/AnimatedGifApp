package com.hope.igb.savvy.networking.models


class ReportModel(val reportID: String,
                  val reportedPostID: String,
                  val reportedReason: String,
                  val additionalInfo: String) {

    constructor() : this("","","","")


}