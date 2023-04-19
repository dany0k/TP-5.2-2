package ru.vsu.cs.tp.richfamily.app

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.cs.tp.richfamily.api.MainApi
import ru.vsu.cs.tp.richfamily.room.AppDataBase

class App : Application() {

    private lateinit var database: AppDataBase

    override fun onCreate() {
        super.onCreate()
        database = AppDataBase.getDatabase(this)
    }

    companion object {
        lateinit var mainApi: MainApi

        fun initRetrofit() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://dummyjson.com").client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            mainApi = retrofit.create(MainApi::class.java)
        }
    }
}