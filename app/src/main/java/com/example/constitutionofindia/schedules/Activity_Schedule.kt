package com.example.constitutionofindia.schedules

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.ThemePreference
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Activity_Schedule : AppCompatActivity() {
    lateinit var Activity_Schedule_BannerAd : AdView

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    lateinit var CoI_SharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        lifecycleScope.launch(Dispatchers.IO){
//            CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
//            val nightmode =
//                CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//            AppCompatDelegate.setDefaultNightMode(nightmode)
//            val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeDefault)
//
//            withContext(Dispatchers.Main){
//                ThemePreference().changeThemeStyle(this@Activity_Schedule, themeselected)
//            }
//        }
        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_schedule)


        val name : String?

        intent.extras.also {
            name = it?.getString("scheduleName")
        }

        lifecycleScope.launch(Dispatchers.Default){
            val jschedulefile : String = applicationContext.assets.open("schedules.json").bufferedReader().use {
                it.readText()
            }

            val jscheduleobj = JSONObject(jschedulefile)
            val schedulenum = jscheduleobj.getJSONObject(name).getString("num")
            val schedulename = jscheduleobj.getJSONObject(name).getString("name")
            val scheduletext = jscheduleobj.getJSONObject(name).getString("text")
            val scheduleArt = jscheduleobj.getJSONObject(name).getString("articlesRelated")
            val schedulefootnote = jscheduleobj.getJSONObject(name).getString("footnote")

            withContext(Dispatchers.Main){
                findViewById<TextView>(R.id.activity_schedule_cvtvHeadline).also {
                    it.setText(Html.fromHtml(schedulenum+"<br></br>"+schedulename, Html.FROM_HTML_MODE_LEGACY))
                }
                findViewById<TextView>(R.id.activity_schedule_cvtvArticlesNum).also {
                    it.setText(Html.fromHtml(scheduleArt, Html.FROM_HTML_MODE_LEGACY))
                }
                findViewById<TextView>(R.id.activity_schedule_tvtext).also {
                    it.setText(Html.fromHtml(scheduletext, Html.FROM_HTML_MODE_LEGACY))
                }
                findViewById<TextView>(R.id.activity_schedule_tvfootnote).also {
                    it.setText(Html.fromHtml(schedulefootnote, Html.FROM_HTML_MODE_LEGACY))
                }
            }
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

            MobileAds.initialize(this@Activity_Schedule){}
            val Activity_Schedule_BannerAdRequest = AdRequest.Builder().build()

            Activity_Schedule_BannerAd = findViewById(R.id.activity_schedule_adView)

            withContext(Dispatchers.Main){
                Activity_Schedule_BannerAd.loadAd(Activity_Schedule_BannerAdRequest)
            }
        }

    }

    override fun onDestroy() {
        Activity_Schedule_BannerAd.removeAllViews()
        Activity_Schedule_BannerAd.destroy()
        super.onDestroy()
    }
}