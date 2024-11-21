package com.example.constitutionofindia.articles

import android.annotation.SuppressLint
import android.content.Context
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Activity_Article : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {
    private lateinit var Activity_Article_BannerAd: AdView

    private val THEME_PREF = "theme_pref"
    private val THEME_SELECTED = "theme_selected"
    private val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    private lateinit var CoI_SharedPref: SharedPreferences

    private lateinit var partNumKey: String
    private var partNum: String? = null
    private var partName: String? = null

    private lateinit var chapterNumKey: String
    private var chapterNum: String? = null
    private var chapterName: String? = null

    private lateinit var sectionsNameKey: String
    private var sectionName: String? = null

    private lateinit var articlesIndex : String
    private var articlesNum: String? = null
    private var articlesName: String? = null
    private var articlesText: String? = null
    private var articlesFootnote: String? = null

    private lateinit var tvArticle: TextView
    private lateinit var tvPartNum: TextView
    private lateinit var tvPartName: TextView
    private lateinit var tvChapterNum: TextView
    private lateinit var tvChapterName: TextView
    private lateinit var tvSubSection: TextView
    private lateinit var tvBookmarkBtn: TextView

    private lateinit var ivShowElements : ImageView

    private lateinit var colorOnSurface: Any
    private lateinit var colorOnTertiary: Any

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var factory: BookmarkViewModelFactory

    private lateinit var bookmark: Element_Bookmark
    private lateinit var stored_bookmark: List<Element_Bookmark>
    private lateinit var dataList: MutableList<String>

    private lateinit var btnbookmark: FloatingActionButton
    private var bookmarkState: Boolean = false
    private lateinit var bookmarkManager: BookmarkManager

    private var showElementState : Boolean = true

    @SuppressLint("ClickableViewAccessibility")
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

        intent.extras.also {
            partNumKey = it?.getString("partNumKey").toString()
            partNum = it?.getString("partNum")
            partName = it?.getString("partName")

            chapterNumKey = it?.getString("chapterNumKey").toString()
            chapterNum = it?.getString("chapterNum")
            chapterName = it?.getString("chapterName")

            sectionsNameKey = it?.getString("sectionNameKey").toString()
            sectionName = it?.getString("sectionName")

            articlesIndex = it?.getString("articlesIndex").toString()
            articlesNum = it?.getString("articlesNum")
            articlesName = it?.getString("articlesName")
            articlesText = it?.getString("articlesText")
            articlesFootnote = it?.getString("articlesFootnote")
        }

        factory = BookmarkViewModelFactory(CoIApplication.repository)
        viewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]



        tvArticle = findViewById(R.id.activity_article_tvHeadline)
        tvPartNum = findViewById(R.id.activity_article_tvPartNum)
        tvPartName = findViewById(R.id.activity_article_tvPartName)
        tvChapterNum = findViewById(R.id.activity_article_tvChapterNum)
        tvChapterName = findViewById(R.id.activity_article_tvChapterName)
        tvSubSection = findViewById(R.id.activity_article_tvSubSection)
        tvBookmarkBtn = findViewById(R.id.activity_article_tvBookmarkBtn)

        ivShowElements = findViewById(R.id.activity_article_ivShowElements)

        val colorTypedValue = TypedValue()
        theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface,colorTypedValue,true)
        colorOnSurface = colorTypedValue.data

        theme.resolveAttribute(com.google.android.material.R.attr.colorOnTertiary,colorTypedValue,true)
        colorOnTertiary = colorTypedValue.data


        tvArticle.also {
            it.setText(
                Html.fromHtml(
                    "$articlesNum<br></br>$articlesName",
                    Html.FROM_HTML_MODE_LEGACY
                )
            )
        }

        tvPartNum.also {
            it.setText(
                Html.fromHtml(
                    partNum, Html.FROM_HTML_MODE_LEGACY
                )
            )
        }

        tvPartName.also {
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

        ivShowElements.setOnClickListener(this)

        bookmarkManager = BookmarkManager()
        btnbookmark = findViewById(R.id.activity_article_fabBookmark)
        btnbookmark.setOnClickListener(this@Activity_Article)
        tvBookmarkBtn.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()



        CoroutineScope(Dispatchers.IO).launch {

            stored_bookmark = viewModel.getBookmark(articlesNum.toString())

            if(stored_bookmark.size > 0){
               bookmarkState = true
            }

            dataList = mutableListOf(articlesNum.toString(),articlesName.toString())
            dataList.also { list ->
                list.add(partNumKey)
                list.add(chapterNumKey)
                list.add(sectionsNameKey)
                list.add(articlesIndex)
            }

            bookmark = Element_Bookmark(Element_Bookmark.TYPE_ARTICLE,articlesNum.toString(),dataList)

            withContext(Dispatchers.Main) {
                bookmarkManager.bookmarkBtnClick(bookmarkState, btnbookmark)
                bookmarkManager.bookmarkBtnClick(bookmarkState, tvBookmarkBtn, colorOnSurface as Int, colorOnTertiary as Int)
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

        lifecycleScope.launch(Dispatchers.IO) {

            Activity_Article_BannerAd = findViewById(R.id.activity_article_adView)
            withContext(Dispatchers.Main) {
                AdManager().loadBannerAd(Activity_Article_BannerAd)
            }
        }

    }


    override fun onDestroy() {
        Activity_Article_BannerAd.removeAllViews()
        Activity_Article_BannerAd.destroy()


        super.onDestroy()

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.activity_article_fabBookmark -> {
                bookmarkState = !bookmarkState
                bookmarkManager.also {
                    it.bookmarkBtnClick(bookmarkState, btnbookmark)
                    it.bookmarkBtnClick(bookmarkState, tvBookmarkBtn, colorOnSurface as Int, colorOnTertiary as Int)
                    it.showMessage(bookmarkState, this.findViewById(R.id.activity_article_layout),R.id.activity_article_AdCardView)
                }

                if(bookmarkState) {
                    viewModel.insertBookmark(bookmark)
                } else {
                    viewModel.deleteBookmark(bookmark.name)
                }

            }

            R.id.activity_article_tvBookmarkBtn -> {
                bookmarkState = !bookmarkState
                bookmarkManager.also {
                    it.bookmarkBtnClick(bookmarkState, btnbookmark)
                    it.bookmarkBtnClick(bookmarkState, tvBookmarkBtn, colorOnSurface as Int, colorOnTertiary as Int)
                    it.showMessage(bookmarkState, this.findViewById(R.id.activity_article_layout),R.id.activity_article_AdCardView)
                }

                if(bookmarkState) {
                    viewModel.insertBookmark(bookmark)
                } else {
                    viewModel.deleteBookmark(bookmark.name)
                }
            }

            R.id.activity_article_ivShowElements -> {
                showElementState = !showElementState

                if(showElementState) {
                    showElements()
                } else {
                    hideElements()
                }
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        v?.performClick()
        return true
    }

    private fun hideElements() {
        ivShowElements.setImageResource(R.drawable.arrow_circle_down)

        tvArticle.also { tv ->
            tv.maxLines = 3
            tv.ellipsize = TextUtils.TruncateAt.END
        }
        tvSubSection.also { tv ->
            if (!sectionName.equals("null")) {
                tv.visibility = View.GONE
            }
        }
        tvChapterNum.also { tv ->
            if (!chapterNum.equals("null")) {
                tv.visibility = View.GONE
            }
        }
        tvChapterName.also { tv ->
            if (!chapterName.equals("null")) {
                tv.visibility = View.GONE
            }
        }
        tvPartNum.also { tv ->
            if (!partNum.equals("null")) {
                tv.visibility = View.GONE
            }
        }
        tvPartName.also { tv ->
            if (!partName.equals("null")) {
                tv.visibility = View.GONE
            }
        }
        tvBookmarkBtn.visibility = View.GONE
    }

    private fun showElements() {
        ivShowElements.setImageResource(R.drawable.arrow_circle_up)

        tvArticle.also { tv ->
            tv.maxLines = Int.MAX_VALUE
        }
        tvSubSection.also { tv ->
            if (!sectionName.equals("null")) {
                tv.visibility = View.VISIBLE
            }
        }
        tvChapterNum.also { tv ->
            if (!chapterNum.equals("null")) {
                tv.visibility = View.VISIBLE
            }
        }
        tvChapterName.also { tv ->
            if (!chapterName.equals("null")) {
                tv.visibility = View.VISIBLE
            }
        }
        tvPartNum.also { tv ->
            if (!partNum.equals("null")) {
                tv.visibility = View.VISIBLE
            }
        }
        tvPartName.also { tv ->
            if (!partName.equals("null")) {
                tv.visibility = View.VISIBLE
            }
        }
        tvBookmarkBtn.visibility = View.VISIBLE
    }

}



















