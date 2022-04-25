package com.github.ethgastracker.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class EtherScanNetwork {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://etherscan.io/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    fun getService(): EtherScanService =  retrofit.create(EtherScanService::class.java)

}