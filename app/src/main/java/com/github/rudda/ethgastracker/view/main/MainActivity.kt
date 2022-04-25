package com.github.ethgastracker.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import androidx.navigation.ui.NavigationUI
import com.github.ethgastracker.network.EtherScanNetwork
import com.github.rudda.ethgastracker.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var mBottomNavigationView: BottomNavigationView
    lateinit var mNavHostFragment : NavHostFragment
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        mNavHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_host_container) as NavHostFragment

        NavigationUI.setupWithNavController(mBottomNavigationView, mNavHostFragment.navController)

    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        mNavHostFragment.navController.handleDeepLink(intent)
//    }

//    fun shareApp() {
//        val sendIntent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
//            type = "text/plain"
//        }
//
//        val shareIntent = Intent.createChooser(sendIntent, null)
//        startActivity(shareIntent)
//    }

}