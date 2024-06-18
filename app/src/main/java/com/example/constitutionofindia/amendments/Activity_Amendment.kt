package com.example.constitutionofindia.amendments

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.AdManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Activity_Amendment : AppCompatActivity() {
    lateinit var Activity_Amendment_BannerAd: AdView

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"

    lateinit var CoI_SharedPref: SharedPreferences

    lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode =
            CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_amendment)

        intent.extras.also {
            name = it?.getString("amendmentName").toString()
        }



    }

    override fun onStart() {

        lifecycleScope.launch(Dispatchers.Default){
            val jamendmentfile: String =
                applicationContext.assets.open("amendments.json").bufferedReader().use {
                    it.readText()
                }

            val jamendmentobj = JSONObject(jamendmentfile)
            val amendmentname = jamendmentobj.getJSONObject(name).getString("name")
            val amendmentyear = jamendmentobj.getJSONObject(name).getString("Year")
            val amendmentText = jamendmentobj.getJSONObject(name).getString("text")
            val amendmentfootnote = jamendmentobj.getJSONObject(name).getString("footnote")
            val amendmentSOR = jamendmentobj.getJSONObject(name).getString("SOR")
            val amendmentArticleAffected =
                jamendmentobj.getJSONObject(name).getString("articlesAffected")
//        val sortext = jamendmentobj.getJSONObject(name).getString("SOR")

            withContext(Dispatchers.Main){
                findViewById<TextView>(R.id.activity_amendment_cvtvHeadline).also {
                    it.setText(
                        Html.fromHtml(
                            amendmentname + ", " + amendmentyear,
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    )
                }

                findViewById<TextView>(R.id.activity_amendment_tvtext).also {
                    it.setText(Html.fromHtml(amendmentText, Html.FROM_HTML_MODE_LEGACY))
                }

                findViewById<TextView>(R.id.activity_amendment_tvfootnote).also {

                    if (amendmentfootnote.equals("null")) {
                        it.visibility = View.GONE
                    } else {
                        it.setText(Html.fromHtml(amendmentfootnote, Html.FROM_HTML_MODE_LEGACY))
                    }
                }
                findViewById<TextView>(R.id.activity_amendment_cvtvArticlesNum).also {
                    it.setText(amendmentArticleAffected)
                }

                findViewById<TextView>(R.id.activity_amendment_tvSOR).also {
                    if (amendmentSOR.equals("null")) {
                        it.visibility = View.GONE
                    }
                    it.setOnClickListener {
                        Intent(this@Activity_Amendment, Activity_Amendment_SOR::class.java).also {intent ->
                            intent.putExtra("SORtext", amendmentSOR)
                            startActivity(intent)
                        }
                    }
                }
            }

        }
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@Activity_Amendment) {}
//            val Activity_Amendment_BannerAdRequest = AdRequest.Builder().build()

            Activity_Amendment_BannerAd = findViewById(R.id.activity_amendment_adView)
            withContext(Dispatchers.Main) {
                AdManager().loadBannerAd(Activity_Amendment_BannerAd)
//                Activity_Amendment_BannerAd.loadAd(Activity_Amendment_BannerAdRequest)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        Activity_Amendment_BannerAd.removeAllViews()
        Activity_Amendment_BannerAd.destroy()

        findViewById<TextView>(R.id.activity_amendment_tvSOR).also {
            it.setOnClickListener(null)
        }
    }
}












