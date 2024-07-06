package com.example.constitutionofindia.data.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {

    @TypeConverter
    fun saveIntList(list: List<String>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun getIntList(list: String): List<String> {
        return Gson().fromJson(
            list,
            object : TypeToken<List<String>>() {}.type
        )
    }
}