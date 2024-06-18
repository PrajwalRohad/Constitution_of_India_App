package com.example.constitutionofindia.preamble

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.AdManager
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

//    private var adLoadingJob: Job? = null


    private val THEME_PREF = "theme_pref"
    private val THEME_SELECTED = "theme_selected"
    private val NIGHT_MODE = "night_mode"

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

//        Activity_Preamble_BannerAd = findViewById(R.id.activity_preamble_adView)
//        val Activity_Preamble_BannerAdRequest = AdRequest.Builder().build()
//        Activity_Preamble_BannerAd?.loadAd(Activity_Preamble_BannerAdRequest)

        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@Activity_Preamble) {}
//            val Activity_Preamble_BannerAdRequest = AdRequest.Builder().build()

            Activity_Preamble_BannerAd = findViewById(R.id.activity_preamble_adView)
            withContext(Dispatchers.Main) {
                AdManager().loadBannerAd(Activity_Preamble_BannerAd)
//                Activity_Preamble_BannerAd.loadAd(Activity_Preamble_BannerAdRequest)
            }
        }



//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
////                adLoadingJob?.cancel()
////                Activity_Preamble_BannerAd?.removeAllViews()
//                Activity_Preamble_BannerAd?.destroy()
//                Activity_Preamble_BannerAd = null
//
//                finish()
//            }
//        })

    }

    override fun onStart() {
        super.onStart()


//        TODO: Putting all data on Web Server/Cloud (Backend)
//        TODO: App Tour on Install


        lifecycleScope.launch(Dispatchers.Default) {

            val jpreamblefile =
                applicationContext.assets.open("preamble.json").bufferedReader().use {
                    it.readText()
                }
            val jpreambleobj = JSONObject(jpreamblefile)
            val preambletext = jpreambleobj.getString("text")
            val preamblefootnote = jpreambleobj.getString("footnote")


            withContext(Dispatchers.Main) {
                findViewById<TextView>(R.id.activity_preamble_tvtext).also {
                    it.setText(Html.fromHtml(preambletext, Html.FROM_HTML_MODE_LEGACY))
//                    it.setText(Html.fromHtml(resources.getText(R.string.PreambleText).toString(), Html.FROM_HTML_MODE_LEGACY))
//                    it.setText(resources.getText(R.string.PreambleText))
                }
                findViewById<TextView>(R.id.activity_preamble_tvfootnote).also {
                    it.setText(Html.fromHtml(preamblefootnote, Html.FROM_HTML_MODE_LEGACY))
//                    it.setText(R.string.PreambleFootnote)
                }
            }
        }
    }

//    override fun onPause() {
//        super.onPause()
//
//        // Cancel the coroutine job to prevent it from running in the background
//        adLoadingJob?.cancel()
//
//        // Remove AdView from the view hierarchy
//        Activity_Preamble_BannerAd?.removeAllViews()
//    }

//    override fun onResume() {
//        super.onResume()
//
////        lifecycleScope.launch(Dispatchers.IO) {
//////            MobileAds.initialize(this@Activity_Preamble) {}
////            val Activity_Preamble_BannerAdRequest = AdRequest.Builder().build()
////
////            Activity_Preamble_BannerAd = findViewById(R.id.activity_preamble_adView)
////            withContext(Dispatchers.Main) {
////                Activity_Preamble_BannerAd.loadAd(Activity_Preamble_BannerAdRequest)
////            }
////        }
//    }


    override fun onDestroy() {
//        super.onDestroy()
//        Activity_Preamble_BannerAd.removeAllViews()
////        Activity_Preamble_BannerAd.isActivated = false
//        Activity_Preamble_BannerAd.destroy()

//        if (::Activity_Preamble_BannerAd.isInitialized) {
//            // Check if Activity_Preamble_BannerAd is initialized before performing operations on it
//            Activity_Preamble_BannerAd.removeAllViews()
//            Activity_Preamble_BannerAd.destroy()
////            Activity_Preamble_BannerAd = AdView(this) // Reinitialize the AdView
//            Activity_Preamble_BannerAd = null // Set to null
//        }

//        if (!isFinishing()) {
//            // Activity is not finishing, perform cleanup
//            Activity_Preamble_BannerAd?.removeAllViews()
//            Activity_Preamble_BannerAd?.destroy()
//            Activity_Preamble_BannerAd = null
//        }

        // Remove AdView from its parent view before destroying

        Activity_Preamble_BannerAd.removeAllViews()
        Activity_Preamble_BannerAd.destroy()
//        Activity_Preamble_BannerAd = null

        super.onDestroy()
    }
}