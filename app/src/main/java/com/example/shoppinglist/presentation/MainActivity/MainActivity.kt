package com.example.shoppinglist.presentation.MainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.presentation.ShopItemActivity.ShopItemActivity
import com.example.shoppinglist.presentation.ShopItemFragment
import com.example.shoppinglist.presentation.ShopListAdapter
import com.example.shoppinglist.presentation.ShopListAdapter.Companion.MAX_POOL_SIZE
import com.example.shoppinglist.presentation.ShopListAdapter.Companion.VIEW_TYPE_DISABLED
import com.example.shoppinglist.presentation.ShopListAdapter.Companion.VIEW_TYPE_ENABLED
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class MainActivity : AppCompatActivity(){


    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemFragmentContainer: FragmentContainerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        File(getCacheDir(), "tfs.txt").createNewFile()
        shopItemFragmentContainer = findViewById(R.id.shop_item_fragment_container)
        val addNewItemButton = findViewById<FloatingActionButton>(R.id.add_new_item_button)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
        addNewItemButton.setOnClickListener {
            if (!isLandScapeMode()) {
                val intent = ShopItemActivity.newIntentAdd(this)
                startActivity(intent)
            }
            else {
                launchShopItemFragment(ShopItemFragment.newInstanceAddItem())
            }
        }


    }



    private fun launchShopItemFragment(fragment: ShopItemFragment){
        supportFragmentManager.beginTransaction().add(R.id.shop_item_fragment_container,
            fragment).commit()
    }

    private fun isLandScapeMode(): Boolean = shopItemFragmentContainer != null

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_item)
        with(rvShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_ENABLED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_DISABLED, MAX_POOL_SIZE)
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
            0,
            androidx.recyclerview.widget.ItemTouchHelper.LEFT or androidx.recyclerview.widget.ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if (isLandScapeMode()) launchShopItemFragment(ShopItemFragment.newInstanceEditItem(it.id))
            else {
                val intent = ShopItemActivity.newIntentEdit(this, it.id)
                startActivity(intent)
            }


        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.updateShopItem(it)
        }
    }

    companion object {
        var count = 0
    }


}
