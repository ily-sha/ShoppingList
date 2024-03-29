package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingItemFinishListener {

    private var mode = INDEFINITE_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        if (savedInstanceState == null) launchFragment()
    }

    private fun launchFragment() {
        val fragment = when (mode) {
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            else -> throw RuntimeException("Unknown screen mode $mode")
        }
        supportFragmentManager.beginTransaction().add(R.id.shop_item_fragment_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) throw RuntimeException("Param extra screen mode is absent")
        mode = intent.getStringExtra(EXTRA_SCREEN_MODE).toString()
        if (mode != MODE_ADD && mode != MODE_EDIT) throw RuntimeException("Unknown screen mode $mode")
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) throw RuntimeException("param extra shop item is absent")
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"
        private const val INDEFINITE_MODE = ""

        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEdit(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(
                EXTRA_SCREEN_MODE, MODE_EDIT
            )
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }

    override fun onEditingItemFinish() {
        finish()
    }

}