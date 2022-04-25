package com.github.ethgastracker.model

import android.util.Log
import com.github.ethgastracker.data.Gas
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*
import kotlin.collections.HashMap

class EtherScanGasTrackerBuilder (val document : String) {
    var mGweiHashMap = HashMap<Gas.GweiSpeed, Gas.Gwei>()
    lateinit var mJsoupDocument: Document
    val TAG = "ETH_ScanGS_Builder"
    val AVG_PRICE_ID = "spanAvgPrice"
    val LOW_PRICE_ID = "spanLowPrice"
    val HIGH_PRICE_ID = "spanHighPrice"
    val LAST_UPDATED_ID = "ct"

    init {
        mJsoupDocument = Jsoup.parse(this.document)
    }

    private fun getUnknownGwei() = Gas.Gwei(0, 0, Gas.GweiSpeed.UNKNOWN, 0)

    private  fun extractGweiPrice(id: String): Int {
        var price = mJsoupDocument.getElementById(id)?.text()
        price = if (!price.isNullOrBlank()) price else "0"
        Log.i(TAG, "extractGweiPrice ${price.toString()}" )
        return price?.toInt() ?: 0
    }

    fun extractLowGwei(): EtherScanGasTrackerBuilder {
        var gwei = getUnknownGwei()

        gwei.price = extractGweiPrice(LOW_PRICE_ID)
        gwei.speed = Gas.GweiSpeed.SAFE_LOW
        gwei.fee = 0

        mGweiHashMap.put(Gas.GweiSpeed.SAFE_LOW, gwei)

        return this
    }

    fun extractMedGwei(): EtherScanGasTrackerBuilder {
        var gwei = getUnknownGwei()
        gwei.price = extractGweiPrice(AVG_PRICE_ID)
        gwei.speed = Gas.GweiSpeed.AVERAGE
        gwei.fee = 0
        mGweiHashMap.put(Gas.GweiSpeed.AVERAGE, gwei)
        return this
    }

    fun extractHighGwei(): EtherScanGasTrackerBuilder {
        var gwei = getUnknownGwei()
        gwei.price = extractGweiPrice(HIGH_PRICE_ID)
        gwei.speed = Gas.GweiSpeed.FAST
        gwei.fee = 0
        mGweiHashMap.put(Gas.GweiSpeed.FAST, gwei)
        return this
    }

    fun build() = mGweiHashMap
}