package com.example.constitutionofindia.articles

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

class Activity_Article : AppCompatActivity() {
    lateinit var Activity_Article_BannerAd: AdView

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"

    lateinit var CoI_SharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode =
            CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_article)

        val partNum: String?
        val partName: String?
        val chapterNum: String?
        val chapterName: String?
        val sectionName: String?
        val articlesNum: String?
        val articlesName: String?
        val articlesText: String?
        val articlesFootnote: String?

        intent.extras.also {
            partNum = it?.getString("partNum")
            partName = it?.getString("partName")
            chapterNum = it?.getString("chapterNum")
            chapterName = it?.getString("chapterName")
            sectionName = it?.getString("sectionName")
            articlesNum = it?.getString("articlesNum")
            articlesName = it?.getString("articlesName")
            articlesText = it?.getString("articlesText")
            articlesFootnote = it?.getString("articlesFootnote")
        }

        findViewById<TextView>(R.id.activity_article_tvHeadline).also {
            it.setText(
                Html.fromHtml(
                    articlesNum + "<br></br>" + articlesName,
                    Html.FROM_HTML_MODE_LEGACY
                )
            )
        }

        findViewById<TextView>(R.id.activity_article_tvPartNum).also {
//            if(partNum.equals("null")){
//                it.visibility = View.GONE
//            }else{
//                it.setText(partNum)
//            }
            it.setText(Html.fromHtml(partNum, Html.FROM_HTML_MODE_LEGACY))
//            Log.d("keynames", "partNum is - " + partNum)
        }

        findViewById<TextView>(R.id.activity_article_tvPartName).also {
//            if(partName.equals("null")){
//                it.visibility = View.GONE
//            }else{
//                it.setText(partName)
//            }
            it.setText(Html.fromHtml(partName, Html.FROM_HTML_MODE_LEGACY))
        }

        findViewById<TextView>(R.id.activity_article_tvChapterNum).also {
            if (chapterNum.equals("null")) {
                it.visibility = View.GONE

            } else {
                it.setText(Html.fromHtml(chapterNum, Html.FROM_HTML_MODE_LEGACY))
            }
        }

        findViewById<TextView>(R.id.activity_article_tvChapterName).also {
            if (chapterName.equals("null")) {
                it.visibility = View.GONE
            } else {
                it.setText(Html.fromHtml(chapterName, Html.FROM_HTML_MODE_LEGACY))
            }
        }

        findViewById<TextView>(R.id.activity_article_tvSubSection).also {
            if (sectionName.equals("null")) {
                it.visibility = View.GONE
            } else {
                it.setText(Html.fromHtml(sectionName, Html.FROM_HTML_MODE_LEGACY))
            }
        }

        findViewById<TextView>(R.id.activity_article_tvtext).also {
            it.setText(Html.fromHtml(articlesText, Html.FROM_HTML_MODE_LEGACY))
        }

        findViewById<TextView>(R.id.activity_article_tvfootnote).also {

            if (articlesFootnote.equals("null")) {
                it.visibility = View.GONE
            } else {
                it.setText(Html.fromHtml(articlesFootnote, Html.FROM_HTML_MODE_LEGACY))
            }

        }


    }


    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@Activity_Article) {}
//            val Activity_Article_BannerAdRequest = AdRequest.Builder().build()

            Activity_Article_BannerAd = findViewById(R.id.activity_article_adView)
            withContext(Dispatchers.Main) {
                AdManager().loadBannerAd(Activity_Article_BannerAd)
//                Activity_Article_BannerAd.loadAd(Activity_Article_BannerAdRequest)
            }
        }

    }

    override fun onDestroy() {
        Activity_Article_BannerAd.removeAllViews()
        Activity_Article_BannerAd.destroy()
        super.onDestroy()
    }
}



















