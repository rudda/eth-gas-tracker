package com.github.rudda.ethgastracker

import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.rudda.ethgastracker.job.NotificationJob
import java.util.concurrent.TimeUnit


class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        val notificationRequest = PeriodicWorkRequestBuilder<NotificationJob>(10, TimeUnit.SECONDS, 15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this)
            .enqueue(notificationRequest)
    }

}