package ru.vsu.cs.tp.richfamily.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.databinding.WalletRvItemBinding
import ru.vsu.cs.tp.richfamily.model.Wallet
import ru.vsu.cs.tp.richfamily.view.wallet.WalletFragment

class WalletRVAdapter(
    val context: WalletFragment,
    private val walletClickDeleteInterface: WalletClickDeleteInterface,
    private val walletClickInterface: WalletClickInterface
    ) : RecyclerView.Adapter<WalletRVAdapter.WalletViewHolder>() {


    private val allWallet = ArrayList<Wallet>()

    inner class WalletViewHolder(
        binding: WalletRvItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var walletText: TextView = binding.walletTitle
        var walletScore: TextView = binding.walletScore
        var delete: ImageView = binding.deleteWallet
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : WalletViewHolder {
        val binding = WalletRvItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        holder.walletText.text = allWallet[position].walletTitle
        holder.walletScore.text = allWallet[position].walletScore.toString()
        holder.delete.setOnClickListener {
            walletClickDeleteInterface.onDeleteIconClick(allWallet[position])
        }
        holder.itemView.setOnClickListener {
            walletClickInterface.onNoteClick(allWallet[position])
        }
    }

    override fun getItemCount() : Int {
        return allWallet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Wallet>) {
        allWallet.clear()
        allWallet.addAll(newList)
        notifyDataSetChanged()
    }
}

interface WalletClickDeleteInterface {
    fun onDeleteIconClick(wallet: Wallet)
}

interface WalletClickInterface {
    fun onNoteClick(wallet: Wallet)
}