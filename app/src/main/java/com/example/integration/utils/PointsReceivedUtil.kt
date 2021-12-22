package com.example.integration

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Document
import java.util.*

object PointsReceivedUtil {


    fun avoidFreePoints(intent: Intent): Boolean {
        return intent.flags == FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK;
    }

    fun checkDatabase(documentReference: DocumentReference): Boolean{
        val docRef = FirebaseFirestore.getInstance().collection("clients").document("paca1712@gmail.com");
        return docRef == documentReference
    }




}
