package com.example.integration

object BoutiqueUtil {


    fun validationPayement(
        x: Int,
        sum: Int,
    ): Boolean {
        if (sum > x || sum < 0 || x < 0) {
            return false
        }
        return true
    }


    fun recupEmail(
        mail: String
    ): Boolean {
        if (mail.isEmpty()) {
            return false
        }
        if (mail.contains(" ")){
            return false
        }
        if (mail.contains("@") || mail.contains(".")){
            return true
        }
        return true
    }


    fun panierCartModelPlusMinus(
        cartModelQuantity: Int
    ): Boolean {
        if (cartModelQuantity < 0) {
            return false
        }
        return true
    }

}
