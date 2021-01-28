package com.mvhousedeveloper.tiketdotcomtest.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mvhousedeveloper.tiketdotcomtest.R
import com.mvhousedeveloper.tiketdotcomtest.model.User
import kotlinx.android.synthetic.main.list_item.view.*

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.user_name
    private val image: ImageView = view.user_image

    private var user: User? = null

    fun bind(user: User?) {
        if (user == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
        } else {
            showSearchData(user)
        }
    }

    private fun showSearchData(user: User) {
        this.user = user
        name.text = user.login

        Glide.with(itemView.context).load(user.avatarUrl).into(image)
    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
            return UserViewHolder(view)
        }
    }
}
