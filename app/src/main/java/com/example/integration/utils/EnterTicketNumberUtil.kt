package com.example.integration

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.widget.Spinner
import java.text.SimpleDateFormat
import java.util.*

object EnterTicketNumberUtil {
    fun avoidFreePoints(intent: Intent): Boolean {
        return intent.flags == FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK;
    }

    fun verifyDateFormat(date: String): Boolean{
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val sdf_date = sdf.format(Date())
        return sdf_date == date;
    }
}
