package com.github.ethgastracker.view.fragments.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import com.github.ethgastracker.data.Gas
import com.github.rudda.ethgastracker.R
import com.github.rudda.ethgastracker.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    val TAG = "HomeFragment"
    private lateinit var mBinding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = HomeFragmentBinding.inflate(layoutInflater)

        return  mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)

        viewModel.getGas().observe(viewLifecycleOwner, Observer {
            if(it != null) {
                Log.i(TAG, "getgas ${it.toString()}")
                val ids = arrayOf(it.low_gwei_id, it.med_gwei_id, it.high_gwei_id)
                viewModel.updateLatestValues(it.last_updated)
                viewModel.getGweis(ids).observe(viewLifecycleOwner, Observer { gweiList ->
                    if(gweiList != null){
                        Log.i(TAG, gweiList.size.toString())
                        gweiList.forEach { gwei ->
                            when(gwei.speed){
                                Gas.GweiSpeed.SAFE_LOW -> viewModel.updateLowPrice(gwei.price)
                                Gas.GweiSpeed.AVERAGE -> viewModel.updateAvgPrice(gwei.price)
                                Gas.GweiSpeed.FAST -> viewModel.updateHighPrice(gwei.price)
                                else ->  Log.i(TAG, "Nothing to update price")
                            }
                        }
                    }
                })
            }
        })
    }

}