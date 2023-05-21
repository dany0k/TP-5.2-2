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
import ru.vsu.cs.tp.richfamily.api.model.group.GroupUser
import ru.vsu.cs.tp.richfamily.databinding.GroupUserRvItemBinding

class GroupUserRVAdapter(
    private val deleteIconClickInterface: ClickDeleteInterface,
    private val iconClickInterface: ItemClickInterface,
    private val isLeader: Boolean
) : ListAdapter<GroupUser, GroupUserRVAdapter.Holder>(Comparator()) {
    class Holder(binding: GroupUserRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val username: TextView = binding.userName
        val deleteUserIV: ImageView = binding.deleteUserIV
        fun bind(groupUser: GroupUser) {
            val fullName = "${groupUser.first_name} ${groupUser.last_name}"
            username.text = fullName
        }
    }

    class Comparator: DiffUtil.ItemCallback<GroupUser>() {
        override fun areItemsTheSame(oldItem: GroupUser, newItem: GroupUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GroupUser, newItem: GroupUser): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_user_rv_item, parent, false)
        val binding = GroupUserRvItemBinding.bind(view)
        if (isLeader) {
            binding.deleteUserIV.visibility = View.VISIBLE
        } else {
            binding.deleteUserIV.visibility = View.GONE
        }
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        val id = getItem(position).id
        holder.deleteUserIV.setOnClickListener {
            deleteIconClickInterface.onDeleteIconClick(id)
        }
        holder.itemView.setOnClickListener {
            iconClickInterface.onItemClick(id)
        }
    }
}