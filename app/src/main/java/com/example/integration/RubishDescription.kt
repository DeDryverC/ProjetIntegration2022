package com.example.integration

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class RubishDescription : AppCompatActivity() {
    //fun onLoad() {Toast.makeText(this, "msg", Toast.LENGTH_LONG).show()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rubish_description_page)
        setupComponents()

    }

    private fun setupComponents() {
        val markerTitle = intent.getStringExtra("MARKER_TITLE")
        val markerDescription = intent.getStringExtra("MARKER_DESCRIPTION")

        findViewById<TextView>(R.id.rubish_title_content).text = markerTitle
        findViewById<TextView>(R.id.rubish_description_content).text = markerDescription
    }
}