package com.winnicki.businesssearch.network

import android.location.Location
import com.winnicki.businesssearch.model.Business
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class YelpApiClient {

    private val yelpApiInterface = getRetrofitClient().create(YelpApiInterface::class.java)

    fun searchBusinesses(searchTerm: String?, location: Location): Single<Response> =
        yelpApiInterface.search(API_KEY, location.latitude, location.longitude, searchTerm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getBusinessDetails(id: String): Observable<Business> =
        yelpApiInterface.getBusinessDetails(API_KEY, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    companion object {
        private const val BASE_URL = "https://api.yelp.com/v3/businesses/"
        const val API_KEY =
            "Bearer oT3pjjW0JpIuEJ-_003lLHTwoDCSLzbVZKug7BRTxbW9slmIep3yH_HncyQV_zPGt3zits6vk7_eQVqN0JEkZ1KW5IF5TbOCwn-1tf5B2-enx60jDC6qs1D7igL2W3Yx"

        fun getRetrofitClient(): Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
                )
                .build()
    }
}
