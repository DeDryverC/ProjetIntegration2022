package com.example.integration

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {
    //@SuppressLint("InflateParams")

    override fun getInfoContents(marker: Marker?): View? {
        val view= LayoutInflater.from(context).inflate(
            R.layout.marker_info_contents, null
        )
        println(marker)
        if (marker != null) {
            view.findViewById<TextView>(R.id.nom_du_depot).text = marker.title
            view.findViewById<TextView>(R.id.description_depot).text = marker.snippet
        }
        return view
    }


    override fun getInfoWindow(marker: Marker?): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }
}