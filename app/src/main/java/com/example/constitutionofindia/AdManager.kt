package com.example.constitutionofindia

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    }

}