package ru.vsu.cs.tp.richfamily.app

import android.app.Application
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.cs.tp.richfamily.api.service.CategoryService
import ru.vsu.cs.tp.richfamily.room.AppDataBase
import java.io.IOException
import java.util.concurrent.TimeUnit

class App : Application() {

    private lateinit var database: AppDataBase

    override fun onCreate() {
        super.onCreate()
        database = AppDataBase.getDatabase(this)
    }

    companion object {
        lateinit var categoryService: CategoryService

        fun initRetrofit() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000").client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            categoryService = retrofit.create(CategoryService::class.java)
        }

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