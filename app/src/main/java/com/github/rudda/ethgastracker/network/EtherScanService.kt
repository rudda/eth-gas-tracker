package com.github.ethgastracker.network

import retrofit2.Call
import retrofit2.http.GET

interface EtherScanService {
    @GET("gasTracker")
    fun gasTracker(): Call<String>
}