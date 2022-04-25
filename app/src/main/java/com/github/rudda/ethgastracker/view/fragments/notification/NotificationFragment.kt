package com.github.ethgastracker.view.fragments.notification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.rudda.ethgastracker.R

class NotificationFragment : PreferenceFragmentCompat() {



//    private lateinit var viewModel: NotificationViewModel

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
//    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_preferences, rootKey)

//        val shouldShowNotification: Preference? = findPreference("notification_low_price")
//        shouldShowNotification?.onPreferenceChangeListener = object : Preference.OnPreferenceChangeListener{
//            override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
//                Log.i("NotificationFragment", newValue.toString() )
//
//                return true
//            }
//
//        }
//
//        val gweiValueForNotification: Preference? = findPreference("gwei_value_low_price")
//
//        gweiValueForNotification?.onPreferenceChangeListener = object : Preference.OnPreferenceChangeListener{
//            override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
//                Log.i("NotificationFragment", newValue.toString() )
//
//                return true
//            }
//
//        }
    }

}