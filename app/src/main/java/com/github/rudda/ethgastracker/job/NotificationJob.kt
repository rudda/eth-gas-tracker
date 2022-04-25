package com.github.rudda.ethgastracker.job


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder

import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.ethgastracker.data.Gas
import com.github.ethgastracker.data.GasTrackerDatabase
import com.github.ethgastracker.view.main.MainActivity
import com.github.rudda.ethgastracker.R
import java.lang.Exception


class NotificationJob (appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    private val CHANNEL_ID: String = "ETH_GAS_MAIN_CHANNEL"
    private val CHANNEL_NAME: String = "ETH GAS TRACKER CHANNEL"
    private val mDatabase by lazy { GasTrackerDatabase.getDatabase(appContext) }


    override fun doWork(): Result {
        // Do the work here--in this case, upload the images.
        checkValues()
        createNotificationChannel()
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    fun checkValues(){
        val gas = mDatabase.GasDao().getCurrentGasPrice()
        notifyIfNeeded(gas.low_gwei_id)
        notifyIfNeeded(gas.med_gwei_id)
        notifyIfNeeded(gas.high_gwei_id)
    }

    private fun notifyIfNeeded(id: Long) {
        Log.i("notifyIfNeeded", "notifyIfNeeded" )
        val gwei = mDatabase.GasDao().getGwei(id)

        when(gwei.speed) {
            Gas.GweiSpeed.SAFE_LOW -> {
               if(checkUserPreferences("notification_low_price", "gwei_value_low_price", gwei)) {
                    sendNotification("Hi, The Gas Price is so Good, only ${gwei.price}")
                }
            }

            Gas.GweiSpeed.AVERAGE -> {
                if(checkUserPreferences("notification_avg_price", "gwei_value_avg_price", gwei)) {
                    sendNotification("Hi, The Gas Price is so Good, only ${gwei.price}")
                }
            }

            Gas.GweiSpeed.FAST -> {
                if(checkUserPreferences("notification_high_price", "gwei_value_high_price", gwei)) {
                    sendNotification("Hi, The Gas Price is so Good, only ${gwei.price}")
                }
            }

            else -> {
                // do nothing yet
            }
        }
    }

    private fun checkUserPreferences(show_notification_preference: String, gwei_value_preference: String, gwei : Gas.Gwei): Boolean {
        //"notification_avg_price"
        val should_notification = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getBoolean(show_notification_preference, false)
        Log.i("checkUserPreferences", " notification_avg_price: ${should_notification.toString()}" )
        if(should_notification) {
            //gwei_value_avg_price
            val gwei_value = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                .getString(gwei_value_preference, "0")
            Log.i("notifyIfNeeded", " gwei_value_avg_price: ${gwei_value.toString()}" )

            if (gwei_value != null) {
                return try {
                    gwei.price <= gwei_value.toInt()
                } catch (error: Exception) {
                    return false
                }
            }
        }

        return false
    }

    private fun sendNotification(message : String) {
        Log.i("notifyIfNeeded", message)
            val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

        }

        var builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ethereum)
            .setContentTitle("ETH Gas Price Tracker")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(1010, builder)
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

