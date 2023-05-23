package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.TemplateRvItemBinding
import ru.vsu.cs.tp.richfamily.api.model.template.Template

class TemplateRVAdapter(
    private val templateClickDeleteInterface: TemplateClickDeleteInterface,
    private val templateClickEditInterface: TemplateClickEditInterface,
    private val templateItemClickInterface: TemplateItemClickInterface
) : ListAdapter<Template, TemplateRVAdapter.Holder>(Comparator()) {
    class Holder(binding: TemplateRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val templateName: TextView = binding.templateName
        private val templateType: TextView = binding.templateType
        private val templateSum: TextView = binding.templateSum
        val deleteTemplateIV: ImageView = binding.deleteTemplateIV
        val editTemplateIV: ImageView = binding.editTemplateIV
        fun bind(template: Template) {
            templateName.text = template.temp_name
            templateType.text = template.temp_variant
            val formattedStr = "%.3f"
            templateSum.text = formattedStr.format(template.temp_sum)
        }
    }

    class Comparator: DiffUtil.ItemCallback<Template>() {
        override fun areItemsTheSame(oldItem: Template, newItem: Template): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Template, newItem: Template): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.template_rv_item, parent, false)
        val binding = TemplateRvItemBinding.bind(view)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        val id = getItem(position).id
        holder.deleteTemplateIV.setOnClickListener {
            templateClickDeleteInterface.onDeleteIconClick(id)
        }
        holder.editTemplateIV.setOnClickListener {
            templateClickEditInterface.onEditIconClick(position)
        }
        holder.itemView.setOnClickListener {
            templateItemClickInterface.onItemClick(position)
        }
    }
}

interface TemplateClickDeleteInterface {
    fun onDeleteIconClick(id: Int)
}

interface TemplateItemClickInterface {
    fun onItemClick(id: Int)
}

interface TemplateClickEditInterface {
    fun onEditIconClick(id: Int)
}
