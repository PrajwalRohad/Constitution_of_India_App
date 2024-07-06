package com.example.constitutionofindia.preamble

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.ThemePreference
import com.example.constitutionofindia.AdManager
import com.example.constitutionofindia.CoIApplication
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class Activity_Preamble : AppCompatActivity() {
    private lateinit var Activity_Preamble_BannerAd: AdView
//    private var Activity_Preamble_BannerAd: AdView? = null
//    private var Activity_Preamble_BannerAd: WeakReference<AdView>? = null


    private val THEME_PREF = "theme_pref"
    private val THEME_SELECTED = "theme_selected"
    private val NIGHT_MODE = "night_mode"
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

        setContentView(R.layout.activity_preamble)


        lifecycleScope.launch(Dispatchers.IO) {
            Activity_Preamble_BannerAd = findViewById(R.id.activity_preamble_adView)
            withContext(Dispatchers.Main) {
                AdManager().loadBannerAd(Activity_Preamble_BannerAd)
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


    override fun onStart() {
        super.onStart()




        lifecycleScope.launch(Dispatchers.Default) {

            val jpreambleobj = CoIApplication.assetManager.preambleJSON
            val preambletext = jpreambleobj.getString("text")
            val preamblefootnote = jpreambleobj.getString("footnote")


            withContext(Dispatchers.Main) {
                findViewById<TextView>(R.id.activity_preamble_tvtext).also {
                    it.setText(Html.fromHtml(preambletext, Html.FROM_HTML_MODE_LEGACY))
                }
                findViewById<TextView>(R.id.activity_preamble_tvfootnote).also {
                    it.setText(Html.fromHtml(preamblefootnote, Html.FROM_HTML_MODE_LEGACY))
                }
            }
        }
    }



    override fun onDestroy() {
        // Remove AdView from its parent view before destroying
        Activity_Preamble_BannerAd.removeAllViews()
        Activity_Preamble_BannerAd.destroy()

        super.onDestroy()
    }
}