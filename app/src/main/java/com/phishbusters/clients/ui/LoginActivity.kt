package com.phishbusters.clients.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.phishbusters.clients.BuildConfig
import com.phishbusters.clients.R
import com.phishbusters.clients.network.ApiService
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val LOG_TAG = "LoginActivity"
    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPassword = findViewById<TextView>(R.id.forgotPasswordLink)
        val register = findViewById<TextView>(R.id.registerLink)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            apiService.httpCall("${BuildConfig.API_URL}/login", "POST", JSONObject()
                .put("email", username)
                .put("password", password)
                .toString(), object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            throw IOException("Unexpected code $response")
                        }

                        val jwtToken = JSONObject(response.body!!.string()).getString("token")
                        Log.v(LOG_TAG, jwtToken);
                    }
                }
            })
        }

        forgotPassword.setOnClickListener {
            Toast.makeText(this, "Perdi contrasena", Toast.LENGTH_SHORT).show()
        }

        register.setOnClickListener {
            Toast.makeText(this, "Registrarse", Toast.LENGTH_SHORT).show()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Este método se llama antes de que el texto cambie
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Este método se llama cuando el texto cambia
                validateInput(usernameEditText, passwordEditText)
            }

            override fun afterTextChanged(s: Editable) {
                // Este método se llama después de que el texto cambia
            }
        }

        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
    }

    private fun validateInput(usernameEditText: EditText, passwordEditText: EditText) {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isEmpty()) {
            usernameEditText.error = "El usuario no puede estar vacío"
        } else {
            usernameEditText.error = null
        }

        if (password.isEmpty()) {
            passwordEditText.error = "La contraseña no puede estar vacía"
        } else {
            passwordEditText.error = null
        }
    }
}