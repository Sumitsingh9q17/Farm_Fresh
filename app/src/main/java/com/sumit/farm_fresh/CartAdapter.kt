package com.sumit.farm_fresh

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(private var cartItems: MutableList<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.productImageView)
        val productName: TextView = view.findViewById(R.id.productNameTextView)
        val productPrice: TextView = view.findViewById(R.id.productPriceTextView)
        val removeButton: ImageView = view.findViewById(R.id.removeItemButton)
        val increaseButton: Button = view.findViewById(R.id.increaseQuantityButton)
        val decreaseButton: Button = view.findViewById(R.id.decreaseQuantityButton)
        val quantityText: TextView = view.findViewById(R.id.productQuantityTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val product = cartItem.product

        // Set product details
        Glide.with(holder.productImage.context)
            .load(product.imageUri)
            .into(holder.productImage)
        holder.productName.text = product.name
        holder.productPrice.text = "₹${"%.2f".format(product.price)}"
        holder.quantityText.text = cartItem.quantity.toString()

        // Handle increase button click
        holder.increaseButton.setOnClickListener {
            val updatedQuantity = cartItem.quantity + 1
            Log.d(
                "CartAdapter",
                "Increase clicked. Current quantity: ${cartItem.quantity}, New quantity: $updatedQuantity"
            )
            CartManager.updateQuantity(product.id, updatedQuantity)
            cartItems[position].quantity = updatedQuantity
            holder.quantityText.text = updatedQuantity.toString()
            notifyItemChanged(position)
            (holder.itemView.context as CartActivity).refreshCart()
        }

        // Handle decrease button click
        holder.decreaseButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                val updatedQuantity = cartItem.quantity - 1
                Log.d(
                    "CartAdapter",
                    "Decrease clicked. Current quantity: ${cartItem.quantity}, New quantity: $updatedQuantity"
                )
                CartManager.updateQuantity(product.id, updatedQuantity)
                cartItems[position].quantity = updatedQuantity
                holder.quantityText.text = updatedQuantity.toString()
                notifyItemChanged(position)
                (holder.itemView.context as CartActivity).refreshCart()
            } else {
                CartManager.removeProduct(product.id)
                cartItems.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cartItems.size)
                (holder.itemView.context as CartActivity).refreshCart()
            }
        }

        // Handle remove button click
        holder.removeButton.setOnClickListener {
            CartManager.removeProduct(product.id)
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
            (holder.itemView.context as CartActivity).refreshCart()
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    // Method to update the cart dataset
    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(newCartItems)
        notifyDataSetChanged()
    }
}
