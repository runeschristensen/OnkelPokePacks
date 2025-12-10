package com.onkelpoke.unclepokepacks.backend

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PackManager {

    private const val PREFS_NAME = "chaos_pack_prefs"
    private const val PACKS_KEY = "packs_map"

    private val gson = Gson()

    // Map of pack name -> available amount
    private val packs = mutableMapOf<String, Int>()

    // -----------------------
    // Core Pack Logic
    // -----------------------

    fun addPack(name: String, amount: Int = 1) {
        if (amount < 0) return
        packs[name] = (packs[name] ?: 0) + amount
    }

    fun tryRemovePack(name: String, amount: Int = 1): Boolean {
        val current = packs[name] ?: return false
        return if (hasEnough(name, amount)) {
            packs[name] = current - amount
            true
        } else {
            false
        }
    }

    fun getAmount(name: String): Int = packs[name] ?: 0

    fun hasEnough(name: String, amount: Int): Boolean {
        if (amount < 0) return false
        val current = packs[name] ?: return false
        return current >= amount
    }

    fun getAllPacks(): Map<String, Int> = packs.toMap()

    fun getAllPackNames(): List<String> = packs.keys.toList()

    fun getAvailablePackNames(): List<String> =
        packs.filter { it.value > 0 }.keys.toList()

    fun selectRandomAvailablePack(): String? {
        val available = getAvailablePackNames()
        if (available.isEmpty()) return null
        return available.random()
    }

    fun selectPackByName(name: String, amount: Int = 1): String? {
        return if (hasEnough(name, amount)) name else null
    }

    // -----------------------
    // Persistence
    // -----------------------

    fun save(context: Context) {
        Log.d("packmon", "saving")
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(packs)
        prefs.edit().putString(PACKS_KEY, json).apply()
    }

    fun load(context: Context) {
        Log.d("packmon", "loading")
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(PACKS_KEY, null)

        if (json == null) {
            packs.clear()
            return
        }

        val type = object : TypeToken<MutableMap<String, Int>>() {}.type
        val loadedMap: MutableMap<String, Int> = gson.fromJson(json, type)

        packs.clear()
        packs.putAll(loadedMap)


    }
}




