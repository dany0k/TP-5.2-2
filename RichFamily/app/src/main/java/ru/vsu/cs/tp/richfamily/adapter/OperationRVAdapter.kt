package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.operation.Operation
import ru.vsu.cs.tp.richfamily.databinding.OperationRvItemBinding

class OperationRVAdapter(
    private val operationClickDeleteInterface: OperationClickDeleteInterface,
    private val operationClickEditInterface: OperationClickEditInterface
) : ListAdapter<Operation, OperationRVAdapter.Holder>(Comparator()) {
    class Holder(binding: OperationRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val opRec: TextView = binding.operationRec
        private val opSum: TextView = binding. operationSum
        private val opDate: TextView = binding.operationDate
        private val opTime: TextView = binding.operationTime
        val deleteOpIV: ImageView = binding.deleteOpIV
        val editOpIV: ImageView = binding.editOpIV
        fun bind(operation: Operation) {
            val sum: String = if (operation.op_variant == "ДОХОД") {
                operation.op_sum.toString()
            } else {
                "-${operation.op_sum}"
            }
            opRec.text = operation.op_recipient
            opSum.text = sum
            opDate.text = getDate(operation.op_date)
            opTime.text = getTime(operation.op_date)
        }

        private fun getTime(dateTimeString: String): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val dateTime = LocalDateTime.parse(dateTimeString, formatter)
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            return dateTime.format(timeFormatter)
        }

        private fun getDate(dateTimeString: String): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val dateTime = LocalDateTime.parse(dateTimeString, formatter)
            val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
            return dateTime.format(dateFormatter)
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
        val id = getItem(position).id
        holder.deleteOpIV.setOnClickListener {
            operationClickDeleteInterface.onDeleteIconClick(id = id)
        }
        holder.editOpIV.setOnClickListener {
            operationClickEditInterface.onEditIconClick(id = id)
        }
    }
}

interface OperationClickDeleteInterface {
    fun onDeleteIconClick(id: Int)
}

interface OperationClickEditInterface {
    fun onEditIconClick(id: Int)
}
