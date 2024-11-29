package data.Remote

import data.model.LoginRequest
import data.model.RegisterRequest
import data.model.Userdata
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/users/login")
    fun login(@Body loginRequest: LoginRequest): Call<Userdata>
    @POST("api/users")
    fun register(@Body RegisterRequest: RegisterRequest):Call<Userdata>
}
