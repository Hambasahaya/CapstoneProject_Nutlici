package com.example.nutlici.UI.View


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nutlicii.R
import data.local.db.AppDatabase
import data.Remote.NutliciiBaseApi
import data.model.LoginRequest
import data.model.Userdata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        appDatabase = AppDatabase.getDatabase(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)

        btnSignIn.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                sendLoginData(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendLoginData(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        val apiService = NutliciiBaseApi.getApiService()

        apiService.login(loginRequest).enqueue(object : Callback<Userdata> {
            override fun onResponse(call: Call<Userdata>, response: Response<Userdata>) {
                if (response.isSuccessful) {
                    val user = response.body()

                    if (user != null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            appDatabase.userDao().insertUser(user)
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.putExtra("user_data", user)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        showErrorMessage("Login failed: No user data received.")
                    }
                } else {
                    showErrorMessage("Login failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Userdata>, t: Throwable) {
                showErrorMessage("Login failed: ${t.message}")
            }
        })
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
