package com.example.constitutionofindia

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.IndiaCanon.constitutionofindia.R

class Activity_About : AppCompatActivity() {

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"

    lateinit var CoI_SharedPref : SharedPreferences

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeDefault)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_about)



        setSupportActionBar(findViewById(R.id.activity_about_tb))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)




        findViewById<ImageView>(R.id.activity_about_ivAppIcon).also {
            it.setImageResource(R.drawable.app_coin_icon_whiteback_figma)
        }


        val packageInfo = applicationContext.packageManager.getPackageInfo(applicationContext.packageName,0)

        findViewById<TextView>(R.id.activity_about_tvVersionCode).also {
            it.text = ("Version "+packageInfo.versionName)
        }






    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
//            Intent(this, Activity_Main::class.java).also {
//                startActivity(it)
//            }
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}