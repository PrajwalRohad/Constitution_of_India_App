package com.example.constitutionofindia.bookmarks.bookmarksViewModel

import androidx.lifecycle.ViewModel
import com.example.constitutionofindia.data.entity.Element_Bookmark
import com.example.constitutionofindia.data.repository.BookmarkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarkViewModel(
    private val repository: BookmarkRepository
) : ViewModel() {


    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()


    fun insertBookmark(bookmark: Element_Bookmark) = CoroutineScope(Dispatchers.IO).launch {
        repository.insertBookmark(bookmark)
    }

    fun deleteBookmark(bookmarkname: String) = CoroutineScope(Dispatchers.IO).launch {
        repository.deleteBookmark(bookmarkname)
    }

    fun deleteALLBookmarks() = CoroutineScope(Dispatchers.IO).launch {
        repository.deleteAllBookmarks()
    }

    suspend fun getBookmark(bookmarkname : String) : List<Element_Bookmark> {
        return repository.getBookmark(bookmarkname).flattenToList()
    }

    fun getAllBookmarks() : Flow<List<Element_Bookmark>> {

        return repository.getAllBookmarks()
    }

    fun getArticleBookmarks() : Flow<List<Element_Bookmark>> {
        return repository.getArticleBookmarks()
    }

    fun getScheduleBookmarks() : Flow<List<Element_Bookmark>> {
        return repository.getScheduleBookmarks()
    }

    fun getAmendmentBookmarks() : Flow<List<Element_Bookmark>> {
        return repository.getAmendmentBookmarks()
    }

}