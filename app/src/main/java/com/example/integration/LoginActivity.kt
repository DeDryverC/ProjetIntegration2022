package com.example.integration

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.integration.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


import java.time.LocalDateTime


open class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    // Login/logout-related properties
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null
    private val db = Firebase.firestore

    var mail = ""

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

    fun onConnection(text: String?) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("key", text)
        startActivity(intent)

    }

    fun login(): Boolean {
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
                    showUserProfile()

                }
            })
        return true
    }

    fun showUserProfile(): String {
        // Guard against showing the profile when no user is logged in
        if (cachedCredentials == null) {
            return ""
        }

        val client = AuthenticationAPIClient(account)
        client
            .userInfo(cachedCredentials!!.accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    showSnackBar(
                        getString(
                            R.string.general_failure_with_exception_code,
                            error.getCode()
                        )
                    )
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onSuccess(result: UserProfile) {
                    cachedUserProfile = result
                    mail = intent.getStringExtra("key").toString()
                    db.collection("clients").document(result.email.toString())
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                onConnection(result.email)
                            } else {
                                createUser(result.email)
                                onConnection(result.email)
                            }
                        }
                        .addOnFailureListener {

                        }
                }

            })
        return account.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createUser(text: String?) {

        db.collection("clients").document("$text").get()
            .addOnSuccessListener {
                    exception ->
                val date = LocalDateTime.now()
                val name = "$text".replaceAfter("@", "").replace("@", "")
                val user = hashMapOf(
                    "email" to "$text",
                    "name" to "$name",
                    "moderator" to false,
                    "lastConnect" to "$date",
                    "points" to 0
                )
                db.collection("clients").document("$text")
                    .set(user)

            }
            .addOnFailureListener { exception ->
                val date = LocalDateTime.now()
                val name = "$text".replaceAfter("@", "").replace("@", "")
                val user = hashMapOf(
                    "email" to "$text",
                    "name" to "$name",
                    "moderator" to false,
                    "lastConnect" to "$date",
                    "points" to 0
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
