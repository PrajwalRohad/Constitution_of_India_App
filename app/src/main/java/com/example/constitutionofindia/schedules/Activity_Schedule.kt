package com.example.constitutionofindia.schedules

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.IndiaCanon.constitutionofindia.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Activity_Schedule : AppCompatActivity() {
    lateinit var Activity_Schedule_BannerAd: AdView

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"

    lateinit var CoI_SharedPref: SharedPreferences

    private lateinit var tvSchedule: TextView
    private lateinit var tvArticlesRelated: TextView
    private lateinit var tvArticlesNum: TextView

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
        val nightmode =
            CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_schedule)


        val name: String?

        intent.extras.also {
            name = it?.getString("scheduleName")
        }


        val jschedulefile: String =
            applicationContext.assets.open("schedules.json").bufferedReader().use {
                it.readText()
            }

        val jscheduleobj = JSONObject(jschedulefile)
        val schedulenum = jscheduleobj.getJSONObject(name).getString("num")
        val schedulename = jscheduleobj.getJSONObject(name).getString("name")
        val scheduletext = jscheduleobj.getJSONObject(name).getString("text")
        val scheduleArt = jscheduleobj.getJSONObject(name).getString("articlesRelated")
        val schedulefootnote = jscheduleobj.getJSONObject(name).getString("footnote")


        tvSchedule = findViewById(R.id.activity_schedule_cvtvHeadline)
        tvArticlesRelated = findViewById(R.id.activity_schedule_cvtvArticlesRelated)
        tvArticlesNum = findViewById(R.id.activity_schedule_cvtvArticlesNum)



        tvSchedule.also {
            it.setText(
                Html.fromHtml(
                    schedulenum + "<br></br>" + schedulename,
                    Html.FROM_HTML_MODE_LEGACY
                )
            )
        }
        tvArticlesNum.also {
            it.setText(Html.fromHtml(scheduleArt, Html.FROM_HTML_MODE_LEGACY))
        }
        findViewById<TextView>(R.id.activity_schedule_tvtext).also {
            it.setText(Html.fromHtml(scheduletext, Html.FROM_HTML_MODE_LEGACY))
        }
        findViewById<TextView>(R.id.activity_schedule_tvfootnote).also {
            it.setText(Html.fromHtml(schedulefootnote, Html.FROM_HTML_MODE_LEGACY))
        }




        findViewById<ScrollView>(R.id.activity_schedule_svText).also {
            it.setOnScrollChangeListener(
                View.OnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->

                    if (scrollY >= view.top + 50) {
//                        Toast.makeText(this@Activity_Article, "Yes, Scrolled", Toast.LENGTH_LONG).show()
                        tvArticlesNum.also { tv ->

                            tv.visibility = View.GONE

                        }
                        tvArticlesRelated.also { tv ->
                            tv.visibility = View.GONE
                        }
                    } else {
                        tvArticlesNum.also { tv ->
                            tv.visibility = View.VISIBLE
                        }
                        tvArticlesRelated.also { tv ->
                            tv.visibility = View.VISIBLE
                        }

                    }

                    return@OnScrollChangeListener
                }
            )
        }


        tvSchedule.also {

            it.setOnTouchListener(
                View.OnTouchListener { v, event ->
                    tvArticlesNum.also { tv ->
                        tv.visibility = View.VISIBLE
                    }
                    tvArticlesRelated.also { tv ->
                        tv.visibility = View.VISIBLE
                    }

                    v.performClick()
                    return@OnTouchListener true
                }
            )
        }


    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {

            MobileAds.initialize(this@Activity_Schedule) {}
            val Activity_Schedule_BannerAdRequest = AdRequest.Builder().build()

            Activity_Schedule_BannerAd = findViewById(R.id.activity_schedule_adView)

            withContext(Dispatchers.Main) {
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