package com.onkelpoke.onkelpokeprod.Scripts

data class PokemonTCGPack(
    val id: String,
    val name: String,
    val releaseDate: String,
    val imageResId: Int,
    var count: Int
)
