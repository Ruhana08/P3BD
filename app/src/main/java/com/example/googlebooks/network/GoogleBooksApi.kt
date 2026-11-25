package com.example.googlebooks.network

import com.example.googlebooks.data.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchVolumes(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") apiKey: String
    ): BooksResponse
}