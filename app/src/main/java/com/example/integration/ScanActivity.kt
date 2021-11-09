package com.example.integration

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_scan_ticket.*
import java.util.*



private const val CAMERA_REQUEST_CODE = 101;

class ScanActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner;



    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ticket);

        setupPermissions();
        codeScanner();
    }

    private fun codeScanner(){
        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.apply{
            camera = CodeScanner.CAMERA_BACK;
            formats = CodeScanner.ALL_FORMATS;

            autoFocusMode = AutoFocusMode.SAFE;
            scanMode = ScanMode.CONTINUOUS;
            isAutoFocusEnabled = true;
            isFlashEnabled = false;

            decodeCallback = DecodeCallback {
                runOnUiThread{
                    tv_textView.text = it.text;
                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread{
                    Log.e("Main", "Erreur d'initialisation de la camera : ${it.message}");
                }
            }

            scanner_view.setOnClickListener{
                codeScanner.startPreview();
            }
        }
    }

    override fun onResume(){
        super.onResume();
        codeScanner.startPreview();
    }

    override fun onPause(){
        codeScanner.releaseResources();
        super.onPause();
    }

    private fun setupPermissions(){
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest();
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Vous avez besoin de la camera pour faire fonctionner le scan!", Toast.LENGTH_SHORT).show();
                } else{
                    //ca a fonctionné
                }
            }
        }
    }

}