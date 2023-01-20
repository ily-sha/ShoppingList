package com.example.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.*

class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun updateShopItem(shopItem: ShopItem){
        val newItem = shopItem.copy(enable = !shopItem.enable)
        updateShopItemUseCase.updateShopItem(newItem)
    }

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }
}