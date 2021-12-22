package com.example.integration


import android.content.Intent
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Test

class PointsReceivedTest {

    @Test
    fun `"le retour est bloqué" return true`(){
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val result = PointsReceivedUtil.avoidFreePoints(intent)
        assertThat(result).isTrue()
    }

    @Test
    fun `"la collection est bien récupérée" return true`(){
        val collect = FirebaseFirestore.getInstance().collection("clients").document("paca1712@gmail.com");
        val result = PointsReceivedUtil.checkDatabase(collect)
    }

}