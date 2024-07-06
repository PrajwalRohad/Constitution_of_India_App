package com.example.constitutionofindia.bookmarks.stateComposeMethod

import com.example.constitutionofindia.data.entity.Element_Bookmark


sealed interface BookmarkEvent {
    data class SaveBookmark(val bookmark : Element_Bookmark) : BookmarkEvent
    data class DeleteBookmark(val bookmark : Element_Bookmark) : BookmarkEvent

    data class FilteredBookmark(val filterType : BookmarkFilterType) : BookmarkEvent

}