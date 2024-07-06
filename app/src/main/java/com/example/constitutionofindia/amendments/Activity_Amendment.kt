package com.example.constitutionofindia.amendments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.AdManager
import com.example.constitutionofindia.BookmarkManager
import com.example.constitutionofindia.CoIApplication
import com.example.constitutionofindia.ThemePreference
import com.example.constitutionofindia.bookmarks.bookmarksViewModel.BookmarkViewModel
import com.example.constitutionofindia.bookmarks.bookmarksViewModel.BookmarkViewModelFactory
import com.example.constitutionofindia.data.entity.Element_Bookmark
import com.google.android.gms.ads.AdView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Activity_Amendment : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {
    private lateinit var Activity_Amendment_BannerAd: AdView

    private val THEME_PREF = "theme_pref"
    private val THEME_SELECTED = "theme_selected"
    private val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    private lateinit var CoI_SharedPref: SharedPreferences


    private lateinit var amendmentKey: String
    private lateinit var amendmentname : String
    private lateinit var amendmentyear : String

    private lateinit var tvAmendment: TextView
    private lateinit var tvArticlesNum: TextView

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var factory: BookmarkViewModelFactory

    private lateinit var bookmark: Element_Bookmark
    private lateinit var stored_bookmark: List<Element_Bookmark>
    private lateinit var dataList: MutableList<String>

    private lateinit var btnbookmark : FloatingActionButton
    private var bookmarkState : Boolean = false
    private lateinit var bookmarkManager: BookmarkManager

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
            amendmentKey = it?.getString("amendmentName").toString()
        }

        factory = BookmarkViewModelFactory(CoIApplication.repository)
        viewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]



    }

    override fun attachBaseContext(newBase: Context) {
        val sharedpref = newBase.getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        var fontsize1 = 1.0f
        if (sharedpref != null) {
            fontsize1 = 0.5f + (0.25f * sharedpref.getInt(FONT_SIZE, 1))
        }

        super.attachBaseContext(ThemePreference().adjustFontScale(newBase, fontsize1))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()

//        val jamendmentfile: String =
//            applicationContext.assets.open("amendments.json").bufferedReader().use {
//                it.readText()
//            }

        val jamendmentobj = CoIApplication.assetManager.amendmentJSON
        amendmentname = jamendmentobj.getJSONObject(amendmentKey).getString("name")
        amendmentyear = jamendmentobj.getJSONObject(amendmentKey).getString("Year")
        val amendmentText = jamendmentobj.getJSONObject(amendmentKey).getString("text")
        val amendmentfootnote = jamendmentobj.getJSONObject(amendmentKey).getString("footnote")
        val amendmentSOR = jamendmentobj.getJSONObject(amendmentKey).getString("SOR")
        val amendmentArticleAffected =
            jamendmentobj.getJSONObject(amendmentKey).getString("articlesAffected")
//        val sortext = jamendmentobj.getJSONObject(name).getString("SOR")


        tvAmendment = findViewById(R.id.activity_amendment_cvtvHeadline)
        tvArticlesNum = findViewById(R.id.activity_amendment_cvtvArticlesNum)


        tvAmendment.also {
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
        tvArticlesNum.also {
            it.setText(amendmentArticleAffected)
        }

        findViewById<TextView>(R.id.activity_amendment_tvSOR).also {
            if (amendmentSOR.equals("null")) {
                it.visibility = View.GONE
            }
            it.setOnClickListener {
                Intent(this@Activity_Amendment, Activity_Amendment_SOR::class.java).also { intent ->
                    intent.putExtra("SORtext", amendmentSOR)
                    startActivity(intent)
                }
            }
        }


        findViewById<ScrollView>(R.id.activity_amendment_svText).also {
            it.setOnScrollChangeListener(
                View.OnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
                    if (scrollY >= view.top + 30) {
//                        Toast.makeText(this@Activity_Article, "Yes, Scrolled", Toast.LENGTH_LONG).show()
                        tvAmendment.also { tv ->
                            tv.maxLines = 3
                            tv.ellipsize = TextUtils.TruncateAt.END
                        }
                        tvArticlesNum.also {tv ->
                            tv.visibility = View.GONE
                        }
                    }else{
                        tvAmendment.also { tv ->
                            tv.maxLines = Int.MAX_VALUE
                        }
                        tvArticlesNum.also {tv ->
                            tv.visibility = View.VISIBLE
                        }
                    }

                    return@OnScrollChangeListener
                }
            )
        }



        tvAmendment.setOnTouchListener(this@Activity_Amendment)

        bookmarkManager = BookmarkManager()
        btnbookmark = findViewById(R.id.activity_amendment_fabBookmark)

        btnbookmark.setOnClickListener(this@Activity_Amendment)

        CoroutineScope(Dispatchers.IO).launch {

            stored_bookmark = viewModel.getBookmark(amendmentname)

            if(stored_bookmark.size > 0){
                bookmarkState = true
            }

            dataList = mutableListOf(amendmentname, amendmentyear, amendmentKey)
            bookmark = Element_Bookmark(Element_Bookmark.TYPE_AMENDMENT, amendmentname, dataList)

            withContext(Dispatchers.Main) {
                bookmarkManager.bookmarkBtnClick(bookmarkState, btnbookmark)

            }

        }

    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {
//            MobileAds.initialize(this@Activity_Amendment) {}
//            val Activity_Amendment_BannerAdRequest = AdRequest.Builder().build()

            Activity_Amendment_BannerAd = findViewById(R.id.activity_amendment_adView)
            withContext(Dispatchers.Main) {
                AdManager().loadBannerAd(Activity_Amendment_BannerAd)
//                Activity_Amendment_BannerAd.loadAd(Activity_Amendment_BannerAdRequest)
            }
        }

    }

    override fun onDestroy() {

        Activity_Amendment_BannerAd.removeAllViews()
        Activity_Amendment_BannerAd.destroy()

        findViewById<TextView>(R.id.activity_amendment_tvSOR).also {
            it.setOnClickListener(null)
        }

//        if(bookmarkState && stored_bookmark.size == 0) {
//            viewModel.insertBookmark(bookmark)
//        } else if (!bookmarkState && stored_bookmark.size > 0) {
//            viewModel.deleteBookmark(stored_bookmark[0])
//        }


        super.onDestroy()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(v) {
            tvAmendment -> {
                tvAmendment.maxLines = Int.MAX_VALUE

                tvArticlesNum.also {tv ->
                    tv.visibility = View.VISIBLE
                }
            }
        }
        v?.performClick()
        return true
    }

    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.activity_amendment_fabBookmark -> {
                bookmarkState = !bookmarkState
                bookmarkManager.also {
                    it.bookmarkBtnClick(bookmarkState, btnbookmark)
                    it.showMessage(bookmarkState, this.findViewById(R.id.activity_amendment_layout),R.id.activity_amendment_AdCardView)
                }

                if(bookmarkState) {
                    viewModel.insertBookmark(bookmark)
                } else {
                    viewModel.deleteBookmark(stored_bookmark[0])
                }
            }
        }
    }
}












