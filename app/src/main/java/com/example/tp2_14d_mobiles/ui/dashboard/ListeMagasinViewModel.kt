package com.example.tp2_14d_mobiles.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp2_14d_mobiles.model.Item

class ListeMagasinViewModel : ViewModel() {

    private val mItem = MutableLiveData<Item>()

    var selectedItems: List<Item> = emptyList()
    var quantities: List<Int> = emptyList()

    var item: LiveData<Item>
        get() = mItem
        set(item) {
            mItem.setValue(item.value)
        }
    fun setItem(item: Item) {
        mItem.setValue(item)
    }
}