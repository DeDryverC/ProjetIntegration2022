package com.example.integration.adapter

import android.content.Intent
import com.example.integration.EnterTicketNumberUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*


class EnterTicketNumberTest {

    @Test
    fun `"le retour est bloqué" return true`(){
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val result = EnterTicketNumberUtil.avoidFreePoints(intent)
        assertThat(result).isTrue()
    }

    @Test
    fun `"le format de date est bon" return true`() {
        val dateFormat = SimpleDateFormat("dd/M/yyyy");
        val date = dateFormat.format(Date());
        val result = EnterTicketNumberUtil.verifyDateFormat(date)
        assertThat(result).isTrue()
    }

}