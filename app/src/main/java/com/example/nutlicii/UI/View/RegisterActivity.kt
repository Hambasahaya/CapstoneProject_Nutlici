package com.example.nutlici.UI.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nutlicii.R
import com.example.nutlicii.UI.View.HomeActivity
import com.example.nutlicii.data.model.ApiResponse
import com.google.gson.Gson
import data.Remote.NutliciiBaseApi
import data.local.db.AppDatabase
import data.model.RegisterRequest
import data.model.Userdata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        appDatabase = AppDatabase.getDatabase(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etRepeatPassword = findViewById<EditText>(R.id.etRepeatPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val name = etName.text.toString()
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val repeatPassword = etRepeatPassword.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()) {
                if (password == repeatPassword) {
                    sendRegisterData(name, username, email, password, repeatPassword)
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendRegisterData(
        name: String,
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ) {
        val registerRequest = RegisterRequest(username, password, repeatPassword, name, email)
        val apiService = NutliciiBaseApi.getApiService()
        apiService.register(registerRequest).enqueue(object : Callback<ApiResponse<Userdata>> {
            override fun onResponse(call: Call<ApiResponse<Userdata>>, response: Response<ApiResponse<Userdata>>) {
                val apiResponse = response.body()
                val user = apiResponse?.data
                if (user != null) {
                    showErrorMessage("Register successful")
                    lifecycleScope.launch(Dispatchers.IO) {
                        appDatabase.userDao().insertUser(user)
                        val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                        intent.putExtra("user_data", user)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = parseErrorMessage(errorBody)
                    showErrorMessage("Register failed: $errorMessage")
                }
            }
            override fun onFailure(call: Call<ApiResponse<Userdata>>, t: Throwable) {
                showErrorMessage("Register failed: ${t.message}")
            }
        })
    }
    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            val gson = Gson()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            if (!errorResponse.errors.isNullOrEmpty()) {
                errorResponse.errors
            } else {
                "Error message not provided by server"
            }
        } catch (e: Exception) {
            if (!errorBody.isNullOrEmpty()) {
                "Server response: $errorBody"
            } else {
                "Unknown error occurred"
            }
        }
    }
}

data class ErrorResponse(
    val errors: String?
)