package com.example.constitutionofindia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.constitutionofindia.data.dao.BookmarkDao
import com.example.constitutionofindia.data.entity.Element_Bookmark
import com.example.constitutionofindia.data.entity.ListConverter


@Database(
    entities = [Element_Bookmark::class],
    version = 1
)
@TypeConverters(ListConverter::class)
abstract class BookmarkDatabase : RoomDatabase() {

    abstract val bookmarkDao : BookmarkDao


    companion object {

        @Volatile
        private var instance : BookmarkDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BookmarkDatabase::class.java,
                "BookmarkDB.db",
            ).build()
    }

}