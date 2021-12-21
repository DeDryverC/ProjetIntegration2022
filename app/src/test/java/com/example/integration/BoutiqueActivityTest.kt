package com.example.integration


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BoutiqueActivityTest {

    // Tests sur la validitée de l'email récupéré

    @Test
    fun `"email est bien un string" return true`(){
        val result = BoutiqueUtil.recupEmail(
        mail = "louiscarlier123@gmail.com"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `"email contient un blanc" return false`(){
        val result = BoutiqueUtil.recupEmail(
            mail = "louiscarlier123@gmail.com "
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `"email contient un point et un @" return true`(){
        val result = BoutiqueUtil.recupEmail(
            mail = "@."
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `"email est vide" return false`(){
        val result = BoutiqueUtil.recupEmail(
            mail = ""
        )
        assertThat(result).isFalse()
    }


}