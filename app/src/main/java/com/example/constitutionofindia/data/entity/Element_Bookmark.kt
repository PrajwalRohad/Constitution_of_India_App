package com.example.constitutionofindia.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "bookmarks_table")
data class Element_Bookmark (
    val type : Int,
    val name : String,
    @field:TypeConverters(ListConverter::class)
    val data : List<String>,
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0
){
    companion object {
        const val TYPE_ARTICLE = 0
        const val TYPE_SCHEDULE = 1
        const val TYPE_AMENDMENT = 2
    }
}