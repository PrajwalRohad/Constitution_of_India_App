package com.example.constitutionofindia

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
//import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.amendments.Activity_Amendmentslist
import com.example.constitutionofindia.bookmarks.Activity_Bookmarks
import com.example.constitutionofindia.faqs.Activity_FAQs
import com.example.constitutionofindia.parts.Activity_Partslist
import com.example.constitutionofindia.preamble.Activity_Preamble
import com.example.constitutionofindia.schedules.Activity_Scheduleslist
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.install.model.AppUpdateType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
//import java.io.Closeable

class Activity_Main : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    private lateinit var Activity_Main_BannerAd: AdView

    lateinit var toggle: ActionBarDrawerToggle

    val THEME_PREF = "theme_pref"
    val DATA_PREF = "data_pref"
    val DATA_DELETED = "data_deleted"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"
    private val FONT_SIZE = "font_size"

    lateinit var CoI_SharedPref: SharedPreferences
//    lateinit var CoI_SharedPref: Deferred<SharedPreferences>
    lateinit var Data_SharedPref: SharedPreferences

    private val viewModel: SplashViewModel by viewModels()

    private lateinit var AppUpdate : AppUpdate

    override fun onCreate(savedInstanceState: Bundle?) {

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        Data_SharedPref = getSharedPreferences(DATA_PREF, MODE_PRIVATE)

        if(!Data_SharedPref.getBoolean(DATA_DELETED, false)) {
//            UserLocalData().clearData(this@Activity_Main)
//            UserLocalData().clearData

            CoI_SharedPref.edit().clear().apply()
            Data_SharedPref.edit().putBoolean(DATA_DELETED, true).apply()
            recreate()
        }
//        UserLocalData().clearData(this@Activity_Main)

        val splashscreen = installSplashScreen()

//        lifecycleScope.launch(Dispatchers.IO){
//            CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
//            val nightmode =
//                CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//            AppCompatDelegate.setDefaultNightMode(nightmode)
//            val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeDefault)
//
//            withContext(Dispatchers.Main){
//                ThemePreference().changeThemeStyle(this@Activity_Main, themeselected)
//            }
//        }

        runBlocking{
//            CoI_SharedPref = async { getSharedPreferences(THEME_PREF, MODE_PRIVATE) }
            launch {
                val nightmode =
                    CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                val nightmode =
//                    CoI_SharedPref.await().getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                AppCompatDelegate.setDefaultNightMode(nightmode)
            }
            launch {
                val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
//                val themeselected = CoI_SharedPref.await().getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
                setTheme(themeselected)
//                ThemePreference().changeThemeStyle(this@Activity_Main, themeselected)
            }

//            CoI_SharedPref.cancel(null)
        }

//        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
//        val nightmode =
//            CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//        AppCompatDelegate.setDefaultNightMode(nightmode)
//        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
//        setTheme(themeselected)
//        ThemePreference().changeThemeStyle(this, themeselected)

        splashscreen.apply {
            this.setKeepOnScreenCondition {
                !viewModel.isReady.value
            }

            this.setOnExitAnimationListener { screen ->

                try {
                    val zoomX = ObjectAnimator.ofFloat(
                        screen.iconView,
                        View.SCALE_X,
                        1.0f,
                        0.0f
                    )
                    zoomX.also {
                        it.interpolator = OvershootInterpolator()
                        it.duration = 500L
                        it.doOnEnd {
                            screen.remove()
                        }
                    }

                    val zoomY = ObjectAnimator.ofFloat(
                        screen.iconView,
                        View.SCALE_Y,
                        1.0f,
                        0.0f
                    )
                    zoomY.also {
                        it.interpolator = OvershootInterpolator()
                        it.duration = 500L
                        it.doOnEnd {
                            screen.remove()
                        }
                    }

                    zoomX.start()
                    zoomY.start()
                }catch (e: Exception) {
                    Log.e("SplashScreen", "Exception accessing icon view properties", e)
                }


            }

        }
        viewModel.viewModelScope.cancel(null)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        toggle = ActionBarDrawerToggle(
            this@Activity_Main,
            findViewById(R.id.activity_main_drawer),
            findViewById(R.id.activity_main_tb),
            R.string.menu,
            R.string.menu_close
        )
        findViewById<DrawerLayout>(R.id.activity_main_drawer).addDrawerListener(toggle)
        toggle.syncState()

        setSupportActionBar(findViewById(R.id.activity_main_tb))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        AppUpdate = AppUpdate(this)




    }

    override fun onStart() {
        super.onStart()





        findViewById<DrawerLayout>(R.id.activity_main_drawer).also { drawer ->
            drawer.closeDrawer(GravityCompat.START)
        }


        val drawerheader =
            findViewById<NavigationView>(R.id.activity_main_drawer_navView).getHeaderView(0)
        drawerheader.also { header ->
            header.findViewById<ImageView>(R.id.activity_main_drawer_header_ivmain).also {
                it.setImageResource(R.drawable.app_vista_icon_transparent_1080dp)

            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            findViewById<CardView>(R.id.activity_main_cvPreamble).setOnClickListener(this@Activity_Main)
            findViewById<CardView>(R.id.activity_main_cvParts).setOnClickListener(this@Activity_Main)
            findViewById<CardView>(R.id.activity_main_cvSchedules).setOnClickListener(this@Activity_Main)
            findViewById<CardView>(R.id.activity_main_cvAmendments).setOnClickListener(this@Activity_Main)

            findViewById<NavigationView>(R.id.activity_main_drawer_navView).setNavigationItemSelectedListener(this@Activity_Main)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val sharedpref = newBase.getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        var fontsize1 = 1.0f
        if (sharedpref != null) {
            fontsize1 = 0.5f + (0.25f * sharedpref.getInt(FONT_SIZE, 1))
        }

        super.attachBaseContext(ThemePreference().adjustFontScale(newBase, fontsize1))
    }

    override fun onResume() {
        super.onResume()
        AppUpdate.onResume()

        lifecycleScope.launch(Dispatchers.IO){

            Activity_Main_BannerAd = findViewById(R.id.activity_main_adView)

            withContext(Dispatchers.Main){
                AdManager().loadBannerAd(Activity_Main_BannerAd)
            }

        }

    }
//
//
//    override fun onStop() {
//        super.onStop()
//
////        CoI_SharedPref.cancel(null)
//
//
//
//    }

    override fun onDestroy() {
        super.onDestroy()

        AppUpdate.onDestroy()

        Activity_Main_BannerAd.removeAllViews()
        Activity_Main_BannerAd.destroy()

        findViewById<DrawerLayout>(R.id.activity_main_drawer).removeDrawerListener(toggle)


        findViewById<CardView>(R.id.activity_main_cvPreamble).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }
        findViewById<CardView>(R.id.activity_main_cvParts).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }

        findViewById<CardView>(R.id.activity_main_cvSchedules).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }
        findViewById<CardView>(R.id.activity_main_cvAmendments).also {
            it.setOnClickListener(null)
            it.removeAllViews()
        }

        findViewById<NavigationView>(R.id.activity_main_drawer_navView).also {
            it.setNavigationItemSelectedListener(null)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }



    override fun onClick(view: View?) {

        when (view?.id) {

            R.id.activity_main_cvPreamble -> {
                Intent(this, Activity_Preamble::class.java).also {
                    startActivity(it)
                }
            }

            R.id.activity_main_cvParts -> {
                Intent(this, Activity_Partslist::class.java).also {
                    startActivity(it)
                }
            }

            R.id.activity_main_cvSchedules -> {
                Intent(this, Activity_Scheduleslist::class.java).also {
                    startActivity(it)
                }
            }

            R.id.activity_main_cvAmendments -> {
                Intent(this, Activity_Amendmentslist::class.java).also {
                    startActivity(it)
                }
            }


        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_menu_Bookmarks -> {
                Intent(this, Activity_Bookmarks::class.java).also {
                    startActivity(it)
                }
            }

            R.id.main_menu_Settings -> {
                Intent(this, Activity_Settings::class.java).also {
                    startActivity(it)
                }
//                onDestroy()
                finish()
//                Thread.sleep(1000)
//                onBackPressedDispatcher.onBackPressed()
            }

            R.id.main_menu_FAQs -> {
                Intent(this, Activity_FAQs::class.java).also {
                    startActivity(it)
                }
            }

            R.id.main_menu_About -> {
                Intent(this, Activity_About::class.java).also {
                    startActivity(it)
                }
            }

            R.id.main_menu_Share -> {
//                    Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show()
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.IndiaCanon.constitutionofindia")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            R.id.main_menu_rate -> {
//                    Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show()
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.IndiaCanon.constitutionofindia")
                ).also { rateintent ->
                    startActivity(rateintent)
                }


            }
        }
        return true
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppUpdate.onActivityResult(requestCode,resultCode,data)
    }

}
