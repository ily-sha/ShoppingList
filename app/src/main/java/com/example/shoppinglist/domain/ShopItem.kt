package com.example.shoppinglist.domain

import androidx.lifecycle.LifecycleObserver

data class ShopItem(
    val name: String,
    val count: Int,
    val enable: Boolean,
    var id: Int = UNDEFINED_ID
) {

    companion object {

        const val UNDEFINED_ID = -1
    }
}




