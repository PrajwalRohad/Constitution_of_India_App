package com.example.constitutionofindia

import org.json.JSONObject

class AssetManager {


    var preambleJSON: JSONObject
    var partsJSON: JSONObject
    var scheduleJSON: JSONObject
    var amendmentJSON: JSONObject
    var faqsJSON : JSONObject


    init {

        preambleJSON = JSONObject(
            CoIApplication.appContext.assets.open("preamble.json").bufferedReader().readText()
        )
        partsJSON = JSONObject(
            CoIApplication.appContext.assets.open("parts.json").bufferedReader().readText()
        )
        scheduleJSON = JSONObject(
            CoIApplication.appContext.assets.open("schedules.json").bufferedReader().readText()
        )
        amendmentJSON = JSONObject(
            CoIApplication.appContext.assets.open("amendments.json").bufferedReader().readText()
        )
        faqsJSON = JSONObject(
            CoIApplication.appContext.assets.open("faqs.json").bufferedReader().readText()
        )

    }


}