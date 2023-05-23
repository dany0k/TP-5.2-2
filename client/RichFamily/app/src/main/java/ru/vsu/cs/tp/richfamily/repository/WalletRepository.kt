package ru.vsu.cs.tp.richfamily.repository

import okhttp3.ResponseBody
import retrofit2.Response
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.api.model.wallet.WalletRequestBody
import ru.vsu.cs.tp.richfamily.api.service.WalletApi

class WalletRepository(
    private val walletApi: WalletApi,
    private val token: String
) {
    suspend fun getAllWallets(): Response<List<Wallet>> {
        return walletApi.getWallets(token = token)
    }

    suspend fun addWallet(
        token: String,
        walletRequestBody: WalletRequestBody
    ): Response<Wallet> {
        return walletApi.addWallet(token = token, walletRequestBody = walletRequestBody)
    }

    suspend fun deleteWallet(token: String, id: Int): Response<ResponseBody> {
        return walletApi.deleteWallet(token = token, id = id)
    }

    suspend fun editWallet(
        walletRequestBody: WalletRequestBody,
        id: Int
    ): Response<Wallet> {
        return walletApi.updateWallet(
            token = token,
            walletRequestBody = walletRequestBody,
            id = id
        )
    }
}