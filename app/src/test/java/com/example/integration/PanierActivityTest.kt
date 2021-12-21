package com.example.integration

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PanierActivityTest {

    // Tests sur le payement dans la partie panier de la boutique

    @Test
    fun `"soustraction payement egale 0" return true`(){
        val result = BoutiqueUtil.validationPayement(
            x = 100,
            sum = 100
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `"soustraction payement plus grand que 0" return true`(){
        val result = BoutiqueUtil.validationPayement(
            x = 100,
            sum = 90
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `"soustraction payement = points négatifs" return false`(){
        val result = BoutiqueUtil.validationPayement(
            x = 100,
            sum = 110
        )
            assertThat(result).isFalse()
    }

    @Test
    fun `"sum et x négatif" return false`(){
        val result = BoutiqueUtil.validationPayement(
            x = -100,
            sum = -100
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `"sum positif et x negatif" return false`(){
        val result = BoutiqueUtil.validationPayement(
            x = -100,
            sum = 100
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `"sum negatif et x positif" return false`(){
        val result = BoutiqueUtil.validationPayement(
            x = 100,
            sum = -100
        )
        assertThat(result).isFalse()
    }

}