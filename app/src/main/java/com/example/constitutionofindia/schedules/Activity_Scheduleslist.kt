package com.example.constitutionofindia.schedules

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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import org.json.JSONArray
import org.json.JSONObject

class Activity_Scheduleslist : AppCompatActivity(), Adapter_Scheduleslist.SchedulesListInterface {
    lateinit var Activity_Scheduleslist_BannerAd : AdView
//    lateinit var scheduleNumArray : Array<String>
//    lateinit var scheduleNameArray : Array<String>
    lateinit var keysSchedules : JSONArray

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

        setContentView(R.layout.activity_scheduleslist)

        MobileAds.initialize(this){}
        val Activity_Scheduleslist_BannerAdRequest = AdRequest.Builder().build()

        Activity_Scheduleslist_BannerAd = findViewById(R.id.activity_scheduleslist_adView)
        Activity_Scheduleslist_BannerAd.loadAd(Activity_Scheduleslist_BannerAdRequest)

//        scheduleNumArray = resources.getStringArray(R.array.ScheduleNum)
//        scheduleNameArray = resources.getStringArray(R.array.ScheduleName)

        val scheduleItemList = mutableListOf<Element_Scheduleslist>()

        val jsonSchedulefile = applicationContext.assets.open("schedules.json").bufferedReader().use {
            it.readText()
        }

        val jsonScheduleobj = JSONObject(jsonSchedulefile)
        keysSchedules = jsonScheduleobj.names()



        for(i in 1..keysSchedules.length()-1){
//            scheduleItemList.add(Element_Scheduleslist(scheduleNumArray[i],scheduleNameArray[i]))
            val num = jsonScheduleobj.getJSONObject(keysSchedules[i].toString()).getString("num")
            val name = jsonScheduleobj.getJSONObject(keysSchedules[i].toString()).getString("name")
            scheduleItemList.add(Element_Scheduleslist(num,name))
        }

        val schedulesListAdapter = Adapter_Scheduleslist(scheduleItemList, this)

        findViewById<RecyclerView>(R.id.activity_scheduleslist_rvScheduleslist).also {
            it.adapter = schedulesListAdapter
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        }







    }

    override fun ScheduleOnClick(position: Int) {
        Intent(this, Activity_Schedule ::class.java).also {
//            it.putExtra("scheduleName", scheduleNumArray[position]+"\n"+scheduleNameArray[position])
            it.putExtra("scheduleName", keysSchedules[position+1].toString())

            startActivity(it)
        }
    }
}