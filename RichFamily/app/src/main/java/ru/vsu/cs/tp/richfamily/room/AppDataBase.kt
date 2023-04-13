package ru.vsu.cs.tp.richfamily.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.vsu.cs.tp.richfamily.MainActivity
import ru.vsu.cs.tp.richfamily.data.WalletDao
import ru.vsu.cs.tp.richfamily.model.Wallet

@Database(entities = [Wallet::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getWalletsDao(): WalletDao

    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "appDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}