package ru.vsu.cs.tp.richfamily.app

import android.app.Application
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.vsu.cs.tp.richfamily.api.service.ClientApi
import java.io.IOException
import java.util.concurrent.TimeUnit


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        val config = YandexMetricaConfig.newConfigBuilder(API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
    }

    companion object {

        const val API_KEY = "a05eacce-4354-4813-b856-94d3d7bbc5b8"

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
                        .url(ClientApi.URL)
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