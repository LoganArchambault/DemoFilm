package cstjean.mobile2.demofilms.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://www.omdbapi.com/"

    private val json = Json {
        ignoreUnknownKeys = true // Ignore les champs JSON non déclarés dans le DTO
    }

    val api: OmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(OmdbApiService::class.java)
    }
}