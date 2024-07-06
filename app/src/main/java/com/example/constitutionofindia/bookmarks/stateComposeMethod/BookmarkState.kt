package com.example.constitutionofindia.bookmarks.stateComposeMethod

import com.example.constitutionofindia.data.entity.Element_Bookmark

data class BookmarkState(
    val bookmarks : List<Element_Bookmark> = emptyList(),
    val filterType: BookmarkFilterType = BookmarkFilterType.TYPE_ALL
)
