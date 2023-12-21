package com.example.constitutionofindia

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import com.IndiaCanon.constitutionofindia.R

class Activity_Settings : AppCompatActivity(), View.OnClickListener {

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"

    lateinit var CoI_SharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_settings)




        setSupportActionBar(findViewById(R.id.activity_settings_tb))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)



        findViewById<CardView>(R.id.activity_settings_cvthemeDefault).setOnClickListener(this)
        findViewById<CardView>(R.id.activity_settings_cvthemeReplyBlue).setOnClickListener(this)
        findViewById<CardView>(R.id.activity_settings_cvthemeBasilGreen).setOnClickListener(this)
        findViewById<CardView>(R.id.activity_settings_cvthemeYellow).setOnClickListener(this)
        findViewById<CardView>(R.id.activity_settings_cvthemeteal).setOnClickListener(this)
        findViewById<CardView>(R.id.activity_settings_cvthemefornightlypurple).setOnClickListener(this)
        findViewById<CardView>(R.id.activity_settings_cvthemepurple).setOnClickListener(this)


        findViewById<SwitchCompat>(R.id.activity_settings_toggleDarkmode).also {
            if(nightmode == AppCompatDelegate.MODE_NIGHT_YES ||  isDarkModeOn()){
                it.isChecked = true
            }else{
                it.isChecked = false
            }

            it.setOnCheckedChangeListener{ _, isChecked ->
                val editor = CoI_SharedPref.edit()
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.also {
                        it.putInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_YES)
                        it.apply()
                    }
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    editor.also {
                        it.putInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_NO)
                        it.apply()
                    }
                }
            }
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Intent(this@Activity_Settings, Activity_Main::class.java).also {
                    startActivity(it)
                }
                finish()

            }
        })


    }

    override fun onDestroy() {
        findViewById<CardView>(R.id.activity_settings_cvthemeDefault).removeAllViews()
        findViewById<CardView>(R.id.activity_settings_cvthemeReplyBlue).removeAllViews()
        findViewById<CardView>(R.id.activity_settings_cvthemeBasilGreen).removeAllViews()
        findViewById<CardView>(R.id.activity_settings_cvthemeYellow).removeAllViews()
        findViewById<CardView>(R.id.activity_settings_cvthemeteal).removeAllViews()
        findViewById<CardView>(R.id.activity_settings_cvthemefornightlypurple).removeAllViews()
        findViewById<CardView>(R.id.activity_settings_cvthemepurple).removeAllViews()

        super.onDestroy()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            Intent(this, Activity_Main::class.java).also {
                startActivity(it)
            }
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {

        val editor = CoI_SharedPref.edit()

        when(view!!.id){

            R.id.activity_settings_cvthemeDefault -> {
                editor.also {
                    it.putInt(THEME_SELECTED, R.style.ThemeDefault)
                    it.apply()
                }
                recreate()

            }

            R.id.activity_settings_cvthemeReplyBlue -> {
                editor.also {
                    it.putInt(THEME_SELECTED, R.style.ThemeReplyBlue)
                    it.apply()
                }
                recreate()

            }

            R.id.activity_settings_cvthemeBasilGreen -> {
                editor.also {
                    it.putInt(THEME_SELECTED, R.style.ThemeBasilGreen)
                    it.apply()
                }
                recreate()

            }

            R.id.activity_settings_cvthemeYellow -> {
                editor.also {
                    it.putInt(THEME_SELECTED, R.style.ThemeYellow)
                    it.apply()
                }
                recreate()

            }

            R.id.activity_settings_cvthemeteal -> {
                editor.also {
                    it.putInt(THEME_SELECTED, R.style.ThemeTeal)
                    it.apply()
                }
                recreate()

            }

            R.id.activity_settings_cvthemefornightlypurple -> {
                editor.also {
                    it.putInt(THEME_SELECTED, R.style.ThemeFornightlyPurple)
                    it.apply()
                }
                recreate()

            }

            R.id.activity_settings_cvthemepurple -> {
                editor.also {
                    it.putInt(THEME_SELECTED, R.style.ThemePurple)
                    it.apply()
                }
                recreate()

            }


        }

    }


    fun isDarkModeOn(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeOn = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        return isDarkModeOn
    }
}