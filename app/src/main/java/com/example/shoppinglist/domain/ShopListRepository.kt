package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData
import java.util.TreeSet

interface ShopListRepository {
    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun getShopItem(shopItemId: Int): ShopItem
    fun getShopList(): LiveData<TreeSet<ShopItem>>
    fun updateShopItem(shopItem: ShopItem)

}