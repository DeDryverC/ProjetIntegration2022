package com.example.integration

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class RubishCreationForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rubish_creation_form)
        setSpinnerView()
        setButtonAction()

    }

    private fun setSpinnerView() {
        val spinner: Spinner = findViewById(R.id.spinner_amount_rubish)
        ArrayAdapter.createFromResource(
            this,
            R.array.amount_rubish_value,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun setButtonAction() {
        val imageButton = findViewById<Button>(R.id.rubish_upload_button)
        val sendButton = findViewById<Button>(R.id.confirm_rubish_button)
        imageButton.setOnClickListener {
            uploadImg()
        }
        sendButton.setOnClickListener {
            addRubish()
        }
    }

    private fun uploadImg() {
        TODO("Not yet implemented")
    }

    private fun addRubish() {
        TODO("Not yet implemented")

    }


}