package com.example.constitutionofindia

import android.content.Context

class ThemePreference {

    fun changeThemeStyle(context : Context, theme : Int){
        context.setTheme(theme)
    }
}
