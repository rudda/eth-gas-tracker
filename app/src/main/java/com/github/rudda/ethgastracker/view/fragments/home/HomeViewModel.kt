package com.github.ethgastracker.view.fragments.home

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import com.github.ethgastracker.data.Gas
import com.github.ethgastracker.data.GasTrackerDatabase
import com.github.ethgastracker.model.EtherScanGasTrackerBuilder
import com.github.ethgastracker.network.EtherScanNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class HomeViewModel(app: Application) : AndroidViewModel(app), DefaultLifecycleObserver {
    // Begin: Lifecycle methods
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        timer.start()
        run()
    }
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        timer.cancel()
    }
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        timer.cancel()
    }
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        timer.cancel()
    }
    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
    // End: Lifecycle methods

    //livedata
    var mLatest_update_values = MutableLiveData<String>()
    private var _mLow_price = MutableLiveData<Int>()
    private var _mAvg_price= MutableLiveData<Int>()
    private var _mHigh_price= MutableLiveData<Int>()
    //database
    private val mDatabase by lazy { GasTrackerDatabase.getDatabase(app) }

    //TransformationMaps in order to have pretty strings in view
    val mLow_priceMap = Transformations.map(_mLow_price) {
        return@map "${it} Gwei"
    }
    val mAvg_priceMap = Transformations.map(_mAvg_price) {
        return@map "${it} Gwei"
    }
    val mHigh_priceMap = Transformations.map(_mHigh_price) {
        return@map "${it} Gwei"
    }

    //countDown |  how many time for updates
    val timer = object: CountDownTimer(15000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
           Log.i("CountDownTimer", millisUntilFinished.toString())
        }
        override fun onFinish() {
            run()
            restartTimer()
        }
    }

    private fun restartTimer() {
        timer.start()
    }

    private fun run(){
        Log.i("HomeViewModel", "scraping: EtherScanNetwork")
        val service = EtherScanNetwork().getService()

        service.gasTracker().enqueue( object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                val mGweiHashMap = EtherScanGasTrackerBuilder(response.body().toString())
                    .extractLowGwei()
                    .extractMedGwei()
                    .extractHighGwei()
                    .build()
                save(mGweiHashMap)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("onResponse", t.message.toString())
            }

        })
    }

    @WorkerThread
    private fun save(mGweiHashMap: HashMap<Gas.GweiSpeed, Gas.Gwei>) {
        var gas = Gas(0, 0,0,0, Date().toString())
        mGweiHashMap.keys.forEach {
            var gwei = mGweiHashMap[it]

            if (gwei != null) {
                val id = mDatabase.GasDao().addGwei(gwei)
                Log.i("HomeViewModel", "gwei_id: ${id.toString()}")

                when(it){
                    Gas.GweiSpeed.FAST -> gas.high_gwei_id = id
                    Gas.GweiSpeed.AVERAGE -> gas.med_gwei_id = id
                    Gas.GweiSpeed.SAFE_LOW -> gas.low_gwei_id = id
                    else -> Log.i("HomeViewModel", "speed not found:  ${gwei.speed}")
                }
            }
         }

        mDatabase.GasDao().addGas(gas)
    }

    fun getGas(): LiveData<Gas> {
        return mDatabase.GasDao().getCurrentGasPrice(1)
    }

    fun getGweis(ids: Array<Long>): LiveData<List<Gas.Gwei>> {
        return mDatabase.GasDao().getGweis(ids)
    }

    fun updateLatestValues( latest_update_values: String ) {
        mLatest_update_values.value = latest_update_values
    }
    fun updateLowPrice(low_price: Int) {
        _mLow_price.value = low_price
    }
    fun updateAvgPrice(avg_price: Int) {
        _mAvg_price.value = avg_price
    }
    fun updateHighPrice(high_price: Int) {
        Log.i("updateHighPrice", high_price.toString())
        _mHigh_price.value = high_price
    }

}