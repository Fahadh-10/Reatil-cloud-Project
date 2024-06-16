package com.example.retailcloudassessmentproject.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retailcloudassessmentproject.Dao.CartDao
import com.example.retailcloudassessmentproject.Dao.ItemDao
import com.example.retailcloudassessmentproject.manager.Utils
import com.example.retailcloudassessmentproject.databinding.ProductListItemBinding
import com.example.retailcloudassessmentproject.model.Item

class ProductListATDR : RecyclerView.Adapter<ProductListATDR.ProductListViewHolder>() {

    var items = ArrayList<Item>()
    private var mOnItemClickListeners: OnItemClickListeners? = null

    interface OnItemClickListeners {
        fun onItemClick(position: Int, item: Item)
    }

    fun setOnClickListeners(onItemClick: OnItemClickListeners) {
        this.mOnItemClickListeners = onItemClick
    }

    class ProductListViewHolder(mBinding: ProductListItemBinding) : RecyclerView.ViewHolder(mBinding.root) {
        val binding: ProductListItemBinding = mBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        return ProductListViewHolder(
            ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleTV.text = item.itemName
        holder.binding.priceTV.text = "Price: ${Utils.toCurrencyString(item.sellingPrice)}"
        holder.binding.TaxTV.visibility = GONE

        holder.binding.addToCartTV.setOnClickListener {
            mOnItemClickListeners?.onItemClick(position, item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
