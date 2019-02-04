package com.winnicki.businesssearch.network

import com.winnicki.businesssearch.model.Business
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface YelpApiInterface {

    @GET("search")
    fun search(
        @Header("Authorization") apiKey: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("term") searchTerm: String?
    ): Single<Response>

    @GET("{id}")
    fun getBusinessDetails(
        @Header("Authorization") apiKey: String,
        @Path("id") id: String
    ): Observable<Business>
}

data class Response(
    val businesses: List<Business>,
    val total: Int
)