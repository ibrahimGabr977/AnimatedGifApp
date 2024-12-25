package com.hope.igb.savvy.util

import java.text.SimpleDateFormat
import java.util.*

class DateFormatBuilder {

    private val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val objectTime = Calendar.getInstance()
    private lateinit var now: Calendar


    fun getPostDate(serverDate: Long): String {
        objectTime.timeInMillis = serverDate
        now = Calendar.getInstance()



        return if (now[Calendar.YEAR] != objectTime[Calendar.YEAR]) {
            sdf.applyPattern("MMM, yyyy")
            sdf.format(objectTime.time).toString()

        } else if (now[Calendar.MONTH] - objectTime[Calendar.MONTH] == 1) {
            sdf.applyPattern(" dd")
            "Last Month" + sdf.format(objectTime.time).toString()

        } else if (now[Calendar.MONTH] - objectTime[Calendar.MONTH] > 1) {
            sdf.applyPattern("dd MMMM")
            sdf.format(objectTime.time).toString()

        } else if (now[Calendar.WEEK_OF_MONTH] != objectTime[Calendar.WEEK_OF_MONTH]) {
            sdf.applyPattern(" dd")
            "This Month" + sdf.format(objectTime.time).toString()

        } else if (now[Calendar.DAY_OF_WEEK] - objectTime[Calendar.DAY_OF_WEEK] > 1) {
            sdf.applyPattern("EEEE")
            sdf.format(objectTime.time).toString()

        } else if (now[Calendar.DAY_OF_WEEK] - objectTime[Calendar.DAY_OF_WEEK] == 1) {
            sdf.applyPattern(" HH:mm")
            "Yesterday" + sdf.format(objectTime.time)

        } else if (now[Calendar.HOUR_OF_DAY] - objectTime[Calendar.HOUR_OF_DAY] == 1)
            "Last Hour"
        else if (now[Calendar.HOUR_OF_DAY] - objectTime[Calendar.HOUR_OF_DAY] > 1) {
            sdf.applyPattern(" HH:mm a")
            "Today" + sdf.format(objectTime.time)

        } else if (now[Calendar.MINUTE] != objectTime[Calendar.MINUTE])
            (now[Calendar.MINUTE] - objectTime[Calendar.MINUTE]).toString() + " Min ago"
        else
            "now"
    }
}