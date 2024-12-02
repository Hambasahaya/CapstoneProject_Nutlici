package data.Remote

import com.example.nutlicii.data.model.ApiResponse
import data.model.LoginRequest
import data.model.RegisterRequest
import data.model.Userdata
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/users/login")
    fun login(@Body loginRequest: LoginRequest): Call<ApiResponse<Userdata>>
    @POST("auth/register")
    fun register(@Body RegisterRequest: RegisterRequest):Call<ApiResponse<Userdata>>
}
