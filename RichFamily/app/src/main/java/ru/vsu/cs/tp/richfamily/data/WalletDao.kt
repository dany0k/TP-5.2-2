package ru.vsu.cs.tp.richfamily.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.vsu.cs.tp.richfamily.model.Wallet

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wallet: Wallet)

    @Delete
    suspend fun delete(wallet: Wallet)

    @Update
    suspend fun update(wallet: Wallet)

    @Query("Select * from wallet order by id ASC")
    fun getAllWallets(): LiveData<List<Wallet>>


}