package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData
import java.util.TreeSet

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>> {
        return shopListRepository.getShopList()
    }
}