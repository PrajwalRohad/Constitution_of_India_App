package com.example.constitutionofindia

import android.content.Context
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
            MobileAds.initialize(context) {}
        }
    }

    fun loadBannerAd(view : AdView) {
        val adRequest = AdRequest.Builder().build()

        view.loadAd(adRequest)
    }

}