package com.example.constitutionofindia.schedules

import android.annotation.SuppressLint
import android.content.Context
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

class Activity_Schedule : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {
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

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var factory: BookmarkViewModelFactory

    private lateinit var bookmark: Element_Bookmark
    private lateinit var stored_bookmark: List<Element_Bookmark>
    private lateinit var dataList: MutableList<String>

    private lateinit var btnbookmark : FloatingActionButton
    private var bookmarkState : Boolean = false
    private lateinit var bookmarkManager: BookmarkManager

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

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

                    if (scrollY >= view.top + 30) {
//                        Toast.makeText(this@Activity_Article, "Yes, Scrolled", Toast.LENGTH_LONG).show()
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
                    } else {
                        tvSchedule.also { tv ->
                            tv.maxLines = Int.MAX_VALUE
                        }
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


        tvSchedule.setOnTouchListener(this@Activity_Schedule)


    }


    override fun onStart() {
        super.onStart()


        bookmarkManager = BookmarkManager()
        btnbookmark = findViewById(R.id.activity_schedule_fabBookmark)
        btnbookmark.setOnClickListener(this@Activity_Schedule)


        CoroutineScope(Dispatchers.IO).launch {

            stored_bookmark = viewModel.getBookmark(schedulenum)

            if(stored_bookmark.size > 0){
                bookmarkState = true
            }

            dataList = mutableListOf(schedulenum, schedulename, scheduleNumKey)
            bookmark = Element_Bookmark(Element_Bookmark.TYPE_SCHEDULE, schedulenum, dataList)

            withContext(Dispatchers.Main) {
                bookmarkManager.bookmarkBtnClick(bookmarkState, btnbookmark)

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

//        if(bookmarkState && stored_bookmark.size == 0) {
//            viewModel.insertBookmark(bookmark)
//        } else if (!bookmarkState && stored_bookmark.size > 0) {
//            viewModel.deleteBookmark(stored_bookmark[0])
//        }


        super.onDestroy()
    }

    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.activity_schedule_fabBookmark -> {
                bookmarkState = !bookmarkState
                bookmarkManager.also { it ->
                    it.bookmarkBtnClick(bookmarkState, btnbookmark)
                    it.showMessage(bookmarkState, this.findViewById(R.id.activity_schedule_layout),R.id.activity_schedule_AdCardView)
                }

                if(bookmarkState) {
                    viewModel.insertBookmark(bookmark)
                } else {
                    viewModel.deleteBookmark(stored_bookmark[0])
                }

            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        when(v?.id) {
            R.id.activity_schedule_cvtvHeadline -> {
                tvSchedule.maxLines = Int.MAX_VALUE

                tvArticlesNum.also { tv ->
                    tv.visibility = View.VISIBLE
                }
                tvArticlesRelated.also { tv ->
                    tv.visibility = View.VISIBLE
                }

            }
        }

        v?.performClick()
        return true
    }

}