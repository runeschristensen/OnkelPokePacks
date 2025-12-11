package com.onkelpoke.onkelpokeprod.Scripts

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PackRepository {

    private const val PREFS_NAME = "pack_prefs"
    private const val KEY_PACKS = "packs_json"

    private val gson = Gson()
    private var packs = mutableListOf<PokemonTCGPack>()

    // Hvis true: gem automatisk efter enhver ændring
    var autoSave = true


    // -------------------------------------------------
    // Load + Save
    // -------------------------------------------------

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_PACKS, null)

        packs = if (json.isNullOrEmpty()) {
            createInitialPacks().toMutableList()

        } else {
            val type = object : TypeToken<List<PokemonTCGPack>>() {}.type
            gson.fromJson(json, type)
        }
        Log.d("load", "hit load")
        if (json.isNullOrEmpty()) save(context)
    }

    fun save(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PACKS, gson.toJson(packs)).apply()
    }


    // -------------------------------------------------
    // Read Operations
    // -------------------------------------------------

    fun getAllPacks(): List<PokemonTCGPack> = packs

    fun getAvailablePacks(): List<PokemonTCGPack> =
        packs.filter { it.count > 0 }

    fun drawRandomPack(): PokemonTCGPack? {
        val available = getAvailablePacks()
        if (available.isEmpty()) return null

        val chosen = available.random()
        chosen.count--

        return chosen
    }

    fun addNewPack(
        id: String,
        name: String,
        releaseDate: String,
        imageResId: Int,
        initialCount: Int = 0,
        context: Context
    ): Boolean {
        if (packs.any { it.id == id }) return false

        packs.add(
            PokemonTCGPack(
                id = id,
                name = name,
                releaseDate = releaseDate,
                imageResId = imageResId,
                count = initialCount
            )
        )

        if (autoSave) save(context)
        return true
    }

    fun incrementPack(id: String, amount: Int = 1, context: Context): Boolean {
        val pack = packs.find { it.id == id } ?: return false
        if (amount < 0) return false

        pack.count += amount
        if (autoSave) save(context)
        return true
    }

    fun setPackCount(id: String, newCount: Int, context: Context): Boolean {
        val pack = packs.find { it.id == id } ?: return false
        if (newCount < 0) return false

        pack.count = newCount
        if (autoSave) save(context)
        return true
    }

    fun removePack(id: String, context: Context): Boolean {
        val removed = packs.removeIf { it.id == id }
        if (removed && autoSave) save(context)
        return removed
    }


    // -------------------------------------------------
    // Initial seed data
    // -------------------------------------------------
    private fun createInitialPacks() = listOf(
        PokemonTCGPack(
            id = "black_bolt",
            name = "Black Bolt",
            releaseDate = "2025",
            imageResId = 0, // skift når du får et rigtigt billede
            count = 2
        )
    )
}