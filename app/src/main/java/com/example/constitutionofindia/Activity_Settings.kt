package com.example.constitutionofindia

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.IndiaCanon.constitutionofindia.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Activity_Settings : AppCompatActivity(), View.OnClickListener {

    private val THEME_PREF = "theme_pref"
    private val THEME_SELECTED = "theme_selected"
    private val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    lateinit var CoI_SharedPref: SharedPreferences
    var fontsize: Float = 1.0f
    var themeselected: Int = R.style.ThemeReplyBlue
    var viewertheme: Int = R.style.ThemeReplyBlue
    var viewerfont: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        viewertheme = themeselected
        val nightmode =
            CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)

        setTheme(themeselected)

        viewerfont = CoI_SharedPref.getInt(FONT_SIZE, 1)
        fontsize = 0.5f + (0.25f * viewerfont)

        setContentView(R.layout.activity_settings)




        setSupportActionBar(findViewById(R.id.activity_settings_tb))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)


        lifecycleScope.launch(Dispatchers.IO) {
            findViewById<CardView>(R.id.activity_settings_cvthemeDefault).setOnClickListener(this@Activity_Settings)
            findViewById<CardView>(R.id.activity_settings_cvthemeReplyBlue).setOnClickListener(this@Activity_Settings)
            findViewById<CardView>(R.id.activity_settings_cvthemeBasilGreen).setOnClickListener(this@Activity_Settings)
            findViewById<CardView>(R.id.activity_settings_cvthemeYellow).setOnClickListener(this@Activity_Settings)
            findViewById<CardView>(R.id.activity_settings_cvthemeteal).setOnClickListener(this@Activity_Settings)
            findViewById<CardView>(R.id.activity_settings_cvthemefornightlypurple).setOnClickListener(
                this@Activity_Settings
            )
            findViewById<CardView>(R.id.activity_settings_cvthemepurple).setOnClickListener(this@Activity_Settings)

            findViewById<Button>(R.id.activity_settings_btnthemeapply).setOnClickListener(this@Activity_Settings)
        }


        findViewById<SwitchCompat>(R.id.activity_settings_toggleDarkmode).also {
            if (nightmode == AppCompatDelegate.MODE_NIGHT_YES || isDarkModeOn()) {
                it.isChecked = true
            } else {
                it.isChecked = false
            }

            it.setOnCheckedChangeListener { _, isChecked ->
                val editor = CoI_SharedPref.edit()
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.also { ed ->
                        ed.putInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_YES)
                        ed.apply()
                    }
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    editor.also { ed ->
                        ed.putInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_NO)
                        ed.apply()
                    }
                }
                editor.clear()
            }
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Intent(this@Activity_Settings, Activity_Main::class.java).also {
                    startActivity(it)
                }
                finish()

            }
        })

        supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.activity_settings_viewerlayout,
                Activity_Settings_viewerFragment.newInstance(themeselected, fontsize)
            )
            commit()
        }


        findViewById<SeekBar>(R.id.activity_settings_sbtextsize).also {

            it.progress = CoI_SharedPref.getInt(FONT_SIZE, 1)

            it.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        viewerfont = progress
                        viewerUpdate()
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
        }


    }

    override fun onDestroy() {
        super.onDestroy()

        findViewById<CardView>(R.id.activity_settings_cvthemeDefault).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }
        findViewById<CardView>(R.id.activity_settings_cvthemeReplyBlue).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }
        findViewById<CardView>(R.id.activity_settings_cvthemeBasilGreen).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }
        findViewById<CardView>(R.id.activity_settings_cvthemeYellow).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }
        findViewById<CardView>(R.id.activity_settings_cvthemeteal).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }
        findViewById<CardView>(R.id.activity_settings_cvthemefornightlypurple).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }
        findViewById<CardView>(R.id.activity_settings_cvthemepurple).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }

        findViewById<SwitchCompat>(R.id.activity_settings_toggleDarkmode).also {
            it.setOnCheckedChangeListener(null)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
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

        when (view!!.id) {

            R.id.activity_settings_cvthemeDefault -> {
                viewertheme = R.style.ThemeDefault
                viewerUpdate()
            }

            R.id.activity_settings_cvthemeReplyBlue -> {
                viewertheme = R.style.ThemeReplyBlue
                viewerUpdate()
            }

            R.id.activity_settings_cvthemeBasilGreen -> {
                viewertheme = R.style.ThemeBasilGreen
                viewerUpdate()
            }

            R.id.activity_settings_cvthemeYellow -> {
                viewertheme = R.style.ThemeYellow
                viewerUpdate()
            }

            R.id.activity_settings_cvthemeteal -> {
                viewertheme = R.style.ThemeTeal
                viewerUpdate()
            }

            R.id.activity_settings_cvthemefornightlypurple -> {
                viewertheme = R.style.ThemeFornightlyPurple
                viewerUpdate()
            }

            R.id.activity_settings_cvthemepurple -> {
                viewertheme = R.style.ThemePurple
                viewerUpdate()
            }

            R.id.activity_settings_btnthemeapply -> {
                editor.also { ed ->
                    ed.putInt(THEME_SELECTED, viewertheme)
                    ed.putInt(FONT_SIZE, viewerfont)
                    ed.apply()
                }
                recreate()
            }

        }
        editor.clear()
    }


    fun isDarkModeOn(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeOn = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        return isDarkModeOn
    }

    fun viewerUpdate() {
        val newfontsize = 0.5f + (0.25f * viewerfont)

        supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.activity_settings_viewerlayout,
                Activity_Settings_viewerFragment.newInstance(viewertheme, newfontsize)
            )
            commit()
        }
    }

}