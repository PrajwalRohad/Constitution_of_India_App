package com.example.constitutionofindia.schedules

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

class Activity_Scheduleslist : AppCompatActivity(), Adapter_Scheduleslist.SchedulesListInterface {
    lateinit var Activity_Scheduleslist_BannerAd: AdView

    //    lateinit var scheduleNumArray : Array<String>
//    lateinit var scheduleNameArray : Array<String>
    lateinit var keysSchedules: JSONArray

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

        setContentView(R.layout.activity_scheduleslist)

//        scheduleNumArray = resources.getStringArray(R.array.ScheduleNum)
//        scheduleNameArray = resources.getStringArray(R.array.ScheduleName)

        lifecycleScope.launch(Dispatchers.Default){
            val jsonSchedulefile =
                applicationContext.assets.open("schedules.json").bufferedReader().use {
                    it.readText()
                }

            val jsonScheduleobj = JSONObject(jsonSchedulefile)
            keysSchedules = jsonScheduleobj.names()!!

            val scheduleItemList = mutableListOf<Element_Scheduleslist>()

            for (i in 1..keysSchedules.length() - 1) {
//            scheduleItemList.add(Element_Scheduleslist(scheduleNumArray[i],scheduleNameArray[i]))
                val num = jsonScheduleobj.getJSONObject(keysSchedules[i].toString()).getString("num")
                val name = jsonScheduleobj.getJSONObject(keysSchedules[i].toString()).getString("name")
                scheduleItemList.add(Element_Scheduleslist(num, name))
            }

            withContext(Dispatchers.Main){
                val schedulesListAdapter = Adapter_Scheduleslist(scheduleItemList, this@Activity_Scheduleslist)

                findViewById<RecyclerView>(R.id.activity_scheduleslist_rvScheduleslist).also {
                    it.adapter = schedulesListAdapter
                    it.layoutManager = LinearLayoutManager(this@Activity_Scheduleslist)
                    it.addItemDecoration(DividerItemDecoration(this@Activity_Scheduleslist, LinearLayoutManager.VERTICAL))
                }
            }
        }


    }


    override fun onResume() {
        super.onResume()

        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@Activity_Scheduleslist) {}
//            val Activity_Scheduleslist_BannerAdRequest = AdRequest.Builder().build()

            Activity_Scheduleslist_BannerAd = findViewById(R.id.activity_scheduleslist_adView)
            withContext(Dispatchers.Main) {
//                Activity_Scheduleslist_BannerAd.loadAd(Activity_Scheduleslist_BannerAdRequest)
                AdManager().loadBannerAd(Activity_Scheduleslist_BannerAd)
            }
        }
    }

    override fun onDestroy() {
        Activity_Scheduleslist_BannerAd.removeAllViews()
        Activity_Scheduleslist_BannerAd.destroy()
        super.onDestroy()
    }

    override fun ScheduleOnClick(position: Int) {
        Intent(this, Activity_Schedule::class.java).also {
//            it.putExtra("scheduleName", scheduleNumArray[position]+"\n"+scheduleNameArray[position])
            it.putExtra("scheduleName", keysSchedules[position + 1].toString())

            startActivity(it)
        }
    }
}