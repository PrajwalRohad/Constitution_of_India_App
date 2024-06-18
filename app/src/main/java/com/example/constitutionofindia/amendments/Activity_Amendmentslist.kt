package com.example.constitutionofindia.amendments

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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class Activity_Amendmentslist : AppCompatActivity(), Adapter_Amendmentslist.AmendmentListInterface {
    lateinit var Activity_Amendmentslist_BannerAd: AdView

//    lateinit var amendmentNameArray : Array<String>
//    lateinit var amendmentYearArray : Array<String>
    lateinit var keysamendments: JSONArray

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
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_amendmentslist)

//        amendmentNameArray = resources.getStringArray(R.array.AmendmentNames)
//        amendmentYearArray = resources.getStringArray(R.array.AmendmentYears)
        lifecycleScope.launch(Dispatchers.Default) {

            val amendmentItemList = mutableListOf<Element_Amendmentslist>()

            val jsonamendmentfile: String =
                applicationContext.assets.open("amendments.json").bufferedReader().use {
                    it.readText()
                }

            val jsonamendmentobj = JSONObject(jsonamendmentfile)
            keysamendments = jsonamendmentobj.names()

            for (i in 1..keysamendments.length() - 1) {
                val name =
                    jsonamendmentobj.getJSONObject(keysamendments[i].toString()).getString("name")
                val year =
                    jsonamendmentobj.getJSONObject(keysamendments[i].toString()).getString("Year")
                amendmentItemList.add(Element_Amendmentslist(name, year))
            }


//        for(i in amendmentNameArray.indices){
//            amendmentItemList.add(Element_Amendmentslist(amendmentNameArray[i], amendmentYearArray[i]))
//        }
            withContext(Dispatchers.Main){
                val amendmentslistAdapter = Adapter_Amendmentslist(amendmentItemList, this@Activity_Amendmentslist)

                findViewById<RecyclerView>(R.id.activity_amendmentslist_rvAmendmentslist).also {
                    it.adapter = amendmentslistAdapter
                    it.layoutManager = LinearLayoutManager(this@Activity_Amendmentslist)
                    it.addItemDecoration(DividerItemDecoration(this@Activity_Amendmentslist, LinearLayoutManager.VERTICAL))
                }
            }
        }

    }

//    override fun onStart() {
//
//
//        super.onStart()
//    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@Activity_Amendmentslist) {}
//            val Activity_Amendmentslist_BannerAdRequest = AdRequest.Builder().build()

            Activity_Amendmentslist_BannerAd = findViewById(R.id.activity_amendmentslist_adView)
            withContext(Dispatchers.Main) {
//                Activity_Amendmentslist_BannerAd.loadAd(Activity_Amendmentslist_BannerAdRequest)
                AdManager().loadBannerAd(Activity_Amendmentslist_BannerAd)
            }
        }

    }

    override fun onDestroy() {
        Activity_Amendmentslist_BannerAd.removeAllViews()
        Activity_Amendmentslist_BannerAd.destroy()
        super.onDestroy()
    }

    override fun AmendmentOnClick(position: Int) {
        Intent(this, Activity_Amendment::class.java).also {
            it.putExtra("amendmentName", keysamendments[position + 1].toString())
            startActivity(it)
        }
    }
}


