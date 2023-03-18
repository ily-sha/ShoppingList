package com.example.shoppinglist.presentation

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

interface OnButtonClickListener {
    fun onButtonClickListener()
}

interface OnTextChangedListener {
    fun onNameTextChangedListener(opt: Int)
    fun onCountTextChangedListener(opt: Int)

}
@BindingAdapter("onButtonSaveClicked")
fun bindButtonSave(button: Button, function: OnButtonClickListener){
    button.setOnClickListener {
        function.onButtonClickListener()
    }
}

@BindingAdapter("shopItemName")
fun bindItemName(textView: TextView, shopItem: ShopItem?){
    if (shopItem != null){
        textView.text = shopItem.name
    }
}

@BindingAdapter("shopItemCount")
fun bindShopItemCount(textView: TextView, shopItem: ShopItem?){
    if (shopItem != null){
        textView.text = shopItem.count.toString()
    }
}

@BindingAdapter("underlineNameError")
fun bindTvNameWithError(textContainer: TextInputLayout, state:  Boolean){
    if (state) textContainer.error = "Invalid name"
    else textContainer.error = null
}

@BindingAdapter("underlineCountError")
fun bindTvCountWithError(textContainer: TextInputLayout, state: Boolean){
    if (state) textContainer.error = "Invalid count"
    else textContainer.error = null
}

@BindingAdapter("addNameTextChangedListener")
fun bindAddNameTextChangedListener(textView: TextInputEditText, function: () -> Unit){
    textView.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            function.invoke()
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    })

}

@BindingAdapter("addCountTextChangedListener")
fun bindAddCountTextChangedListener(textView: TextInputEditText, function: () -> Unit){
    textView.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            function.invoke()
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    })
}