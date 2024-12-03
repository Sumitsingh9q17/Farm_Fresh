package com.sumit.farm_fresh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class OrderHistoryAdapter : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    private var orderHistoryList: List<OrderHistory> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_history, parent, false)
        return OrderHistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = orderHistoryList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }

    fun submitList(list: List<OrderHistory>) {
        orderHistoryList = list
        notifyDataSetChanged()
    }

    inner class OrderHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productNameTextView: TextView = itemView.findViewById(R.id.productName)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.productPrice)
        private val orderDateTextView: TextView = itemView.findViewById(R.id.orderDate)
        private val productImageView: ImageView = itemView.findViewById(R.id.productImage)

        fun bind(order: OrderHistory) {
            productNameTextView.text = order.productName
            productPriceTextView.text = "Price: ${order.productPrice}"
            orderDateTextView.text = "Ordered on: ${order.orderDate}"
            Glide.with(itemView.context)
                .load(order.productImage)
                .into(productImageView)
        }
    }
}
