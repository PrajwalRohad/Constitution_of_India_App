package com.example.constitutionofindia.amendments

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.AdManager
import com.example.constitutionofindia.ThemePreference
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Activity_Amendment_SOR : AppCompatActivity() {
    lateinit var Activity_Amendment_SOR_BannerAd : AdView
    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    lateinit var CoI_SharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_amendment_sor)


        val text : String?
        intent.extras.also {
            text = it?.getString("SORtext")
        }
        findViewById<TextView>(R.id.activity_amendment_sor_svtvtext).also {
            it.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY))
        }

    }

    override fun attachBaseContext(newBase: Context) {
        val sharedpref = newBase.getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        var fontsize1 = 1.0f
        if (sharedpref != null) {
            fontsize1 = 0.5f + (0.25f * sharedpref.getInt(FONT_SIZE, 1))
        }

        super.attachBaseContext(ThemePreference().adjustFontScale(newBase, fontsize1))
    }
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO){

            Activity_Amendment_SOR_BannerAd = findViewById(R.id.activity_amendment_sor_adView)
            withContext(Dispatchers.Main){
                AdManager().loadBannerAd(Activity_Amendment_SOR_BannerAd)
            }
        }

    }

    override fun onDestroy() {
        Activity_Amendment_SOR_BannerAd.removeAllViews()
        Activity_Amendment_SOR_BannerAd.destroy()
        super.onDestroy()
    }
}