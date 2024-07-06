package com.example.constitutionofindia.data.repository

import com.example.constitutionofindia.data.database.BookmarkDatabase
import com.example.constitutionofindia.data.entity.Element_Bookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookmarkRepository(
    private val db : BookmarkDatabase
) {

    suspend fun insertBookmark(bookmark: Element_Bookmark) = db.bookmarkDao.insertBookmark(bookmark)

    suspend fun deleteBookmark(bookmark: Element_Bookmark) = db.bookmarkDao.deleteBookmark(bookmark)

    suspend fun deleteAllBookmarks() = db.bookmarkDao.deleteAllBookmarks()

    suspend fun deleteBookmark(id : Int) = db.bookmarkDao.deleteBookmark(id)

    fun getBookmark(bookmarkname : String) : Flow<List<Element_Bookmark>> = flow {
        emit(db.bookmarkDao.getBookmark(bookmarkname))
    }

    fun getAllBookmarks() : Flow<List<Element_Bookmark>> = flow {
        emit(db.bookmarkDao.getAllBookmarks())
    }

    fun getArticleBookmarks() : Flow<List<Element_Bookmark>> = flow {
        emit(db.bookmarkDao.getArticleBookmarks())
    }

    fun getScheduleBookmarks() : Flow<List<Element_Bookmark>> = flow {
        emit(db.bookmarkDao.getScheduleBookmarks())
    }
    fun getAmendmentBookmarks() : Flow<List<Element_Bookmark>> = flow {
        emit(db.bookmarkDao.getAmendmentBookmarks())
    }
}