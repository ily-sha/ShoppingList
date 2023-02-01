package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.MainActivity.MainActivity
import com.example.shoppinglist.presentation.ShopItemActivity.ShopItemActivity
import com.example.shoppinglist.presentation.ShopItemActivity.ShopItemModel
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment: Fragment() {
    lateinit var shopItemViewModel : ShopItemModel
    private lateinit var buttonSave: Button
    private lateinit var textInputLayoutCount: TextInputLayout
    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputEditTextName: EditText
    private lateinit var textInputEditTextCount: EditText

    private var screenMode: String = INDEFINITE_MODE
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseFragmentParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shop_item_fragment, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        shopItemViewModel = ViewModelProvider(this)[ShopItemModel::class.java]
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
        setUpObservers()
        addEditTextListener()
    }



    private fun addEditTextListener() {
        textInputEditTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        textInputEditTextCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun setUpObservers() {
        shopItemViewModel.closeActivity.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner) {
            if (it) textInputLayoutName.error = "error"
            else textInputLayoutName.error = null
        }
        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner) {
            if (it) textInputLayoutCount.error = "error"
            else textInputLayoutCount.error = null
        }
    }

    private fun launchAddMode(){
        buttonSave.setOnClickListener {
            shopItemViewModel.addShopItem(textInputEditTextName.text?.toString(),
                textInputEditTextCount.text?.toString())
        }
    }

    private fun launchEditMode(){
        buttonSave.setOnClickListener {
            shopItemViewModel.updateShopItem(textInputEditTextName.text?.toString(),
                textInputEditTextCount.text?.toString())
        }
        shopItemViewModel.shopItem.observe(viewLifecycleOwner) {
            textInputEditTextName.setText(it.name)
            textInputEditTextCount.setText(it.count.toString())
        }
        shopItemViewModel.getShopItem(shopItemId)
    }

    private fun parseFragmentParams(){
        val arg = requireArguments()
        if (!arg.containsKey(SCREEN_MODE)) throw RuntimeException("Param extra screen mode is absent")
        if (arg.getString(SCREEN_MODE) != MODE_ADD && arg.getString(SCREEN_MODE) != MODE_EDIT)
            throw RuntimeException("Unknown screen mode $screenMode")
        screenMode = arg.getString(SCREEN_MODE)!!
        if (screenMode == MODE_EDIT) {
            if (!arg.containsKey(SHOP_ITEM_ID)) throw RuntimeException("param extra shop item id is absent")
            shopItemId = arg.getInt(SHOP_ITEM_ID)
        }



    }

    private fun initViews(view: View){
        textInputLayoutCount = view.findViewById(R.id.textInputLayoutCount)
        textInputLayoutName = view.findViewById(R.id.textInputLayoutName)
        textInputEditTextCount = view.findViewById(R.id.textInputEditTextCount)
        buttonSave = view.findViewById(R.id.button_save)
        textInputEditTextName = view.findViewById(R.id.textInputEditTextName)
    }


    companion object {
        private const val SCREEN_MODE = "scree_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val SHOP_ITEM_ID = "shop_item_id"
        private const val INDEFINITE_MODE = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }

    }
}


