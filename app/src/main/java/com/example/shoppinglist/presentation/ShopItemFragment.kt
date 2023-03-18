package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.databinding.ShopItemFragmentBinding
import com.example.shoppinglist.domain.ShopItem

class ShopItemFragment : Fragment() {
    lateinit var shopItemViewModel: ShopItemModel


    private var screenMode: String = INDEFINITE_MODE
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    private var _binding: ShopItemFragmentBinding? = null
    private val binding: ShopItemFragmentBinding
        get() = _binding ?: throw RuntimeException("ShopItemFragmentBinding in null")


    private lateinit var onEditingItemFinish: OnEditingItemFinishListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingItemFinishListener) {
            onEditingItemFinish = context
        } else {
            throw RuntimeException("Activity which use ShopItemFragment must implements OnEditingItemFinishListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Fragment", "onCreate")
        parseFragmentParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Fragment", "onCreateView")
        _binding = ShopItemFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("Fragment", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        shopItemViewModel = ViewModelProvider(this)[ShopItemModel::class.java]
        binding.viewModel = shopItemViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
        setUpObservers()
    }

    override fun onStart() {
        super.onStart()
        Log.d("Fragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Fragment", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Fragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Fragment", "onStop")

    }


    private fun setUpObservers() {
        shopItemViewModel.closeActivity.observe(viewLifecycleOwner) {
            onEditingItemFinish.onEditingItemFinish()
        }
    }

    private fun launchAddMode() {
        binding.buttonSave.setOnClickListener {
            shopItemViewModel.addShopItem(
                binding.textInputEditTextName.text?.toString(),
                binding.textInputEditTextCount.text?.toString()
            )
        }
    }

    private fun launchEditMode() {
        binding.buttonSave.setOnClickListener {
            shopItemViewModel.updateShopItem(
                binding.textInputEditTextName.text?.toString(),
                binding.textInputEditTextCount.text?.toString()
            )
        }
        shopItemViewModel.getShopItem(shopItemId)
    }

    private fun parseFragmentParams() {
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

    interface OnEditingItemFinishListener {
        fun onEditingItemFinish()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("Fragment", "onDestroyView")
    }

    override fun onDestroy() {
        Log.d("Fragment", "onDestroy $view")
        super.onDestroy()


    }

    override fun onDetach() {
        super.onDetach()
        Log.d("Fragment", "onDetach")

    }
}


