package com.example.constitutionofindia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.constitutionofindia.data.entity.Element_Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookmark(bookmark: Element_Bookmark)

    @Query("DELETE FROM 'bookmarks_table' WHERE name = :bookmarkname")
    suspend fun deleteBookmark(bookmarkname: String)

    @Query("DELETE FROM 'bookmarks_table'")
    suspend fun deleteAllBookmarks()

    @Query("DELETE FROM 'bookmarks_table' WHERE id = :bookmarkid")
    suspend fun deleteBookmark(bookmarkid : Int)

    @Query("SELECT * FROM 'bookmarks_table' WHERE name = :bookmarkname")
    fun getBookmark(bookmarkname : String) : List<Element_Bookmark>

    @Query("SELECT * FROM 'bookmarks_table' ORDER BY id ASC")
    fun getAllBookmarks() : List<Element_Bookmark>

    @Query("SELECT * FROM 'bookmarks_table' WHERE type = 0 ORDER BY id ASC")
    fun getArticleBookmarks() : List<Element_Bookmark>

    @Query("SELECT * FROM 'bookmarks_table' WHERE type = 1 ORDER BY id ASC")
    fun getScheduleBookmarks() : List<Element_Bookmark>

    @Query("SELECT * FROM 'bookmarks_table' WHERE type = 2 ORDER BY id ASC")
    fun getAmendmentBookmarks() : List<Element_Bookmark>

}
