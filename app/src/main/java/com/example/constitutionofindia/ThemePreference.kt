package com.example.constitutionofindia

import android.content.Context

class ThemePreference {

    fun changeThemeStyle(context : Context, theme : Int){
        context.setTheme(theme)
    }

    fun adjustFontScale(context: Context, scale: Float) : Context{
        val configuration = context.resources.configuration
        configuration.fontScale = scale

        return context.createConfigurationContext(configuration)
    }
}
