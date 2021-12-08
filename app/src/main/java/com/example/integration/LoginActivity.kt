package com.example.integration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.integration.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import kotlinx.android.synthetic.main.activity_boutique.*
import java.lang.StringBuilder

import java.time.LocalDateTime
import java.util.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Login/logout-related properties
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null
    private val db = Firebase.firestore

    private var mail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener { login() }


    }

    private fun onConnection(text: String?) {

        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("key",text)
        startActivity(intent)
    }

    private fun login() {
        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope(getString(R.string.login_scopes))
            .withAudience(getString(R.string.login_audience, getString(R.string.com_auth0_domain)))
            .start(this, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(getString(R.string.login_failure_message, error.getCode()))
                }

                override fun onSuccess(result: Credentials) {
                    cachedCredentials = result
                    showSnackBar(getString(R.string.login_success_message, result.accessToken))
                    showUserProfile()
                }
            })

    }
    private fun showUserProfile() {
        // Guard against showing the profile when no user is logged in
        if (cachedCredentials == null) {
            return
        }

        val client = AuthenticationAPIClient(account)
        client
            .userInfo(cachedCredentials!!.accessToken!!)
            .start(object : Callback<UserProfile, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile
                    mail=intent.getStringExtra("key").toString()
                    db.collection("clients").document(profile?.email.toString())
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                onConnection(profile?.email)
                            }
                            else {
                                createUser(profile?.email)
                                onConnection(profile?.email)
                            }
                            }
                        .addOnFailureListener{

                        }
                }

            })
    }

    private fun createUser(text: String?) {
        db.collection("clients").document("$text").get()
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                val date = LocalDateTime.now()
                val user = hashMapOf(
                    "email" to "$text",
                    "lastConnect" to "$date",
                    "points" to 1
                )
                db.collection("clients").document("$text")
                    .set(user)
            }
            }


    private fun showSnackBar(text: String) {
        Snackbar
            .make(
                binding.root,
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }

}