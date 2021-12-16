package com.example.integration

import android.net.Uri
import org.junit.Assert
import org.junit.Test


class CollecteUnitTest {

    private val collecte = CollecteModel(
        "Voici la description", "Collecte1", "", true, "Louvain-la-Neuve", "Collecte à LLN",
        "Arthur", "12/12", "18:00")

    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2)
    }
    @Test
    fun creation_collecte_test(){
        Assert.assertNotNull(collecte)
    }
    @Test
    fun recup_description_test(){
        equals(collecte.description == "Voici la description")
    }
    @Test
    fun recup_id_test() {
        equals(collecte.id == "Collecte1")
    }
    @Test
    fun recup_liked_test(){
        Assert.assertTrue(collecte.liked)
    }
    @Test
    fun recup_unliked_test(){
        Assert.assertFalse(!collecte.liked)
    }
    @Test
    fun recup_localisation_test(){
        equals(collecte.localisation == "Louvain-la-Neuve")
    }
    @Test
    fun recup_nom_test(){
        equals(collecte.nom == "Collecte à LLN")
    }
    @Test
    fun recup_organisateur_test(){
        equals(collecte.organisateur == "Louvain-la-Neuve")
    }
    @Test
    fun recup_date_test(){
        equals(collecte.date == "12/12")
    }
    @Test
    fun recup_heure_test() {
        equals(collecte.heure == "18:00")
    }
}