package com.example.constitutionofindia.parts

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
import com.example.constitutionofindia.ThemePreference
import com.example.constitutionofindia.articles.Activity_Articleslist
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class Activity_Partslist : AppCompatActivity(),
    Adapter_Partslist.PartsListInterface {
    lateinit var Activity_Partslist_BannerAd: AdView

    lateinit var keysparts: JSONArray

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
        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_partslist)

//        val partTitlesArray = resources.getStringArray(R.array.PartTitles)
//        val partNamesArray  = resources.getStringArray(R.array.PartNames)
//        val partRangesArray = resources.getStringArray(R.array.PartRanges)


//        val jpartsfile = applicationContext.assets.open("parts.json").bufferedReader().use {
//            it.readText()
//        }
//        val jpartobj = JSONObject(jpartsfile)
//        keysparts = jpartobj.names()!!
//
//        val partItemslist = mutableListOf<Element_Partslist>()
//
//        for (i in 0..keysparts.length() - 1) {
//            val num = jpartobj.getJSONObject(keysparts[i].toString()).getString("part_num")
//            val name = jpartobj.getJSONObject(keysparts[i].toString()).getString("part_name")
//            val range = jpartobj.getJSONObject(keysparts[i].toString()).getString("range")
//            partItemslist.add(Element_Partslist(num, name, "Articles\n" + range))
////            partItemslist.add(Element_Partslist(partTitlesArray[i], partNamesArray[i], "Articles\n"+partRangesArray[i]))
//        }
//        val partslistAdapter = Adapter_Partslist(partItemslist, this@Activity_Partslist)
//
//        findViewById<RecyclerView>(R.id.activity_partslist_rvPartslist).also {
//            it.adapter = partslistAdapter
//            it.layoutManager = LinearLayoutManager(this@Activity_Partslist)
//            it.addItemDecoration(DividerItemDecoration(this@Activity_Partslist, LinearLayoutManager.VERTICAL))
//        }

        lifecycleScope.launch(Dispatchers.Default){
            val jpartsfile = applicationContext.assets.open("parts.json").bufferedReader().use {
                it.readText()
            }
            val jpartobj = JSONObject(jpartsfile)
            keysparts = jpartobj.names()!!

            val partItemslist = mutableListOf<Element_Partslist>()

            for (i in 0..keysparts.length() - 1) {
                val num = jpartobj.getJSONObject(keysparts[i].toString()).getString("part_num")
                val name = jpartobj.getJSONObject(keysparts[i].toString()).getString("part_name")
                val range = jpartobj.getJSONObject(keysparts[i].toString()).getString("range")
                partItemslist.add(Element_Partslist(num, name, "Articles\n" + range))
//            partItemslist.add(Element_Partslist(partTitlesArray[i], partNamesArray[i], "Articles\n"+partRangesArray[i]))
            }
            withContext(Dispatchers.Main){
                val partslistAdapter = Adapter_Partslist(partItemslist, this@Activity_Partslist)

                findViewById<RecyclerView>(R.id.activity_partslist_rvPartslist).also {
                    it.adapter = partslistAdapter
                    it.layoutManager = LinearLayoutManager(this@Activity_Partslist)
                    it.addItemDecoration(DividerItemDecoration(this@Activity_Partslist, LinearLayoutManager.VERTICAL))
                }

            }
        }





    }


    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@Activity_Partslist) {}
            val Activity_Partslist_BannerAdRequest = AdRequest.Builder().build()

            Activity_Partslist_BannerAd = findViewById(R.id.activity_partslist_adView)
            withContext(Dispatchers.Main) {
                Activity_Partslist_BannerAd.loadAd(Activity_Partslist_BannerAdRequest)

            }
        }
    }

    override fun onDestroy() {
        Activity_Partslist_BannerAd.removeAllViews()
        Activity_Partslist_BannerAd.destroy()
        super.onDestroy()

//        Activity_Partslist_BannerAd.removeAllViews()

        showFeedbackDialog()
//        Toast.makeText(this, "Exited!", Toast.LENGTH_LONG).show()
    }

    override fun PartOnClick(position: Int) {
//        Toast.makeText(this, "clicked "+position, Toast.LENGTH_LONG).show()

        Intent(this, Activity_Articleslist::class.java).also {
            it.putExtra("partNum", keysparts[position].toString())
            startActivity(it)
        }
    }

    private fun showFeedbackDialog() {
        val reviewManager = ReviewManagerFactory.create(applicationContext)
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                reviewManager.launchReviewFlow(this, it.result)
            }
        }
    }
}