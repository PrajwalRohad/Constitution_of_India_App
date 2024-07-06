package com.example.constitutionofindia.bookmarks.bookmarksViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.constitutionofindia.bookmarks.stateComposeMethod.BookmarkEvent
import com.example.constitutionofindia.bookmarks.stateComposeMethod.BookmarkState
import com.example.constitutionofindia.bookmarks.stateComposeMethod.BookmarkFilterType
import com.example.constitutionofindia.data.entity.Element_Bookmark
import com.example.constitutionofindia.data.repository.BookmarkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
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

    fun deleteBookmark(bookmark: Element_Bookmark) = CoroutineScope(Dispatchers.IO).launch {
        repository.deleteBookmark(bookmark)
    }

    fun deleteALLBookmarks() = CoroutineScope(Dispatchers.IO).launch {
        repository.deleteAllBookmarks()
    }

    suspend fun getBookmark(bookmarkname : String) : List<Element_Bookmark> {
        return repository.getBookmark(bookmarkname).flattenToList()
    }

    fun getAllBookmarks() : Flow<List<Element_Bookmark>> {
//        val currentlist : List<Element_Bookmark> = repository.getAllBookmarks().flattenToList()
//        return currentlist

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


//    private val _filterType = MutableStateFlow(BookmarkFilterType.TYPE_ALL)
//
//
//    private val _bookmarks = _filterType.flatMapLatest { filtertype ->
//        Log.v("DatabaseCheck", "latest filter type = $filtertype")
//        when (filtertype) {
//                BookmarkFilterType.TYPE_ALL -> repository.getAllBookmarks()
//                BookmarkFilterType.TYPE_ARTICLE -> repository.getArticleBookmarks()
//                BookmarkFilterType.TYPE_SCHEDULE -> repository.getScheduleBookmarks()
//                BookmarkFilterType.TYPE_AMENDMENT -> repository.getAmendmentBookmarks()
//            }
//        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList()) // this done to save above in state
//
//
//    private val _state = MutableStateFlow(BookmarkState())
//    val state = combine(_state, _bookmarks, _filterType) { newstate, newbookmarks, newfiltertype ->
//        newstate.copy(
//            bookmarks = newbookmarks,
//            filterType = newfiltertype
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), BookmarkState())
//
//
//    fun onEvent(event: BookmarkEvent) {
//        when (event) {
//            is BookmarkEvent.FilteredBookmark -> {
//                _filterType.value = event.filterType
//                Log.v("DatabaseCheck", "filter type changed to = "+_filterType.value)
//                Log.v("DatabaseCheck", "bookmark type changed to = "+_bookmarks.value.size)
//            }
//
//            is BookmarkEvent.DeleteBookmark -> {
//                viewModelScope.launch {
//                    repository.deleteBookmark(event.bookmark)
//                }
//            }
//            is BookmarkEvent.SaveBookmark -> {
//                viewModelScope.launch {
//                    repository.insertBookmark(event.bookmark)
//                }
//            }
//        }
//    }

}