package com.example.constitutionofindia.faqs

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.ThemePreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class Activity_FAQs : AppCompatActivity()
//    Adapter_FAQsList.FAQsListInterface
{

    val THEME_PREF = "theme_pref"
    val THEME_SELECTED = "theme_selected"
    val NIGHT_MODE = "night_mode"

    lateinit var CoI_SharedPref : SharedPreferences

    lateinit var keysFAQs : JSONArray


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoI_SharedPref = getSharedPreferences(THEME_PREF, MODE_PRIVATE)
        val themeselected = CoI_SharedPref.getInt(THEME_SELECTED, R.style.ThemeReplyBlue)
        val nightmode = CoI_SharedPref.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightmode)
        ThemePreference().changeThemeStyle(this, themeselected)

        setContentView(R.layout.activity_faqs)



        setSupportActionBar(findViewById(R.id.activity_faqs_tb))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)



        lifecycleScope.launch(Dispatchers.Default){
            val jfaqsfile = applicationContext.assets.open("faqs.json").bufferedReader().use {
                it.readText()
            }
            val jfaqsObject = JSONObject(jfaqsfile)
            keysFAQs = jfaqsObject.names()

            val FAQsList = mutableListOf<faqQnA>()

            for(i in 0..keysFAQs.length()-1){
                val question = jfaqsObject.getJSONObject(keysFAQs[i].toString()).getString("question")
                val answer = jfaqsObject.getJSONObject(keysFAQs[i].toString()).getString("answer")

                FAQsList.add(faqQnA(question,answer))
            }

            withContext(Dispatchers.Main){
                val FAQsListAdapter = Adapter_FAQsList(FAQsList)

                findViewById<RecyclerView>(R.id.activity_faqs_rvFAQsList).also {

                    it.adapter = FAQsListAdapter
                    it.layoutManager = LinearLayoutManager(this@Activity_FAQs)
                }
            }

        }



    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
//            Intent(this, Activity_Main::class.java).also {
//                startActivity(it)
//            }
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
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