package cstjean.mobile2.demofilms.network

import retrofit2.http.Query
import retrofit2.http.GET

interface OmdbApiService {

    // Reconstitution d'URL : https://www.omdbapi.com/?apikey=...&t=Inception
    @GET("/")
    suspend fun chercherFilm(
        @Query("t") titre: String,
        @Query("apikey") apiKey: String
    ): OmdbFilmDto
}