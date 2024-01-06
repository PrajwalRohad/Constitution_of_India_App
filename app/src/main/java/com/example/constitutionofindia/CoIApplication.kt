package com.example.constitutionofindia

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.example.constitutionofindia.data.database.BookmarkDatabase
import com.example.constitutionofindia.data.repository.BookmarkRepository

class CoIApplication : Application() {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!


    companion object {

        lateinit var bookmarkDB: BookmarkDatabase
        lateinit var repository: BookmarkRepository
        lateinit var appContext : Context
        lateinit var assetManager : AssetManager

    }

    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
        AdManager().adsInitialize(this)

        appContext = applicationContext

        bookmarkDB = BookmarkDatabase(this)
        repository = BookmarkRepository(bookmarkDB)

        assetManager = AssetManager()

//        UserLocalData().clearData(appContext)


    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    override fun onLowMemory() {
        super.onLowMemory()
    }

}