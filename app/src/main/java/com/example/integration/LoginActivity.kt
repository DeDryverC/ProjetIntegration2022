package com.example.integration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.example.integration.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Login/logout-related properties
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null

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

    private fun onConnection() {

        val intent = Intent(this, MapsActivity::class.java)
        // start your next activity
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
                    onConnection()

                }
            })
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