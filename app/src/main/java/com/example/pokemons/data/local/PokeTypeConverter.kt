package com.example.pokemons.data.local

import androidx.room.TypeConverter
import com.example.pokemons.data.model.Stat
import com.example.pokemons.data.model.Type
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PokeTypeConverter {

    @TypeConverter
    fun fromStatList(statList: List<Stat>): String {
        val gson = Gson()
        return gson.toJson(statList)
    }

    @TypeConverter
    fun toStatList(statListString: String): List<Stat> {
        val gson = Gson()
        val type = object : TypeToken<List<Stat>>() {}.type
        return gson.fromJson(statListString, type)
    }

    @TypeConverter
    fun fromTypesList(typesList: List<Type>): String {
        val gson = Gson()
        return gson.toJson(typesList)
    }

    @TypeConverter
    fun toTypesList(typesListString: String): List<Type> {
        val gson = Gson()
        val type = object : TypeToken<List<Type>>() {}.type
        return gson.fromJson(typesListString, type)
    }
}