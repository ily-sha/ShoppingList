package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import java.util.Random
import java.util.TreeSet

object ShopListRepositoryImpl: ShopListRepository {
    private val shopList = sortedSetOf<ShopItem>(
        Comparator { t, t2 -> t.id.compareTo(t2.id) })

    private val shopListLiveData = MutableLiveData<TreeSet<ShopItem>>()

//    private var autoIncrement = 0;

    init {
        for (i in 0..1000){
            addShopItem(ShopItem(i, "name $i", i, kotlin.random.Random.nextBoolean()))

        }
    }

    override fun addShopItem(shopItem: ShopItem) {
//        shopItem.id = autoIncrement++
        shopList.add(shopItem)
        shopListLiveData.value = shopList
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        shopListLiveData.value = shopList
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId } ?: throw Exception("not found")
    }

    override fun getShopList(): LiveData<TreeSet<ShopItem>> {
        return shopListLiveData
    }

    override fun updateShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)
        shopList.remove(oldItem)
        shopList.add(shopItem)
        shopListLiveData.value = shopList
    }
}