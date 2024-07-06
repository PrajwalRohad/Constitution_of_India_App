package com.example.constitutionofindia.articles

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.inmobi.ads.controllers.d
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

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

//    private var layoutParams : ConstraintLayout.LayoutParams? = null
//    private var coordinates = arrayOf(0,0)

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var factory: BookmarkViewModelFactory

    private lateinit var bookmark: Element_Bookmark
    private lateinit var stored_bookmark: List<Element_Bookmark>
    private lateinit var dataList: MutableList<String>

    private lateinit var btnbookmark: FloatingActionButton
    private var bookmarkState: Boolean = false
    private lateinit var bookmarkManager: BookmarkManager

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




        findViewById<ScrollView>(R.id.activity_article_svText).also {
            it.setOnScrollChangeListener(
                View.OnScrollChangeListener { view, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->

                    if (scrollY >= view.top + 30) {
//                        Toast.makeText(this@Activity_Article, "Yes, Scrolled", Toast.LENGTH_LONG).show()
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
                    } else {
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


        tvArticle.setOnTouchListener(this@Activity_Article)


    }

    override fun onStart() {
        super.onStart()

        bookmarkManager = BookmarkManager()
        btnbookmark = findViewById(R.id.activity_article_fabBookmark)
        btnbookmark.setOnClickListener(this@Activity_Article)


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
                    it.showMessage(bookmarkState, this.findViewById(R.id.activity_article_layout),R.id.activity_article_AdCardView)
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

        when (v?.id) {
            R.id.activity_article_tvHeadline -> {

                tvArticle.maxLines = Int.MAX_VALUE

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
                tvSubSection.also { tv ->
                    if (!sectionName.equals("null")) {
                        tv.visibility = View.VISIBLE
                    }
                }
            }
        }

        v?.performClick()
        return true
    }

//    override fun onLongClick(v: View?): Boolean {
//
//        val shadowbuilder = DragShadowBuilder(v)
//        v?.startDragAndDrop(null, shadowbuilder, v, 0)
////        v?.visibility = View.INVISIBLE
////        Log.d("drag123", "long click")
//        return true
//    }

//    override fun onDrag(v: View?, event: DragEvent?): Boolean {
////        Log.d("drag123", "dragging")
//        v?.visibility = View.INVISIBLE
////        val dragaction = event?.action
////        val view = event?.localState as View
////        var oldX = 0
////        var oldY = 0
//
//        when(event?.action) {
//
//            DragEvent.ACTION_DRAG_STARTED -> {
//                Log.d("drag123", "drag started")
////                layoutParams = view.layoutParams as ConstraintLayout.LayoutParams?
//                coordinates[0] = v?.right as Int
//                coordinates[1] = v.bottom
//                Log.d("drag123", "old X = "+ coordinates[0])
//                Log.d("drag123", "old Y = "+ coordinates[1])
//                return true
//            }
//
//            DragEvent.ACTION_DRAG_ENTERED -> {
////                Log.d("drag123", "drag entered")
////                v?.invalidate()
//                return true
//            }
//
//            DragEvent.ACTION_DRAG_EXITED -> {
////                Log.d("drag123", "drag exited")
////                v?.invalidate()
//                return true
//            }
//
//            DragEvent.ACTION_DROP -> {
//                Log.d("drag123", "drag dropped")
//
////                v?.invalidate()
//
////                val view = event.localState as View
////                val parentview = view.parent as ViewGroup
////                parentview.removeView(view)
////
////                val container = v as ConstraintLayout
////                container.addView(view)
//
////                var newLayoutParams = ConstraintLayout.LayoutParams(v?.layoutParams)
////                newLayoutParams.rightMargin = 145
////                v?.layoutParams = newLayoutParams
////                v.visibility = View.VISIBLE
//
////                val displaymetrics = DisplayMetrics()
////                layoutParams?.marginEnd = displaymetrics.widthPixels - event.x.toInt()
////                layoutParams?.bottomMargin = displaymetrics.heightPixels - event.y.toInt()
////                view.layoutParams = layoutParams
////                view.visibility = View.VISIBLE
////                view.invalidate()
//
//                return true
//            }
//
//            DragEvent.ACTION_DRAG_ENDED -> {
//                Log.d("drag123", "drag ended")
//                Log.d("drag123", "old X = "+ coordinates[0])
//                Log.d("drag123", "old Y = "+ coordinates[1])
//                Log.d("drag123", "event X = "+(event.x.toInt()))
//                Log.d("drag123", "event Y = "+(event.y.toInt()))
////                Log.d("drag123", "new X = "+(oldX + event.x.toInt()))
////                Log.d("drag123", "new Y = "+(oldY + event.y.toInt()))
////                v?.invalidate()
//
//                v?.visibility = View.VISIBLE
//                val newX = coordinates[0] - event.x.toInt()
//                val newY = coordinates[1] - event.y.toInt()
//                Log.d("drag123", "new X = "+ newX)
//                Log.d("drag123", "new Y = "+ newY)
//
////                val newLayoutParams = v?.layoutParams as ConstraintLayout.LayoutParams
////                newLayoutParams.marginEnd = newX
////                newLayoutParams.bottomMargin = newY
//
////                v?.animate().also { anim ->
////                    anim?.x(newX.toFloat())
////                    anim?.y(newY.toFloat())
////                    anim?.duration = 0
////                    anim?.start()
////                }
////                v.layoutParams = newLayoutParams
////                v.requestLayout()  //works if constraints set with ConstraintSet
//                return true
//            }
//
////            else -> return false
//
//        }
//
//
//        return true
//    }


}



















