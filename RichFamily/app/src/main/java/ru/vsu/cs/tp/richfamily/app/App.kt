package ru.vsu.cs.tp.richfamily.app

import android.app.Application
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.cs.tp.richfamily.api.service.ServiceAPI
import java.io.IOException
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        lateinit var serviceAPI: ServiceAPI

        fun getRetrofit(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000").client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }

        fun initRetrofit() {}

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
                        .url("http://10.0.2.2:8000")
                        .build()
                ).execute()
                return true
            } catch (e: IOException) {
                Log.d("E", "NO CON")
            }
            return false
        }
    }
}