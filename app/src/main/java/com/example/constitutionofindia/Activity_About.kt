package com.example.constitutionofindia

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.amendments.Activity_Amendment_SOR

class Activity_About : AppCompatActivity() {

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    lateinit var CoI_SharedPref: SharedPreferences

    private lateinit var appIconAnimation: AnimatedVectorDrawable

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode =
            CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_about)



        setSupportActionBar(findViewById(R.id.activity_about_tb))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)




        findViewById<ImageView>(R.id.activity_about_ivAppIcon).also {
            it.setImageResource(R.drawable.app_vista_icon_transparent_1080dp)

            it.setOnClickListener { view ->
                it.setImageResource(R.drawable.avd_vista_icon_transparent_1080px)
                appIconAnimation = it.drawable as AnimatedVectorDrawable
                appIconAnimation.start()

//                if(appIconAnimation.isRunning){
//                    it.setImageResource(R.drawable.app_coin_icon_whiteback_figma)
//                }
            }
        }


        val packageInfo =
            applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)

        findViewById<TextView>(R.id.activity_about_tvVersionCode).also {
            it.text = ("Version " + packageInfo.versionName)
        }

        findViewById<TextView>(R.id.activity_about_tvPrivacyPolicy).also {
            it.setOnClickListener {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://doc-hosting.flycricket.io/constitution-of-india-privacy-policy/797bf621-9116-40ef-b594-289ea15b8f1d/privacy")
                ).also { newintent ->
                    startActivity(newintent)
                }
            }
        }

        findViewById<TextView>(R.id.activity_about_tvTnC).also {
            it.setOnClickListener {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://doc-hosting.flycricket.io/constitution-of-india-terms-of-use/feafb3ec-e595-4eac-b3e3-469defe749fd/terms")
                ).also { newintent ->
                    startActivity(newintent)
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

//    override fun onStart() {
//        super.onStart()
//
////        val iconImage = findViewById<ImageView>(R.id.activity_about_ivAppIcon).apply {
////
////        }
////
////        iconImage.setOnClickListener({ appIconAnimation.start() })
//    }

    override fun onDestroy() {
        super.onDestroy()

        findViewById<ImageView>(R.id.activity_about_ivAppIcon).also {
            it.setOnClickListener(null)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
//            Intent(this, Activity_Main::class.java).also {
//                startActivity(it)
//            }
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}