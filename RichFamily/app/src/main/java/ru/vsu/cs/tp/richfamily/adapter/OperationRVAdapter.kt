package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.operation.Operation
import ru.vsu.cs.tp.richfamily.databinding.OperationRvItemBinding

class OperationRVAdapter(
    private val operationClickDeleteInterface: OperationClickDeleteInterface,
    private val operationClickEditInterface: OperationClickEditInterface
) : ListAdapter<Operation, OperationRVAdapter.Holder>(Comparator()) {
    class Holder(binding: OperationRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val opName: TextView = binding.operationName
        private val opType: TextView = binding.operationType
        private val opSum: TextView = binding. operationSum
        private val opDate: TextView = binding.operationDate
        private val opTime: TextView = binding.operationTime
        fun bind(operation: Operation) {
//            opName.text = operation.
            opType.text = operation.op_variant
            opSum.text = operation.op_sum.toString()
            opDate.text = operation.op_date.toString()
//            opTime.text = operation..toString()
        }
    }

    class Comparator: DiffUtil.ItemCallback<Operation>() {
        override fun areItemsTheSame(oldItem: Operation, newItem: Operation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Operation, newItem: Operation): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.operation_rv_item, parent, false)
        val binding = OperationRvItemBinding.bind(view)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}

interface OperationClickDeleteInterface {
    fun onDeleteIconClick(id: Int)
}

interface OperationClickEditInterface {
    fun onEditIconClick(id: Int)
}
