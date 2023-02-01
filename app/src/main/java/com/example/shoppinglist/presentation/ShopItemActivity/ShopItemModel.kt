package com.example.shoppinglist.presentation.ShopItemActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.UpdateShopItemUseCase

class ShopItemModel: ViewModel() {


    private val repository = ShopListRepositoryImpl

    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
    get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _closeActivity = MutableLiveData<Unit>()
    val closeActivity: LiveData<Unit>
        get() = _closeActivity






    fun updateShopItem(inputName: String?, inputCount: String?){
        val name = inputName?.trim() ?: ""
        val count = try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: java.lang.Exception){
            0
        }
        if (validateInput(name, count)){
            _shopItem.value?.let {
                val item = it.copy(name= name, count= count)
                updateShopItemUseCase.updateShopItem(item)
                finishActivity() }

        }
    }

    fun addShopItem(inputName: String?, inputCount: String?){
        val name = inputName?.trim() ?: ""
        val count = try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: java.lang.Exception){
            0
        }
        if (validateInput(name, count)){
            addShopItemUseCase.addShopItem(ShopItem(name, count, true))
            finishActivity()


        }
    }

    fun getShopItem(shopItemId: Int){
        _shopItem.value = getShopItemUseCase.getShopItem(shopItemId)
    }

    private fun validateInput(name: String, count: Int): Boolean {
        return if (name.isBlank()) {
            _errorInputName.value = true
            false
        } else if (count <= 0){
            _errorInputCount.value = true
            false        }
        else {
            true
        }
    }

    fun resetErrorInputName(){
        _errorInputName.value = false
    }

    fun resetErrorInputCount(){
        _errorInputCount.value = false
    }

    private fun finishActivity(){
        _closeActivity.value = Unit
    }

}