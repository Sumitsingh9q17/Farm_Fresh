//package com.sumit.farm_fresh
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//class UserListAdapter(
//    private val userList: List<User>,
//    private val onActionClick: (User, String) -> Unit
//) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
//        return UserViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val user = userList[position]
//        holder.bind(user)
//    }
//
//    override fun getItemCount(): Int {
//        return userList.size
//    }
//
//    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
//        private val tvUserEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
//        private val btnEditUser: Button = itemView.findViewById(R.id.btnEditUser)
//        private val btnDeleteUser: Button = itemView.findViewById(R.id.btnDeleteUser)
//
//        fun bind(user: User) {
//            tvUserName.text = user.name
//            tvUserEmail.text = user.email
//
//            btnEditUser.setOnClickListener {
//                onActionClick(user, "edit")
//            }
//
//            btnDeleteUser.setOnClickListener {
//                onActionClick(user, "delete")
//            }
//        }
//    }
//}
