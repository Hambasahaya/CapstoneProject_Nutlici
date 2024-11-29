package data.Remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NutliciiBaseApi {
    private const val BASE_URL = "https://my-nest-app-586910673966.asia-southeast2.run.app/"

    fun getApiService(): ApiService {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // Mengembalikan instance dari ApiService
        return retrofit.create(ApiService::class.java)
    }
}
