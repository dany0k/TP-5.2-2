package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.wallet.Wallet
import ru.vsu.cs.tp.richfamily.databinding.WalletRvItemBinding

class WalletRVAdapter(
    private val walletClickDeleteInterface: WalletClickDeleteInterface,
    private val walletClickEditInterface: WalletClickEditInterface
) : ListAdapter<Wallet, WalletRVAdapter.Holder>(Comparator()) {
    class Holder(binding: WalletRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val walletName: TextView = binding.walletName
        private val walletTotal: TextView = binding.walletTotal
        val deleteWalIV: ImageView = binding.deleteWalletIV
        val editWalIV: ImageView = binding.editWalletIV
        fun bind(wallet: Wallet) {
            walletName.text = wallet.acc_name
            walletTotal.text = wallet.acc_sum.toString()
        }
    }

    class Comparator: DiffUtil.ItemCallback<Wallet>() {
        override fun areItemsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wallet_rv_item, parent, false)
        val binding = WalletRvItemBinding.bind(view)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        val id = getItem(position).id
        holder.deleteWalIV.setOnClickListener {
            walletClickDeleteInterface.onDeleteIconClick(id)
        }
        holder.editWalIV.setOnClickListener {
            walletClickEditInterface.onEditIconClick(position)
        }
    }
}

interface WalletClickDeleteInterface {
    fun onDeleteIconClick(id: Int)
}

interface WalletClickEditInterface {
    fun onEditIconClick(id: Int)
}
