package com.example.constitutionofindia.bookmarks

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.AdManager
import com.example.constitutionofindia.AssetManager
import com.example.constitutionofindia.BookmarkManager
import com.example.constitutionofindia.CoIApplication
import com.example.constitutionofindia.SwipeToDeleteCallback
import com.example.constitutionofindia.ThemePreference
import com.example.constitutionofindia.bookmarks.bookmarksViewModel.BookmarkViewModel
import com.example.constitutionofindia.bookmarks.bookmarksViewModel.BookmarkViewModelFactory
import com.example.constitutionofindia.bookmarks.stateComposeMethod.BookmarkFilterType
import com.example.constitutionofindia.data.entity.Element_Bookmark
import com.google.android.gms.ads.AdView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Activity_Bookmarks : AppCompatActivity(), Adapter_Bookmarks.BookmarkInterface,
    View.OnClickListener {

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    private lateinit var CoI_SharedPref: SharedPreferences

    private lateinit var Activity_Bookmarks_BannerAd: AdView

    private lateinit var filter_all: Button
    private lateinit var filter_articles: Button
    private lateinit var filter_schedules: Button
    private lateinit var filter_amendments: Button
    private lateinit var rvBookmarks: RecyclerView

    private lateinit var bgTypedValue: TypedValue
    private lateinit var thumbTypedValue: TypedValue

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var factory: BookmarkViewModelFactory

//    lateinit var state: BookmarkState
    lateinit var currentFilterType: BookmarkFilterType

    lateinit var rvListAll: List<Element_Bookmark>
    lateinit var rvListArticle: List<Element_Bookmark>
    lateinit var rvListSchedule: List<Element_Bookmark>
    lateinit var rvListAmendment: List<Element_Bookmark>
    lateinit var bookmarksListAdapter: Adapter_Bookmarks

    private lateinit var assetManager: AssetManager
    private lateinit var bookmarkManager: BookmarkManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode =
            CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)

        setContentView(R.layout.activity_bookmarks)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_activity_bookmarks)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        factory = BookmarkViewModelFactory(CoIApplication.repository)
        viewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]


        setSupportActionBar(findViewById(R.id.activity_bookmarks_tb))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        currentFilterType = BookmarkFilterType.TYPE_ALL

        CoroutineScope(Dispatchers.IO).launch {

            viewModel.getAllBookmarks().collect{ value ->
                rvListAll = value
            }

            viewModel.getArticleBookmarks().collect{ value ->
                rvListArticle = value
            }

            viewModel.getScheduleBookmarks().collect{ value ->
                rvListSchedule = value
            }

            viewModel.getAmendmentBookmarks().collect{ value ->
                rvListAmendment = value
            }


            withContext(Dispatchers.Main) {
                setUpRV()
            }
        }

        initiateButtons()
        assetManager = CoIApplication.assetManager
        bookmarkManager = BookmarkManager()

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.RESUMED){
                setRVList(currentFilterType)
                setAllButtons()
                when(currentFilterType) {
                    BookmarkFilterType.TYPE_ALL -> setButton(filter_all)
                    BookmarkFilterType.TYPE_ARTICLE -> setButton(filter_articles)
                    BookmarkFilterType.TYPE_SCHEDULE -> setButton(filter_schedules)
                    BookmarkFilterType.TYPE_AMENDMENT -> setButton(filter_amendments)
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        Activity_Bookmarks_BannerAd = findViewById(R.id.activity_bookmarks_adView)

        AdManager().loadBannerAd(Activity_Bookmarks_BannerAd)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_bookmarks_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.bookmarks_menu_deleteAll -> {
                viewModel.deleteALLBookmarks()
                bookmarksListAdapter.also { rvadap ->
                    rvadap.bookmarkslist = mutableListOf()
                    rvadap.notifyDataSetChanged()
                }
                Snackbar.make(
                    this.findViewById(R.id.layout_activity_bookmarks),
                    "All Bookmarks removed.",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAnchorView(R.id.activity_bookmarks_cvAdCard)
                    setActionTextColor(Color.WHITE)
                    show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun BookmarkOnClick(position: Int, bookmark: Element_Bookmark) {
//        Toast.makeText(this, "${bookmark.type} click", Toast.LENGTH_SHORT).show()
        val intent = bookmarkManager.loadFromBookmark(bookmark, assetManager)

        startActivity(intent)

    }


    override fun onClick(v: View?) {
        setAllButtons()
        when (v?.id) {
            R.id.activity_bookmarks_filter_all -> {
                currentFilterType = BookmarkFilterType.TYPE_ALL
                setRVList(BookmarkFilterType.TYPE_ALL)
                setButton(filter_all)
            }

            R.id.activity_bookmarks_filter_articles -> {
                currentFilterType = BookmarkFilterType.TYPE_ARTICLE
                setRVList(BookmarkFilterType.TYPE_ARTICLE)
                setButton(filter_articles)
            }

            R.id.activity_bookmarks_filter_schedules -> {
                currentFilterType = BookmarkFilterType.TYPE_SCHEDULE
                setRVList(BookmarkFilterType.TYPE_SCHEDULE)
                setButton(filter_schedules)
            }

            R.id.activity_bookmarks_filter_amendments -> {
                currentFilterType = BookmarkFilterType.TYPE_AMENDMENT
                setRVList(BookmarkFilterType.TYPE_AMENDMENT)
                setButton(filter_amendments)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRVList(filterType: BookmarkFilterType) {
        var list = emptyList<Element_Bookmark>()
        CoroutineScope(Dispatchers.IO).launch {

            when (filterType) {
                BookmarkFilterType.TYPE_ALL -> {
                    viewModel.getAllBookmarks().collect{ value ->
                        list = value
                    }
                }

                BookmarkFilterType.TYPE_ARTICLE -> {
                    viewModel.getArticleBookmarks().collect{ value ->
                        list = value
                    }
                }

                BookmarkFilterType.TYPE_SCHEDULE -> {
                    viewModel.getScheduleBookmarks().collect{ value ->
                        list = value
                    }
                }

                BookmarkFilterType.TYPE_AMENDMENT -> {
                    viewModel.getAmendmentBookmarks().collect{ value ->
                        list = value
                    }
                }
            }

            withContext(Dispatchers.Main) {
                bookmarksListAdapter.also { rvadap ->
                    rvadap.bookmarkslist = list.toMutableList()
                    rvadap.notifyDataSetChanged()
                }
            }
        }


    }

    private fun initiateButtons() {


        bgTypedValue = TypedValue()
        theme.resolveAttribute(R.attr.background, bgTypedValue, true)

        thumbTypedValue = TypedValue()
        theme.resolveAttribute(R.attr.thumbTint, thumbTypedValue, true)


        filter_all = findViewById(R.id.activity_bookmarks_filter_all)
        filter_articles = findViewById(R.id.activity_bookmarks_filter_articles)
        filter_schedules = findViewById(R.id.activity_bookmarks_filter_schedules)
        filter_amendments = findViewById(R.id.activity_bookmarks_filter_amendments)

        filter_all.setOnClickListener(this@Activity_Bookmarks)
        filter_articles.setOnClickListener(this@Activity_Bookmarks)
        filter_schedules.setOnClickListener(this@Activity_Bookmarks)
        filter_amendments.setOnClickListener(this@Activity_Bookmarks)
        setAllButtons()
        setButton(filter_all)


    }

    private fun setAllButtons() {

        val btnbackground = bgTypedValue.data
        val btntextcolor = thumbTypedValue.data
        filter_all.also {
            it.setBackgroundColor(btnbackground)
            it.setTextColor(btntextcolor)
        }
        filter_articles.also {
            it.setBackgroundColor(btnbackground)
            it.setTextColor(btntextcolor)
        }
        filter_schedules.also {
            it.setBackgroundColor(btnbackground)
            it.setTextColor(btntextcolor)
        }
        filter_amendments.also {
            it.setBackgroundColor(btnbackground)
            it.setTextColor(btntextcolor)
        }

    }

    private fun setButton(btn: Button) {
        val btntextcolor = bgTypedValue.data
        val btnbackground = thumbTypedValue.data

        btn.also {
            it.setBackgroundColor(btnbackground)
            it.setTextColor(btntextcolor)
        }
    }


    private fun setUpRV() {

        bookmarksListAdapter =
            Adapter_Bookmarks(rvListAll.toMutableList(), this@Activity_Bookmarks, viewModel)

        rvBookmarks = findViewById(R.id.activity_bookmarks_rvBookmarkslist)
        rvBookmarks.also {
            it.adapter = bookmarksListAdapter
            it.layoutManager = LinearLayoutManager(this@Activity_Bookmarks)
            it.addItemDecoration(
                DividerItemDecoration(
                    this@Activity_Bookmarks,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        ItemTouchHelper(object : SwipeToDeleteCallback(this@Activity_Bookmarks) {
            override fun onMove(
                v: RecyclerView,
                h: RecyclerView.ViewHolder,
                t: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) =
                bookmarksListAdapter.removeAt(h.absoluteAdapterPosition)

        }).attachToRecyclerView(rvBookmarks)
    }


}