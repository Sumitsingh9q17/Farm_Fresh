package com.sumit.farm_fresh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private val productList: MutableList<Product>,
    private val listener: OnItemClickListener,
    private val isAdmin: Boolean // Added isAdmin flag to determine user role
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    interface OnItemClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.nameTextView.text = product.name
        holder.priceTextView.text = "₹${product.price}"
        holder.typeTextView.text = product.type
        holder.categoryTextView.text = product.category

        // Load image using Glide (you can also use Picasso or other libraries)
        Glide.with(holder.itemView.context).load(product.imageUri).into(holder.productImageView)

        // Set visibility of Edit and Delete buttons based on user role

        if (isAdmin) {
            holder.editButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.VISIBLE
        } else {
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        }

        holder.editButton.setOnClickListener {
            listener.onEditClick(position)
        }

        holder.deleteButton.setOnClickListener {
            listener.onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateProductList(filteredProducts: List<Product>) {
        productList.clear()
        productList.addAll(filteredProducts)
        notifyDataSetChanged()
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tv_product_name)
        val priceTextView: TextView = view.findViewById(R.id.tv_product_price)
        val typeTextView: TextView = view.findViewById(R.id.tv_product_type)
        val productImageView: ImageView = view.findViewById(R.id.iv_product_image)
        val categoryTextView: TextView = view.findViewById(R.id.tv_product_category)
        val editButton: Button = view.findViewById(R.id.btn_edit)
        val deleteButton: Button = view.findViewById(R.id.btn_delete)
    }
}