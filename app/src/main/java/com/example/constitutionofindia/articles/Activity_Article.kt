package com.example.constitutionofindia.articles

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

    private var sectionName: String? = null
    private var partNum: String? = null
    private var partName: String? = null
    private var chapterNum: String? = null
    private var chapterName: String? = null
    private var articlesNum: String? = null
    private var articlesName: String? = null
    private var articlesText: String? = null
    private var articlesFootnote: String? = null

    private lateinit var tvArticle : TextView
    private lateinit var tvPartNum : TextView
    private lateinit var tvPartName : TextView
    private lateinit var tvChapterNum : TextView
    private lateinit var tvChapterName : TextView
    private lateinit var tvSubSection : TextView

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


        tvArticle = findViewById(R.id.activity_article_tvHeadline)
        tvPartNum = findViewById(R.id.activity_article_tvPartNum)
        tvPartName = findViewById(R.id.activity_article_tvPartName)
        tvChapterNum = findViewById(R.id.activity_article_tvChapterNum)
        tvChapterName = findViewById(R.id.activity_article_tvChapterName)
        tvSubSection = findViewById(R.id.activity_article_tvSubSection)



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

        tvArticle.also {
            it.setText(
                Html.fromHtml(
                    articlesNum + "<br></br>" + articlesName,
                    Html.FROM_HTML_MODE_LEGACY
                )
            )
        }

        tvPartNum.also {
//            if(partNum.equals("null")){
//                it.visibility = View.GONE
//            }else{
//                it.setText(partNum)
//            }
            it.setText(Html.fromHtml(partNum, Html.FROM_HTML_MODE_LEGACY))
//            Log.d("keynames", "partNum is - " + partNum)
        }

        tvPartName.also {
//            if(partName.equals("null")){
//                it.visibility = View.GONE
//            }else{
//                it.setText(partName)
//            }
            it.setText(Html.fromHtml(partName, Html.FROM_HTML_MODE_LEGACY))
        }

        tvChapterNum.also {
            if (chapterNum.equals("null")) {
                it.visibility = View.GONE

            } else {
                it.setText(Html.fromHtml(chapterNum, Html.FROM_HTML_MODE_LEGACY))
            }
        }

        tvChapterName.also {
            if (chapterName.equals("null")) {
                it.visibility = View.GONE
            } else {
                it.setText(Html.fromHtml(chapterName, Html.FROM_HTML_MODE_LEGACY))
            }
        }

        tvSubSection.also {
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




        findViewById<ScrollView>(R.id.activity_article_svText).also {
            it.setOnScrollChangeListener(
                View.OnScrollChangeListener { view, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->

                    if (scrollY >= view.top + 50) {
//                        Toast.makeText(this@Activity_Article, "Yes, Scrolled", Toast.LENGTH_LONG).show()
                        tvSubSection.also {tv ->
                            if(!sectionName.equals("null")){
                                tv.visibility = View.GONE
                            }
                        }
                    }else{
                        tvSubSection.also {tv ->
                            if(!sectionName.equals("null")){
                                tv.visibility = View.VISIBLE
                            }
                        }
                    }

                    if (scrollY >= view.top +50) {
//                        Toast.makeText(this@Activity_Article, "Yes, Scrolled", Toast.LENGTH_LONG).show()
                        tvChapterNum.also {tv ->
                            if(!chapterNum.equals("null")){
                                tv.visibility = View.GONE
                            }
                        }
                        tvChapterName.also {tv ->
                            if(!chapterName.equals("null")){
                                tv.visibility = View.GONE
                            }
                        }
                    }else{
                        tvChapterNum.also {tv ->
                            if(!chapterNum.equals("null")){
                                tv.visibility = View.VISIBLE
                            }
                        }
                        tvChapterName.also {tv ->
                            if(!chapterName.equals("null")){
                                tv.visibility = View.VISIBLE
                            }
                        }

                    }

                    if (scrollY >= view.top +50) {
//                        Toast.makeText(this@Activity_Article, "Yes, Scrolled", Toast.LENGTH_LONG).show()
                        tvPartNum.also { tv ->
                            if(!partNum.equals("null")){
                                tv.visibility = View.GONE
                            }
                        }
                        tvPartName.also {tv ->
                            if(!partName.equals("null")){
                                tv.visibility = View.GONE
                            }
                        }
                    }else{
                        tvPartNum.also { tv ->
                            if(!partNum.equals("null")){
                                tv.visibility = View.VISIBLE
                            }
                        }
                        tvPartName.also {tv ->
                            if(!partName.equals("null")){
                                tv.visibility = View.VISIBLE
                            }
                        }

                    }

                    return@OnScrollChangeListener
                }
            )

//            it.setOnTouchListener(
//                View.OnTouchListener { view, event ->
//                    when (event.action) {
////                        MotionEvent.AXIS_HSCROLL -> {
////                            findViewById<TextView>(R.id.activity_article_tvSubSection).also {
////                                if (!sectionName.equals("null")) {
////                                    it.visibility = View.GONE
////                                }
////                            }
////                        }
////                        MotionEvent.ACTION_SCROLL -> {
////                            findViewById<TextView>(R.id.activity_article_tvSubSection).also {
////                                if (!sectionName.equals("null")) {
////                                    it.visibility = View.GONE
////                                }
////                            }
////                        }
////                        MotionEvent.ACTION_UP -> {
////                            // TODO: Handle ACTION_UP
////                            findViewById<TextView>(R.id.activity_article_tvSubSection).also {
////                                if (!sectionName.equals("null")) {
//////                                    if(it.visibility == View.GONE){
//////                                        it.visibility = View.VISIBLE
//////                                    }else{
//////                                        it.visibility = View.GONE
//////                                    }
////                                    it.visibility = View.GONE
////                                }
////                            }
////                        }
////                        MotionEvent.ACTION_DOWN -> {
////                            // TODO: Handle ACTION_DOWN
////                            findViewById<TextView>(R.id.activity_article_tvSubSection).also {
////                                if (!sectionName.equals("null")) {
////                                    it.visibility = View.GONE
////                                }
////                            }
////                        }
//                    }
//
//                    // required to by-pass lint warning
//                    view.performClick()
//                    return@OnTouchListener true
//                }
//            )
        }


        tvArticle.also {

            it.setOnTouchListener(
                View.OnTouchListener { v, event ->
                    tvPartNum.also { tv ->
                        if(!partNum.equals("null")){
                            tv.visibility = View.VISIBLE
                        }
                    }
                    tvPartName.also {tv ->
                        if(!partName.equals("null")){
                            tv.visibility = View.VISIBLE
                        }
                    }
                    tvChapterNum.also {tv ->
                        if(!chapterNum.equals("null")){
                            tv.visibility = View.VISIBLE
                        }
                    }
                    tvChapterName.also {tv ->
                        if(!chapterName.equals("null")){
                            tv.visibility = View.VISIBLE
                        }
                    }
                    tvSubSection.also {tv ->
                        if(!sectionName.equals("null")){
                            tv.visibility = View.VISIBLE
                        }
                    }

                    v.performClick()
                    return@OnTouchListener true
                }
            )
        }

    }

    override fun onStart() {
        super.onStart()

//        lifecycleScope.launch(Dispatchers.IO) {
//            MobileAds.initialize(this@Activity_Article) {}
//            val Activity_Article_BannerAdRequest = AdRequest.Builder().build()
//
//            Activity_Article_BannerAd = findViewById(R.id.activity_article_adView)
//            withContext(Dispatchers.Main) {
//                Activity_Article_BannerAd.loadAd(Activity_Article_BannerAdRequest)
//            }
//        }

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

//        Toast.makeText(this@Activity_Article, "OnResume working!", Toast.LENGTH_LONG).show()

    }

    override fun onDestroy() {
        Activity_Article_BannerAd.removeAllViews()
        Activity_Article_BannerAd.destroy()
        super.onDestroy()
    }
}



















