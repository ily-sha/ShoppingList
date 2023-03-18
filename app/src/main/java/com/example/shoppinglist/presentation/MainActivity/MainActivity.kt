package com.example.shoppinglist.presentation.MainActivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.presentation.ShopItemActivity
import com.example.shoppinglist.presentation.ShopItemFragment
import com.example.shoppinglist.presentation.ShopListAdapter
import com.example.shoppinglist.presentation.ShopListAdapter.Companion.MAX_POOL_SIZE
import com.example.shoppinglist.presentation.ShopListAdapter.Companion.VIEW_TYPE_DISABLED
import com.example.shoppinglist.presentation.ShopListAdapter.Companion.VIEW_TYPE_ENABLED

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingItemFinishListener {



    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val shopListAdapter by lazy {
        ShopListAdapter()
    }
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
        binding.addNewItemButton.setOnClickListener {
            if (!isLandMode()) {
                val intent = ShopItemActivity.newIntentAdd(this)
                startActivity(intent)
            }
            else {
                launchShopItemFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }



    private fun launchShopItemFragment(fragment: ShopItemFragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun isLandMode(): Boolean = binding.shopItemFragmentContainer != null

    private fun setupRecyclerView() {
        with(binding.rvShopItem) {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_ENABLED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_DISABLED, MAX_POOL_SIZE)
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(binding.rvShopItem)
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
            if (isLandMode()) launchShopItemFragment(ShopItemFragment.newInstanceEditItem(it.id))
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

    override fun onEditingItemFinish() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }


}
