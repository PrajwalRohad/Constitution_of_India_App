package com.example.constitutionofindia.parts

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
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
import org.json.JSONArray
import org.json.JSONObject

class Activity_Partslist : AppCompatActivity(),
Adapter_Partslist.PartsListInterface    {
    lateinit var Activity_Partslist_BannerAd : AdView
    lateinit var keysparts : JSONArray

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"

    lateinit var CoI_SharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeDefault)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_partslist)

        MobileAds.initialize(this){}
        val Activity_Partslist_BannerAdRequest = AdRequest.Builder().build()

        Activity_Partslist_BannerAd = findViewById(R.id.activity_partslist_adView)
        Activity_Partslist_BannerAd.loadAd(Activity_Partslist_BannerAdRequest)


//        val partTitlesArray = resources.getStringArray(R.array.PartTitles)
//        val partNamesArray  = resources.getStringArray(R.array.PartNames)
//        val partRangesArray = resources.getStringArray(R.array.PartRanges)

        val partItemslist = mutableListOf<Element_Partslist>()

        val jpartsfile = applicationContext.assets.open("parts.json").bufferedReader().use {
            it.readText()
        }
        val jpartobj = JSONObject(jpartsfile)
        keysparts = jpartobj.names()


        for(i in 0 .. keysparts.length()-1){
            val num = jpartobj.getJSONObject(keysparts[i].toString()).getString("part_num")
            val name = jpartobj.getJSONObject(keysparts[i].toString()).getString("part_name")
            val range = jpartobj.getJSONObject(keysparts[i].toString()).getString("range")
            partItemslist.add(Element_Partslist(num, name, "Articles\n"+range))
//            partItemslist.add(Element_Partslist(partTitlesArray[i], partNamesArray[i], "Articles\n"+partRangesArray[i]))
        }

        val partslistAdapter = Adapter_Partslist(partItemslist, this)
        findViewById<RecyclerView>(R.id.activity_partslist_rvPartslist).also {
            it.adapter = partslistAdapter
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        }





    }

    override fun onDestroy() {
        super.onDestroy()

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

    private fun showFeedbackDialog(){
        val reviewManager = ReviewManagerFactory.create(applicationContext)
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful){
                reviewManager.launchReviewFlow(this, it.result)
            }
        }
    }
}