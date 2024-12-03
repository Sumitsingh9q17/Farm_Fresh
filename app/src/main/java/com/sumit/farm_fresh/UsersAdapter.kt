package com.sumit.farm_fresh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private var userList: MutableList<User>,  // Make userList mutable
    private val listener: OnUserClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface OnUserClickListener {
        fun onEdit(user: User)
        fun onDelete(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.nameTextView.text = user.name
        holder.emailTextView.text = user.email

        // Set up edit and delete buttons
        holder.editButton.setOnClickListener { listener.onEdit(user) }
        holder.deleteButton.setOnClickListener { listener.onDelete(user) }
    }

    override fun getItemCount(): Int = userList.size

    // Method to update the user list
    fun updateList(newUserList: List<User>) {
        userList.clear()  // Clear the current list
        userList.addAll(newUserList)  // Add the new list
        notifyDataSetChanged()  // Notify the adapter to refresh the view
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textViewName)
        val emailTextView: TextView = view.findViewById(R.id.textViewEmail)
        val editButton: Button = view.findViewById(R.id.btnEditUser)
        val deleteButton: Button = view.findViewById(R.id.btnDeleteUser)
    }
}
