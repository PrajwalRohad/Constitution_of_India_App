package com.example.constitutionofindia.articles

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.AdManager
import com.example.constitutionofindia.CoIApplication
import com.example.constitutionofindia.ThemePreference
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class Activity_Articleslist : AppCompatActivity(), Adapter_Articleslist.ArticlesListInterface {
    private lateinit var Activity_Articleslist_BannerAd: AdView

    private lateinit var partNumKey: String
    private lateinit var partNum: String
    private lateinit var partName: String

    private lateinit var chapterNumKeyList: MutableList<String>
    private lateinit var chapterNumList: MutableList<String>
    private lateinit var chapterNameList: MutableList<String>

    private lateinit var sectionsNameKeyList: MutableList<String>
    private lateinit var sectionsNameList: MutableList<String>

    private lateinit var articlesIndexList: MutableList<Int>
    private lateinit var articlesNumList: MutableList<String>
    private lateinit var articlesNameList: MutableList<String>
    private lateinit var articlesTextList: MutableList<String>
    private lateinit var articlesFootnoteList: MutableList<String>

    private val THEME_PREF = "theme_pref"
    private val THEME_SELECTED = "theme_selected"
    private val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    private lateinit var CoI_SharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode =
            CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_articleslist)



        intent.extras.also {
            partNumKey = it?.getString("partNum").toString()
        }

        lifecycleScope.launch(Dispatchers.Default) {

            val jpartsobj = CoIApplication.assetManager.partsJSON
            val partobj = jpartsobj.getJSONObject(partNumKey)
            partNum = partobj.getString("part_num")
            partName = partobj.getString("part_name")

            val chaptersList = partobj.getJSONArray("chapters")
            var sectionsList: JSONArray


            chapterNumKeyList = mutableListOf()
            chapterNumList = mutableListOf()
            chapterNameList = mutableListOf()

            sectionsNameKeyList = mutableListOf()
            sectionsNameList = mutableListOf()

            articlesIndexList = mutableListOf()
            articlesNumList = mutableListOf()
            articlesNameList = mutableListOf()
            articlesTextList = mutableListOf()
            articlesFootnoteList = mutableListOf()


            for (i in 0..chaptersList.length() - 1) {
                val chapter = partobj.getJSONObject(chaptersList[i].toString())
//            Log.d("Article123", "chapter is -"+chapter)
                sectionsList = chapter.getJSONArray("sections")
//            Log.d("Article123", "sections are -"+sectionsList)
//
                for (j in 0..sectionsList.length() - 1) {
                    val section = chapter.getJSONObject(sectionsList[j].toString())

                    val articles = section.getJSONArray("articles")
                    for (k in 0..articles.length() - 1) {
                        chapterNumKeyList.add(chaptersList[i].toString())

                        chapterNumList.add(chapter.getString("chapter_num"))
                        chapterNameList.add(chapter.getString("chapter_name"))

                        sectionsNameKeyList.add(sectionsList[j].toString())
                        sectionsNameList.add(section.getString("section_name").toString())

                        articlesIndexList.add(k)
                        articlesNumList.add(articles.getJSONObject(k).getString("num"))
                        articlesNameList.add(articles.getJSONObject(k).getString("name"))
                        articlesTextList.add(articles.getJSONObject(k).getString("text"))
                        articlesFootnoteList.add(articles.getJSONObject(k).getString("footnote"))

                    }
                }
            }


            val articleItemList = mutableListOf<Element_Articleslist>()

            for (i in articlesNumList.indices) {
                articleItemList.add(Element_Articleslist(articlesNumList[i], articlesNameList[i]))
            }

            withContext(Dispatchers.Main) {
                val articlesListAdapter =
                    Adapter_Articleslist(articleItemList, this@Activity_Articleslist)

                findViewById<RecyclerView>(R.id.activity_articleslist_rvArticleslist).also {
                    it.adapter = articlesListAdapter
                    it.layoutManager = LinearLayoutManager(this@Activity_Articleslist)
                    it.addItemDecoration(
                        DividerItemDecoration(
                            this@Activity_Articleslist,
                            LinearLayoutManager.VERTICAL
                        )
                    )
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

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {

            Activity_Articleslist_BannerAd = findViewById(R.id.activity_articleslist_adView)
            withContext(Dispatchers.Main) {
                AdManager().loadBannerAd(Activity_Articleslist_BannerAd)
            }
        }


    }


    override fun onDestroy() {
        super.onDestroy()

        Activity_Articleslist_BannerAd.removeAllViews()
        Activity_Articleslist_BannerAd.destroy()


    }

    override fun ArticleOnClick(position: Int) {
        Intent(this, Activity_Article::class.java).also {
            it.putExtra("partNumKey", partNumKey)
            it.putExtra("partNum", partNum)
            it.putExtra("partName", partName)

            it.putExtra("chapterNumKey", chapterNumKeyList[position])
            it.putExtra("chapterNum", chapterNumList[position])
            it.putExtra("chapterName", chapterNameList[position])

            it.putExtra("sectionNameKey", sectionsNameKeyList[position])
            it.putExtra("sectionName", sectionsNameList[position])

            it.putExtra("articlesIndex", articlesIndexList[position].toString())
            it.putExtra("articlesNum", articlesNumList[position])
            it.putExtra("articlesName", articlesNameList[position])
            it.putExtra("articlesText", articlesTextList[position])
            it.putExtra("articlesFootnote", articlesFootnoteList[position])

            startActivity(it)
        }
    }
}