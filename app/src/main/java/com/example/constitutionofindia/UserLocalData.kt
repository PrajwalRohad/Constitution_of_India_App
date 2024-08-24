package com.example.constitutionofindia

import android.app.ActivityManager
import android.content.Context
import android.os.Build

class UserLocalData {

    fun clearData(context: Context) {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData() // note: it has a return value!
            } else {
//                val packageName = context.packageName
//                val runtime = Runtime.getRuntime()
//                runtime.exec("pm clear $packageName")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}