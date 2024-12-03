package com.sumit.farm_fresh

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class FeaturedProductAdapter(
    private var products: List<Product>, // Use mutable list for dynamic updates
    private val onProductClick: (Product) -> Unit // Callback function for item click
) : RecyclerView.Adapter<FeaturedProductAdapter.ProductViewHolder>() {

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_featured_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = products[position]
        holder.productName.text = currentProduct.name
        holder.productPrice.text = "₹${currentProduct.price}"
        holder.productType.text = currentProduct.type

        // Use Glide to load the image into the ImageView
        Glide.with(holder.productImage.context)
            .load(currentProduct.imageUri)  // Assuming imageUri is a URL or local path
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.farmfresh)  // Placeholder image while loading
                    .error(R.drawable.farmfresh)  // Error image if the loading fails
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )  // Cache strategy
            .into(holder.productImage)
        Log.d("ProductAdapter", "Loading image: ${currentProduct.imageUri}")

        // Set the click listener for the item
        holder.itemView.setOnClickListener {
            onProductClick(currentProduct) // Trigger the callback when item is clicked
        }
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val productType: TextView = view.findViewById(R.id.productType)
    }
}
