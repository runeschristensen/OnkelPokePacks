package com.onkelpoke.onkelpokeprod.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onkelpoke.onkelpokeprod.R

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Onkel Poke's pack battle arena spectacularrrrr yarrr"
    }

    val text: LiveData<String> = _text
    val whiteflare: Int = R.drawable.white_flare_background
}