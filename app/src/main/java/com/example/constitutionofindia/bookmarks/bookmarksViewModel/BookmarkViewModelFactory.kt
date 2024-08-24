package com.example.constitutionofindia.bookmarks.bookmarksViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.constitutionofindia.data.repository.BookmarkRepository

@Suppress("UNCHECKED_CAST")
class BookmarkViewModelFactory(
    private val repository: BookmarkRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookmarkViewModel(repository) as T
    }

}