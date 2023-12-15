package com.example.constitutionofindia.preamble

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.ThemePreference
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import org.json.JSONObject


class Activity_Preamble : AppCompatActivity() {
    lateinit var Activity_Preamble_BannerAd : AdView

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"

    lateinit var CoI_SharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeDefault)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_preamble)

        MobileAds.initialize(this){}
        val Activity_Preamble_BannerAdRequest = AdRequest.Builder().build()

        Activity_Preamble_BannerAd = findViewById(R.id.activity_preamble_adView)
        Activity_Preamble_BannerAd.loadAd(Activity_Preamble_BannerAdRequest)


        val jpreamblefile = applicationContext.assets.open("preamble.json").bufferedReader().use {
            it.readText()
        }
        val jpreambleobj = JSONObject(jpreamblefile)
        val preambletext = jpreambleobj.getString("text")
        val preamblefootnote = jpreambleobj.getString("footnote")



        findViewById<TextView>(R.id.activity_preamble_tvtext).also {
            it.setText(Html.fromHtml(preambletext, Html.FROM_HTML_MODE_LEGACY))
//            it.setText(Html.fromHtml(resources.getText(R.string.PreambleText).toString(), Html.FROM_HTML_MODE_LEGACY))
//            it.setText(resources.getText(R.string.PreambleText))
        }
        findViewById<TextView>(R.id.activity_preamble_tvfootnote).also {
            it.setText(Html.fromHtml(preamblefootnote, Html.FROM_HTML_MODE_LEGACY))
//            it.setText(R.string.PreambleFootnote)
        }


    }
}