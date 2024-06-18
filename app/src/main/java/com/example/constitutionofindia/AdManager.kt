package com.example.constitutionofindia

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class AdManager {

    fun loadBannerAd(view : AdView) {
        val adRequest = AdRequest.Builder().build()

        view.loadAd(adRequest)
    }
}