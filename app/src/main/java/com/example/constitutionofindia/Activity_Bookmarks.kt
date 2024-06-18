package com.example.constitutionofindia

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.IndiaCanon.constitutionofindia.R
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class Activity_Bookmarks : AppCompatActivity() {

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"

    lateinit var CoI_SharedPref: SharedPreferences

    lateinit var Activity_Bookmarks_BannerAd : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode =
            CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)

        setContentView(R.layout.activity_bookmarks)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_activity_bookmarks)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onResume() {
        super.onResume()

        MobileAds.initialize(this@Activity_Bookmarks)
        Activity_Bookmarks_BannerAd = findViewById(R.id.activity_bookmarks_adView)

        AdManager().loadBannerAd(Activity_Bookmarks_BannerAd)

    }

}