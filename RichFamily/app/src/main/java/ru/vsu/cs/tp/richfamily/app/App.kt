package ru.vsu.cs.tp.richfamily.app

import android.app.Application
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8000"

        fun checkServerConnection(): Boolean {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

            try {
                okHttpClient.newCall(
                    Request.Builder()
                        .url(BASE_URL)
                        .build()
                ).execute()
                return true
            } catch (e: IOException) {
                Log.d("Err", "NO CONNECTION")
            }
            return false
        }
    }
}