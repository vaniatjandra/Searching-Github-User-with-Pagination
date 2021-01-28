package com.mvhousedeveloper.tiketdotcomtest.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mvhousedeveloper.tiketdotcomtest.model.User

class ListAdapter : ListAdapter<User, UserViewHolder>(DATA_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
    }

    companion object {
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                    oldItem.avatarUrl == newItem.avatarUrl

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                    oldItem == newItem
        }
    }
}
