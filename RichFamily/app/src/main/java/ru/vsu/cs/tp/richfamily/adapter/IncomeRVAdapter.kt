package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.Income
import ru.vsu.cs.tp.richfamily.databinding.IncomeRvItemBinding

class IncomeRVAdapter(
    private val incomeClickDeleteInterface: IncomeClickDeleteInterface,
    private val incomeClickEditInterface: IncomeClickEditInterface
) : ListAdapter<Income, IncomeRVAdapter.Holder>(Comparator()) {
    class Holder(binding: IncomeRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val incomeName: TextView = binding.incomeName
        private val incomeSum: TextView = binding.incomeSum
        private val incomeDate: TextView = binding.incomeDate
        private val incomeTime: TextView = binding.incomeTime
        val deleteIncomeIV: ImageView = binding.deleteIncomeIV
        val editIncomeIV: ImageView = binding.editIncomeIV
        fun bind(income: Income) {
            incomeName.text = income.incomeName
            incomeSum.text = income.incomeSum.toString()
            incomeDate.text = income.incomeDate.toString()
            incomeTime.text = income.incomeTime.toString()
        }
    }

    class Comparator: DiffUtil.ItemCallback<Income>() {
        override fun areItemsTheSame(oldItem: Income, newItem: Income): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Income, newItem: Income): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.income_rv_item, parent, false)
        val binding = IncomeRvItemBinding.bind(view)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        val id = getItem(position).id
        holder.deleteIncomeIV.setOnClickListener {
            incomeClickDeleteInterface.onDeleteIconClick(id)
        }
        holder.editIncomeIV.setOnClickListener {
            incomeClickEditInterface.onEditIconClick(id)
        }
    }
}

interface IncomeClickDeleteInterface {
    fun onDeleteIconClick(id: Int)
}

interface IncomeClickEditInterface {
    fun onEditIconClick(id: Int)
}
