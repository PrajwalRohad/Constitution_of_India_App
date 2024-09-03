package com.example.constitutionofindia.faqs

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.AdManager
import com.example.constitutionofindia.CoIApplication
import com.example.constitutionofindia.ThemePreference
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class Activity_FAQs : AppCompatActivity(), View.OnClickListener
//    Adapter_FAQsList.FAQsListInterface
{

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    lateinit var CoI_SharedPref : SharedPreferences

    lateinit var keysFAQs : JSONArray

    lateinit var Activity_FAQs_BannerAd : FrameLayout
    lateinit var adManager_instance : AdManager
    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_faqs)



        setSupportActionBar(findViewById(R.id.activity_faqs_tb))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)



//        lifecycleScope.launch(Dispatchers.Default){
//            val jfaqsObject = CoIApplication.assetManager.faqsJSON
//            keysFAQs = jfaqsObject.names()
//
//            val FAQsList = mutableListOf<faqQnA>()
//
//            for(i in 0..keysFAQs.length()-1){
//                val question = jfaqsObject.getJSONObject(keysFAQs[i].toString()).getString("question")
//                val answer = jfaqsObject.getJSONObject(keysFAQs[i].toString()).getString("answer")
//
//                FAQsList.add(faqQnA(question,answer))
//            }
//
//            withContext(Dispatchers.Main){
//                val FAQsListAdapter = Adapter_FAQsList(FAQsList)
//
//                findViewById<RecyclerView>(R.id.activity_faqs_rvFAQsList).also {
//
//                    it.adapter = FAQsListAdapter
//                    it.layoutManager = LinearLayoutManager(this@Activity_FAQs)
//                }
//            }
//
//        }


        findViewById<Button>(R.id.ad_button_banner).setOnClickListener(this@Activity_FAQs)
        findViewById<Button>(R.id.ad_button_interstitial).setOnClickListener(this@Activity_FAQs)

        Activity_FAQs_BannerAd = findViewById(R.id.activity_faqs_adViewContainer)

        adManager_instance = AdManager()

//        loadInterstitial()

    }

    override fun attachBaseContext(newBase: Context) {
        val sharedpref = newBase.getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        var fontsize1 = 1.0f
        if (sharedpref != null) {
            fontsize1 = 0.5f + (0.25f * sharedpref.getInt(FONT_SIZE, 1))
        }

        super.attachBaseContext(ThemePreference().adjustFontScale(newBase, fontsize1))
    }

//    override fun onDestroy() {
//        super.onDestroy()
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {

        when(v?.id) {

            R.id.ad_button_banner -> {
                adManager_instance.loadBannerAd(Activity_FAQs_BannerAd, this)
            }

            R.id.ad_button_interstitial -> {
//                if (mInterstitialAd != null) {
//                    mInterstitialAd?.show(this)
//                    loadInterstitial()
//                } else {
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
//                }
                adManager_instance.loadBannerAdWithRDP()

            }

        }

    }


    fun loadInterstitial() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("AD_MANAGER_TAG", adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("AD_MANAGER_TAG", "Interstitial Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }

//    override fun FAQsOnClick(position: Int) {
//        Log.d("faqslog","clicked - "+position)
//        findViewById<TextView>(R.id.activity_faqs_element_cvtvAnswer).also {
//            if(it.visibility == View.GONE){
//                it.visibility = View.VISIBLE
//            }else{
//                it.visibility = View.GONE
//            }
//        }
//    }
}