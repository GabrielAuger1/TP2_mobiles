package com.example.tp2_14d_mobiles.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp2_14d_mobiles.model.Item

class HomeViewModel : ViewModel() {

    var selectedItems: List<Item> = emptyList()
    var quantities: List<Int> = emptyList()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}