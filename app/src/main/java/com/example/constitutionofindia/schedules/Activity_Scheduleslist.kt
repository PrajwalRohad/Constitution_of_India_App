package com.example.constitutionofindia.schedules

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

class Activity_Scheduleslist : AppCompatActivity(), Adapter_Scheduleslist.SchedulesListInterface {
    lateinit var Activity_Scheduleslist_BannerAd: AdView

    lateinit var keysSchedules: JSONArray

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

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

        lifecycleScope.launch(Dispatchers.Default){
            val jsonScheduleobj = CoIApplication.assetManager.scheduleJSON
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
            Activity_Scheduleslist_BannerAd = findViewById(R.id.activity_scheduleslist_adView)
            withContext(Dispatchers.Main) {
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