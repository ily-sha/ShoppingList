package com.example.shoppinglist.presentation

import android.provider.ContactsContract.RawContacts.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.databinding.ShopItemDisabledBinding
import com.example.shoppinglist.databinding.ShopItemEnabledBinding
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.MainActivity.ShopItemDiffCallback
import com.example.shoppinglist.presentation.MainActivity.ShopListDiffCallback

class ShopListAdapter : ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(
    ShopItemDiffCallback()
){

    var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback(shopList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
            VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        viewHolder.binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        viewHolder.binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        when (viewHolder.binding){
            is ShopItemDisabledBinding -> {
                viewHolder.binding.tvName.text = shopItem.name
                viewHolder.binding.tvCount.text = shopItem.count.toString()
            }
            is ShopItemEnabledBinding -> {
                viewHolder.binding.tvName.text = shopItem.name
                viewHolder.binding.tvCount.text = shopItem.count.toString()
            }
        }

    }



    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enable) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }


    class ShopItemViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)


    companion object {

        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101
//
        const val MAX_POOL_SIZE = 30
    }

}
