package com.example.constitutionofindia

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AdManager {

    fun adsInitialize(context : Context) {
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(context) { initializationStatus ->
                val statusMap =
                    initializationStatus.adapterStatusMap
                for (adapterClass in statusMap.keys) {
                    val status = statusMap[adapterClass]
                    Log.d(
                        "MyAppAd", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status!!.description, status.latency
                        )
                    )
                }
                // Start loading ads here...
            }
        }

//        MobileAds.initialize(context)
    }

    fun loadBannerAd(view : AdView) {

        val adRequest = AdRequest.Builder().build()

        view.loadAd(adRequest)
        Log.d("AD_MANAGER_TAG", "The Banner ad shown.")
    }

    fun loadBannerAd(container : FrameLayout, context: Context) {

        val adView = AdView(context)
        adView.adUnitId = "ca-app-pub-9123074968180633/5652156318"
        adView.setAdSize(AdSize.BANNER)

        container.removeAllViews()
        container.addView(adView)

        val adRequest = AdRequest.Builder().build()

        adView.loadAd(adRequest)
        Log.d("AD_MANAGER_TAG", "The Banner ad shown.")
    }

    fun loadBannerAdWithRDP(view : AdView) {

        val networkExtrasBundle = Bundle()
        networkExtrasBundle.putInt("rdp", 1)

        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, networkExtrasBundle)
            .build()


        view.loadAd(adRequest)
        Log.d("AD_MANAGER_TAG", "The Banner ad with RDP shown.")
    }


    suspend fun loadInterstitialAd(context: Context) : InterstitialAd{

//        var mInterstitialAd: InterstitialAd? = null
//
//        val adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(context,"ca-app-pub-9123074968180633/7856951162", adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.d("AD_MANAGER_TAG", adError.toString())
//                mInterstitialAd = null
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                Log.d("AD_MANAGER_TAG", "Interstitial Ad was loaded.")
//                mInterstitialAd = interstitialAd
//            }
//        })

        return suspendCancellableCoroutine { continuation ->
            val adRequest = AdRequest.Builder().build()

            InterstitialAd.load(context, "ca-app-pub-9123074968180633/7856951162", adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // Resume the coroutine with the loaded ad
                    continuation.resume(interstitialAd)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Resume the coroutine with an exception or null
                    continuation.resumeWithException(Exception(loadAdError.message))
                }
            })
        }

    }

    suspend fun showInterstitialAd(context: Context, activity: Activity) {
        val mInterstitialAd = loadInterstitialAd(context)
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

}