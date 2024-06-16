package com.example.retailcloudassessmentproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retailcloudassessmentproject.databinding.CartListItemBinding
import com.example.retailcloudassessmentproject.manager.Utils
import com.example.retailcloudassessmentproject.databinding.ProductListItemBinding
import com.example.retailcloudassessmentproject.model.CartItem

class CartListATDR : RecyclerView.Adapter<CartListATDR.ProductListViewHolder>() {

    var items = ArrayList<CartItem>()

    class ProductListViewHolder(mBinding: CartListItemBinding) : RecyclerView.ViewHolder(mBinding.root) {
        val binding: CartListItemBinding = mBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        return ProductListViewHolder(
            CartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleTV.text = item.itemName
        val totalPrice = item.sellingPrice?.times(item.quantity ?: 1)
        holder.binding.priceTV.text = "Price: ${Utils.toCurrencyString(totalPrice)}"
        holder.binding.TaxTV.text = "Tax Amount: ${item.taxPercentage} %"
        holder.binding.quantityTV.text = "Qty: " + item.quantity.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<CartItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
