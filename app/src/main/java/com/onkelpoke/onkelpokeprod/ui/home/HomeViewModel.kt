package com.onkelpoke.onkelpokeprod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Onkel Poke's pack battle arena spectacularrrrr yarrr"
    }

    val text: LiveData<String> = _text
    val white_flare: Int = R.drawable.white_flare_background
}