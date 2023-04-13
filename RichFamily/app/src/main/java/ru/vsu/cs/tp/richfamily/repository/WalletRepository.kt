package ru.vsu.cs.tp.richfamily.repository

import androidx.lifecycle.LiveData
import ru.vsu.cs.tp.richfamily.data.WalletDao
import ru.vsu.cs.tp.richfamily.model.Wallet

class WalletRepository(private val walletDao: WalletDao) {

    val allWallets: LiveData<List<Wallet>> = walletDao.getAllWallets()

    suspend fun insert(wallet: Wallet) {
        walletDao.insert(wallet)
    }

    suspend fun delete(wallet: Wallet) {
        walletDao.delete(wallet)
    }

    suspend fun update(wallet: Wallet) {
        walletDao.update(wallet)
    }
}