package com.example.constitutionofindia.schedules

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.TypedValue
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

class Activity_Schedule : AppCompatActivity(), View.OnClickListener {
    private lateinit var Activity_Schedule_BannerAd : AdView

    private val THEME_PREF = "theme_pref"
    private val THEME_SELECTED = "theme_selected"
    private val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    private lateinit var CoI_SharedPref : SharedPreferences

    private lateinit var scheduleNumKey : String
    private lateinit var schedulenum : String
    private lateinit var schedulename : String

    private lateinit var tvSchedule: TextView
    private lateinit var tvArticlesRelated: TextView
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)

        setContentView(R.layout.activity_schedule)



        intent.extras.also {
            scheduleNumKey = it?.getString("scheduleName").toString()
        }

        factory = BookmarkViewModelFactory(CoIApplication.repository)
        viewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]



        val jscheduleobj = CoIApplication.assetManager.scheduleJSON
        schedulenum = jscheduleobj.getJSONObject(scheduleNumKey).getString("num")
        schedulename = jscheduleobj.getJSONObject(scheduleNumKey).getString("name")
        val scheduletext = jscheduleobj.getJSONObject(scheduleNumKey).getString("text")
        val scheduleArt = jscheduleobj.getJSONObject(scheduleNumKey).getString("articlesRelated")
        val schedulefootnote = jscheduleobj.getJSONObject(scheduleNumKey).getString("footnote")


        tvSchedule = findViewById(R.id.activity_schedule_cvtvHeadline)
        tvArticlesRelated = findViewById(R.id.activity_schedule_cvtvArticlesRelated)
        tvArticlesNum = findViewById(R.id.activity_schedule_cvtvArticlesNum)
        tvBookmarkBtn = findViewById(R.id.activity_schedule_tvBookmarkBtn)

        ivShowElements = findViewById(R.id.activity_schedule_ivShowElements)

        val colorTypedValue = TypedValue()
        theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface,colorTypedValue,true)
        colorOnSurface = colorTypedValue.data

        theme.resolveAttribute(com.google.android.material.R.attr.colorOnTertiary,colorTypedValue,true)
        colorOnTertiary = colorTypedValue.data

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


        ivShowElements.setOnClickListener(this)

    }


    override fun onStart() {
        super.onStart()


        bookmarkManager = BookmarkManager()
        tvBookmarkBtn.setOnClickListener(this)


        CoroutineScope(Dispatchers.IO).launch {

            stored_bookmark = viewModel.getBookmark(schedulenum)

            if(stored_bookmark.size > 0){
                bookmarkState = true
            }

            dataList = mutableListOf(schedulenum, schedulename, scheduleNumKey)
            bookmark = Element_Bookmark(Element_Bookmark.TYPE_SCHEDULE, schedulenum, dataList)

            withContext(Dispatchers.Main) {
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

        lifecycleScope.launch(Dispatchers.IO){
            Activity_Schedule_BannerAd = findViewById(R.id.activity_schedule_adView)

            withContext(Dispatchers.Main){
                AdManager().loadBannerAd(Activity_Schedule_BannerAd)
            }
        }

    }

    override fun onDestroy() {
        Activity_Schedule_BannerAd.removeAllViews()
        Activity_Schedule_BannerAd.destroy()

        super.onDestroy()
    }

    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.activity_schedule_tvBookmarkBtn -> {
                bookmarkState = !bookmarkState
                bookmarkManager.also { it ->
                    it.bookmarkBtnClick(bookmarkState, tvBookmarkBtn, colorOnSurface as Int, colorOnTertiary as Int)
                    it.showMessage(bookmarkState, this.findViewById(R.id.activity_schedule_layout),R.id.activity_schedule_AdCardView)
                }

                if(bookmarkState) {
                    viewModel.insertBookmark(bookmark)
                } else {
                    viewModel.deleteBookmark(bookmark.name)
                }
            }

            R.id.activity_schedule_ivShowElements -> {
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

        tvSchedule.also { tv ->
            tv.maxLines = 3
            tv.ellipsize = TextUtils.TruncateAt.END
        }
        tvArticlesNum.also { tv ->
            tv.visibility = View.GONE
        }
        tvArticlesRelated.also { tv ->
            tv.visibility = View.GONE
        }
        tvBookmarkBtn.visibility = View.GONE
    }

    private fun showElements() {
        ivShowElements.setImageResource(R.drawable.arrow_circle_up)

        tvSchedule.also { tv ->
            tv.maxLines = Int.MAX_VALUE
        }
        tvArticlesNum.also { tv ->
            tv.visibility = View.VISIBLE
        }
        tvArticlesRelated.also { tv ->
            tv.visibility = View.VISIBLE
        }
        tvBookmarkBtn.visibility = View.VISIBLE
    }

}