package com.sumit.farm_fresh

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<FAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_featured_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Bind data to the views
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "$${product.price}"
        holder.typeTextView.text = product.type

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(product.imageUri)
            .into(holder.productImageView)

        // Set click listener on the item
        holder.itemView.setOnClickListener {
            // Create an Intent to navigate to ProductDetailActivity
            val intent = Intent(holder.itemView.context, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT", product)  // Pass the product as Parcelable
            holder.itemView.context.startActivity(intent)  // Start the activity
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    // ViewHolder to bind data
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
    }
}
