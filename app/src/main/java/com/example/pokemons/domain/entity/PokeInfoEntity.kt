package com.example.pokemons.domain.entity

data class PokeInfoEntity(
    val name: String,
    val number: Int,
    val weight: Int,
    val height: Int,
    val stats: List<StatEntity>,
    val types: List<TypeEntity>
)

data class StatEntity(
    val baseStat: Int,
    val stat: StatXEntity
)

data class StatXEntity(
    val name: String
)

data class TypeEntity(
    val type: TypeXEntity
)

data class TypeXEntity(
    val name: String
)
