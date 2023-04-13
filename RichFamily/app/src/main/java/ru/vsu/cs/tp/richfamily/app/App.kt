package ru.vsu.cs.tp.richfamily.app

import android.app.Application
import androidx.room.Room
import ru.vsu.cs.tp.richfamily.data.WalletDao
import ru.vsu.cs.tp.richfamily.room.AppDataBase

class App : Application() {

    private lateinit var database: AppDataBase
    private lateinit var walletDao: WalletDao

    override fun onCreate() {
        super.onCreate()
        database = AppDataBase.getDatabase(this)
    }
}