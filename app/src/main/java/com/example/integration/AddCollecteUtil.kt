package com.example.integration

object AddCollecteUtil {

    private val existingCollecte = listOf("Collecte LLN", "Collecte B-L-C")

    /**
     * input not valid if...
     * inputs empty
     * name already taken
     * date not valid
     * heure not valid
     * description's length less than 10
     */

    fun validateAddCollecteInput(
        name: String,
        description: String,
        localisation: String,
        organisateur: String,
        imageUrl: String,
        date: String,
        heure: String
    ): Boolean {
        if (name.isEmpty() || description.isEmpty() || localisation.isEmpty() ||
            organisateur.isEmpty() || imageUrl.isEmpty() || date.isEmpty() ||
            heure.isEmpty()
        ) {
            return false
        }
        if (name in existingCollecte) {
            return false
        }
        if (":" !in heure.split("")) {
            return false
        }
        if (heure.split(":")[0].toInt() > 23 || heure.split(":")[0].toInt() < 0 ||
            heure.split(":")[1].toInt() > 59 || heure.split(":")[1].toInt() < 0){
            return false
        }
        if("/" !in date.split("")){
            return false
        }
        if (date.split("/")[0].toInt() > 31 || date.split("/")[0].toInt() < 0 ||
            date.split("/")[1].toInt() > 12 || date.split("/")[1].toInt() < 0){
            return false
        }
        if(description.count() < 10){
            return false
        }


        return true
    }

}