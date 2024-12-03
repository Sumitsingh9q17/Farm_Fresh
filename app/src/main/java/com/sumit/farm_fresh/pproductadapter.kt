package com.sumit.farm_fresh

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class pproductadapter(
    private var products: List<Product>,
    private val cart: Cart,
    private val context: Context // Add context for starting a new activity
) : RecyclerView.Adapter<pproductadapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val addToCartButton: Button = view.findViewById(R.id.addToCartButton) // Add to Cart button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pproduct, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Set product details
        holder.productName.text = product.name
        holder.productPrice.text = "₹${"%.2f".format(product.price)}"
        Glide.with(holder.productImage.context).load(product.imageUri).into(holder.productImage)

        // Handle Add to Cart button click
        holder.addToCartButton.setOnClickListener {
            // Use CartManager's addToCart function
            CartManager.addToCart(product)

            // Notify user
            Toast.makeText(
                holder.itemView.context,
                "${product.name} added to cart",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Handle Product click to open ProductDetailActivity
        holder.itemView.setOnClickListener {
            // Start ProductDetailActivity and pass the product data
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT", product) // Pass the product object
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = products.size

    // Update product list
    fun updateProductList(newProducts: List<Product>?) {
        products = newProducts ?: listOf()
        notifyDataSetChanged()
    }
}
