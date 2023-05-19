package ru.vsu.cs.tp.richfamily.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.tp.richfamily.R
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ClickDeleteInterface
import ru.vsu.cs.tp.richfamily.adapter.interfaces.ItemClickInterface
import ru.vsu.cs.tp.richfamily.api.model.group.Group
import ru.vsu.cs.tp.richfamily.databinding.GroupRvItemBinding

class GroupListRVAdapter(
    private val groupClickExitInterface: ClickDeleteInterface,
    private val groupClickLeaveInterface: ClickLeaveInterface,
    private val itemClickInterface: ItemClickInterface,
    private val isLeader: Boolean
) : ListAdapter<Group, GroupListRVAdapter.Holder>(Comparator()) {
    class Holder(binding: GroupRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val groupName: TextView = binding.groupName
        val exitGroupIV: ImageView = binding.exitGroupIV
        val deleteGroupIV: ImageView = binding.deleteGroupIV
        fun bind(group: Group) {
            groupName.text = group.gr_name
        }
    }

    class Comparator: DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_rv_item, parent, false)
        val binding = GroupRvItemBinding.bind(view)
        if (isLeader) {
            binding.deleteGroupIV.visibility = View.VISIBLE
            binding.exitGroupIV.visibility = View.GONE
        } else {
            binding.deleteGroupIV.visibility = View.GONE
            binding.exitGroupIV.visibility = View.VISIBLE
        }
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        val id = getItem(position).id
        holder.itemView.setOnClickListener {
            itemClickInterface.onItemClick(id)
        }
        holder.exitGroupIV.setOnClickListener {
            groupClickLeaveInterface.onLeaveIconClick(id)
        }
        holder.deleteGroupIV.setOnClickListener {
            groupClickExitInterface.onDeleteIconClick(id)
        }
    }
}

interface ClickLeaveInterface {
    fun onLeaveIconClick(id: Int)
}