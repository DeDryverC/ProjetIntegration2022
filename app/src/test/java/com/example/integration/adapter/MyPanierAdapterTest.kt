package com.example.integration.adapter

import com.example.integration.BoutiqueUtil
import com.google.common.truth.Truth
import org.junit.Test

class MyPanierAdapterTest {

    // Tests sur l'ajout ou le retrait d'éléments dans le panier
    @Test
    fun `"nombre d'éléments dans panier egal 0" return true`(){
        val result = BoutiqueUtil.panierCartModelPlusMinus(
            cartModelQuantity = 0
        )
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `"nombre d'éléments dans panier positif" return true`(){
        val result = BoutiqueUtil.panierCartModelPlusMinus(
            cartModelQuantity = 2
        )
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `"nombre d'éléments dans panier negatif" return false`(){
        val result = BoutiqueUtil.panierCartModelPlusMinus(
            cartModelQuantity = -1
        )
        Truth.assertThat(result).isFalse()
    }
}