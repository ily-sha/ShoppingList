package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.ShopListAdapter.Companion.MAX_POOL_SIZE

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = MainViewModel()
        setUpRecyclerView()
        viewModel.shopList.observe(this) {
            shopListAdapter.listOfItems = it.toList()
        }



    }
    private fun setUpRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.rv_shop_item)
        shopListAdapter = ShopListAdapter()
        recyclerView.apply {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(R.layout.shop_item_disabled, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(R.layout.shop_item_enabled, MAX_POOL_SIZE)

        }


    }


}