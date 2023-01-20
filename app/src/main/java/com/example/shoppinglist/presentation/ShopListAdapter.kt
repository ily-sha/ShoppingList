package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {

    companion object {
        const val ENABLED_VIEW_TYPE = 1
        const val DISABLED_VIEW_TYPE = 2
        const val MAX_POOL_SIZE = 20
    }
    var count = 0


    var listOfItems = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return if (listOfItems[position].enable) ENABLED_VIEW_TYPE else DISABLED_VIEW_TYPE
    }


    class ShopListViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val tvCount = view.findViewById<TextView>(R.id.tvCount)
        val tvName = view.findViewById<TextView>(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        Log.d("ShopListAdapter", "${++count}")
        val layout = when (viewType) {
            ENABLED_VIEW_TYPE -> R.layout.shop_item_enabled
            else -> R.layout.shop_item_disabled
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val item = listOfItems[position]

        holder.tvName.text = item.name
        holder.tvCount.text = item.count.toString()
        holder.view.setOnLongClickListener {
//            viewModel.updateShopItem(item)
            true

        }
    }
}