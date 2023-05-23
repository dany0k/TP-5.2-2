package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.credit.Credit
import ru.vsu.cs.tp.richfamily.databinding.CreditRvItemBinding

class CreditRVAdapter(
    private val creditClickDeleteInterface: CreditClickDeleteInterface,
    private val creditClickItemInterface: CreditClickItemInterface
) : ListAdapter<Credit, CreditRVAdapter.Holder>(Comparator()) {
    class Holder(binding: CreditRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val creditName: TextView = binding.creditName
        private val creditSum: TextView = binding.creditTotal
        val deleteTemplateIV: ImageView = binding.deleteCreditIV
        fun bind(credit: Credit) {
            // creditName
            val formattedStr = "%.3f"
            creditName.text = credit.cr_name
            creditSum.text = formattedStr.format(credit.cr_all_sum)
        }
    }

    class Comparator: DiffUtil.ItemCallback<Credit>() {
        override fun areItemsTheSame(oldItem: Credit, newItem: Credit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Credit, newItem: Credit): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.credit_rv_item, parent, false)
        val binding = CreditRvItemBinding.bind(view)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        val id = getItem(position).id
        holder.itemView.setOnClickListener {
            creditClickItemInterface.onItemClick(position)
        }
        holder.deleteTemplateIV.setOnClickListener {
            creditClickDeleteInterface.onDeleteIconClick(id)
        }
    }
}

interface CreditClickDeleteInterface {
    fun onDeleteIconClick(id: Int)
}

interface CreditClickItemInterface {
    fun onItemClick(id: Int)
}


