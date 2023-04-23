package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.api.model.Consumption
import ru.vsu.cs.tp.richfamily.databinding.ConsumptionRvItemBinding

class ConsumptionRVAdapter(
    private val consumptionClickDeleteInterface: ConsumptionClickDeleteInterface,
    private val consumptionClickEditInterface: ConsumptionClickEditInterface
) : ListAdapter<Consumption, ConsumptionRVAdapter.Holder>(Comparator()) {
    class Holder(binding: ConsumptionRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val consName: TextView = binding.consumptionName
        private val consSum: TextView = binding.consumptionSum
        private val consDate: TextView = binding.consumptionDate
        private val consTime: TextView = binding.consumptionTime
        val deleteTemplateIV: ImageView = binding.deleteConsumptionIV
        val editTemplateIV: ImageView = binding.editConsumptionIV
        fun bind(consumption: Consumption) {
            consName.text = consumption.consName
            consSum.text = consumption.consSum.toString()
            consDate.text = consumption.consDate.toString()
            consTime.text = consumption.consTime.toString()
        }
    }

    class Comparator: DiffUtil.ItemCallback<Consumption>() {
        override fun areItemsTheSame(oldItem: Consumption, newItem: Consumption): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Consumption, newItem: Consumption): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.consumption_rv_item, parent, false)
        val binding = ConsumptionRvItemBinding.bind(view)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        val id = getItem(position).id
        holder.deleteTemplateIV.setOnClickListener {
            consumptionClickDeleteInterface.onDeleteIconClick(id)
        }
        holder.editTemplateIV.setOnClickListener {
            consumptionClickEditInterface.onEditIconClick(id)
        }
    }
}

interface ConsumptionClickDeleteInterface {
    fun onDeleteIconClick(id: Int)
}

interface ConsumptionClickEditInterface {
    fun onEditIconClick(id: Int)
}
