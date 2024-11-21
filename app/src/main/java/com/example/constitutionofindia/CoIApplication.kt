package com.example.constitutionofindia

import android.app.Application
import android.content.Context
import com.example.constitutionofindia.data.database.BookmarkDatabase
import com.example.constitutionofindia.data.repository.BookmarkRepository

class CoIApplication : Application() {

    companion object {

        lateinit var bookmarkDB: BookmarkDatabase
        lateinit var repository: BookmarkRepository
        lateinit var appContext : Context
        lateinit var assetManager : AssetManager

    }

    override fun onCreate() {
        super.onCreate()
        AdManager().adsInitialize(this)

        appContext = applicationContext

        bookmarkDB = BookmarkDatabase(this)
        repository = BookmarkRepository(bookmarkDB)

        assetManager = AssetManager()

    }

}