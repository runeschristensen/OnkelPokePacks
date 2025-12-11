package com.onkelpoke.onkelpokeprod.ui.home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.onkelpoke.onkelpokeprod.R
import com.onkelpoke.onkelpokeprod.Scripts.PackRepository
import com.onkelpoke.onkelpokeprod.Scripts.PokemonTCGPack


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("chaos_pack_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val type = object : TypeToken<MutableMap<String, PokemonTCGPack>>() {}.type

    // LiveData med alle pakker
    private val _packs = MutableLiveData<MutableMap<String, PokemonTCGPack>>()
    val packs: LiveData<MutableMap<String, PokemonTCGPack>> = _packs


    init {
        loadPacks()
        Log.d("init", "through init")
    }


    // --------------------------
    // Mutations (som PackManager)
    // --------------------------

    fun addPack(pack: PokemonTCGPack) {
        val map = _packs.value ?: mutableMapOf()
        map[pack.name] = pack
        _packs.value = map
        savePacks()
    }

    fun updateAmount(name: String, delta: Int) {
        val map = _packs.value ?: return
        val p = map[name] ?: return
        val newPack = p.copy(count = (p.count + delta).coerceAtLeast(0))
        map[name] = newPack
        _packs.value = map
        savePacks()
    }

    fun getRandomAvailablePack(): PokemonTCGPack? {
        val available = _packs.value?.values?.filter { it.count > 0 } ?: return null
        return available.random()
    }


    // --------------------------
    // Persistence
    // --------------------------

    private fun savePacks() {
        val json = gson.toJson(_packs.value)
        prefs.edit().putString("packs_map", json).apply()
    }

    private fun loadPacks() {
        val json = prefs.getString("packs_map", null)
        Log.d("loadpacks", "hit load")
        if (json == null) {
            _packs.value = mutableMapOf()
            return
        }

        val map: MutableMap<String, PokemonTCGPack> = gson.fromJson(json, type)
        _packs.value = map
    }
}