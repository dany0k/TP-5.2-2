package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.databinding.CategoryRvItemBinding
import ru.vsu.cs.tp.richfamily.api.model.category.Category

class CategoryRVAdapter(
    private val categoryClickDeleteInterface: CategoryClickDeleteInterface,
    private val categoryClickEditInterface: CategoryClickEditInterface
) : ListAdapter<Category, CategoryRVAdapter.Holder>(Comparator()) {
    class Holder(binding: CategoryRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val catName: TextView = binding.categoryName
        val deleteCatIV: ImageView = binding.deleteCategoryIV
        val editCatIV: ImageView = binding.editCategoryIV
        fun bind(category: Category) {
            catName.text = category.cat_name
        }
    }

    class Comparator: DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_rv_item, parent, false)
        val binding = CategoryRvItemBinding.bind(view)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        val id = getItem(position).id
        holder.deleteCatIV.setOnClickListener {
            categoryClickDeleteInterface.onDeleteIconClick(id)
        }
        holder.editCatIV.setOnClickListener {
            categoryClickEditInterface.onEditIconClick(id)
        }
    }
}

interface CategoryClickDeleteInterface {
    fun onDeleteIconClick(id: Int)
}

interface CategoryClickEditInterface {
    fun onEditIconClick(id: Int)
}
