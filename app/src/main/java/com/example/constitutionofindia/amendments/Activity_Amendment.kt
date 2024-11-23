package com.example.constitutionofindia.amendments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private lateinit var tvBookmarkBtn: TextView

    private lateinit var ivShowElements : ImageView

    private lateinit var colorOnSurface: Any
    private lateinit var colorOnTertiary: Any

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var factory: BookmarkViewModelFactory

    private lateinit var bookmark: Element_Bookmark
    private lateinit var stored_bookmark: List<Element_Bookmark>
    private lateinit var dataList: MutableList<String>

    private var bookmarkState : Boolean = false
    private lateinit var bookmarkManager: BookmarkManager

    private var showElementState : Boolean = true

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

        val jamendmentobj = CoIApplication.assetManager.amendmentJSON
        amendmentname = jamendmentobj.getJSONObject(amendmentKey).getString("name")
        amendmentyear = jamendmentobj.getJSONObject(amendmentKey).getString("Year")
        val amendmentText = jamendmentobj.getJSONObject(amendmentKey).getString("text")
        val amendmentfootnote = jamendmentobj.getJSONObject(amendmentKey).getString("footnote")
        val amendmentSOR = jamendmentobj.getJSONObject(amendmentKey).getString("SOR")
        val amendmentArticleAffected =
            jamendmentobj.getJSONObject(amendmentKey).getString("articlesAffected")


        tvAmendment = findViewById(R.id.activity_amendment_cvtvHeadline)
        tvArticlesNum = findViewById(R.id.activity_amendment_cvtvArticlesNum)
        tvBookmarkBtn = findViewById(R.id.activity_amendment_tvBookmarkBtn)

        ivShowElements = findViewById(R.id.activity_amendment_ivShowElements)

        val colorTypedValue = TypedValue()
        theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface,colorTypedValue,true)
        colorOnSurface = colorTypedValue.data

        theme.resolveAttribute(com.google.android.material.R.attr.colorOnTertiary,colorTypedValue,true)
        colorOnTertiary = colorTypedValue.data


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


        ivShowElements.setOnClickListener(this)

        bookmarkManager = BookmarkManager()

        tvBookmarkBtn.setOnClickListener(this)

        CoroutineScope(Dispatchers.IO).launch {

            stored_bookmark = viewModel.getBookmark(amendmentname)

            if(stored_bookmark.size > 0){
                bookmarkState = true
            }

            dataList = mutableListOf(amendmentname, amendmentyear, amendmentKey)
            bookmark = Element_Bookmark(Element_Bookmark.TYPE_AMENDMENT, amendmentname, dataList)

            withContext(Dispatchers.Main) {
                bookmarkManager.bookmarkBtnClick(bookmarkState, tvBookmarkBtn, colorOnSurface as Int, colorOnTertiary as Int)
            }

        }

    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {

            Activity_Amendment_BannerAd = findViewById(R.id.activity_amendment_adView)
            withContext(Dispatchers.Main) {
                AdManager().loadBannerAd(Activity_Amendment_BannerAd)
            }
        }

    }

    override fun onDestroy() {

        Activity_Amendment_BannerAd.removeAllViews()
        Activity_Amendment_BannerAd.destroy()

        findViewById<TextView>(R.id.activity_amendment_tvSOR).also {
            it.setOnClickListener(null)
        }

        super.onDestroy()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        v?.performClick()
        return true
    }

    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.activity_amendment_tvBookmarkBtn -> {
                bookmarkState = !bookmarkState
                bookmarkManager.also {
                    it.bookmarkBtnClick(bookmarkState, tvBookmarkBtn, colorOnSurface as Int, colorOnTertiary as Int)
                    it.showMessage(bookmarkState, this.findViewById(R.id.activity_amendment_layout),R.id.activity_amendment_AdCardView)
                }

                if(bookmarkState) {
                    viewModel.insertBookmark(bookmark)
                } else {
                    viewModel.deleteBookmark(bookmark.name)
                }
            }

            R.id.activity_amendment_ivShowElements -> {
                showElementState = !showElementState

                if(showElementState) {
                    showElements()
                } else {
                    hideElements()
                }
            }
        }
    }


    private fun hideElements() {
        ivShowElements.setImageResource(R.drawable.arrow_circle_down)

        tvAmendment.also { tv ->
            tv.maxLines = 3
            tv.ellipsize = TextUtils.TruncateAt.END
        }
        tvArticlesNum.also {tv ->
            tv.visibility = View.GONE
        }
        tvBookmarkBtn.visibility = View.GONE
    }

    private fun showElements() {
        ivShowElements.setImageResource(R.drawable.arrow_circle_up)

        tvAmendment.also { tv ->
            tv.maxLines = Int.MAX_VALUE
        }
        tvArticlesNum.also {tv ->
            tv.visibility = View.VISIBLE
        }
        tvBookmarkBtn.visibility = View.VISIBLE
    }
}












