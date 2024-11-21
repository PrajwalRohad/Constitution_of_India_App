package com.example.constitutionofindia

import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.amendments.Activity_Amendment
import com.example.constitutionofindia.articles.Activity_Article
import com.example.constitutionofindia.data.entity.Element_Bookmark
import com.example.constitutionofindia.schedules.Activity_Schedule
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class BookmarkManager {

    fun loadFromBookmark(bookmark: Element_Bookmark, assets: AssetManager): Intent {
        val list = bookmark.data

        val intent: Intent = when (bookmark.type) {
            Element_Bookmark.TYPE_ARTICLE -> loadArticleFromBookmark(list, assets)
            Element_Bookmark.TYPE_SCHEDULE -> loadScheduleFromBookmark(list)
            Element_Bookmark.TYPE_AMENDMENT -> loadAmendmentFromBookmark(list)
            else -> Intent()
        }

        return intent

    }


    fun loadArticleFromBookmark(list: List<String>, assets: AssetManager): Intent {
        val jpartsobj = assets.partsJSON


        val partNumKey = list[2]
        val partobj = jpartsobj.getJSONObject(partNumKey)
        val partNum = partobj.getString("part_num")
        val partName = partobj.getString("part_name")

        val chapterNumKey = list[3]
        val chapterobj = partobj.getJSONObject(chapterNumKey)
        val chapterNum = chapterobj.getString("chapter_num")
        val chapterName = chapterobj.getString("chapter_name")

        val sectionsNameKey = list[4]
        val sectionobj = chapterobj.getJSONObject(sectionsNameKey)
        val sectionsName = sectionobj.getString("section_name")

        val articlesIndex = list[5].toInt()
        val articleobj = sectionobj.getJSONArray("articles").getJSONObject(articlesIndex)
        val articlesNum = articleobj.getString("num")
        val articlesName = articleobj.getString("name")
        val articlesText = articleobj.getString("text")
        val articlesFootnote = articleobj.getString("footnote")


        val intent = Intent(CoIApplication.appContext, Activity_Article::class.java).also {
            it.putExtra("partNumKey", partNumKey)
            it.putExtra("partNum", partNum)
            it.putExtra("partName", partName)

            it.putExtra("chapterNumKey", chapterNumKey)
            it.putExtra("chapterNum", chapterNum)
            it.putExtra("chapterName", chapterName)

            it.putExtra("sectionNameKey", sectionsNameKey)
            it.putExtra("sectionName", sectionsName)

            it.putExtra("articlesIndex", articlesIndex.toString())
            it.putExtra("articlesNum", articlesNum)
            it.putExtra("articlesName", articlesName)
            it.putExtra("articlesText", articlesText)
            it.putExtra("articlesFootnote", articlesFootnote)
        }

        return intent
    }


    fun loadScheduleFromBookmark(list: List<String>): Intent {
        val scheduleNumKey = list[2]

        val intent = Intent(CoIApplication.appContext, Activity_Schedule::class.java).also {
            it.putExtra("scheduleName", scheduleNumKey)
        }

        return intent
    }


    fun loadAmendmentFromBookmark(list: List<String>): Intent {
        val amendmentKey = list[2]

        val intent = Intent(CoIApplication.appContext, Activity_Amendment::class.java).also {
            it.putExtra("amendmentName", amendmentKey)
        }

        return intent
    }


    fun bookmarkBtnClick(state: Boolean, btn: FloatingActionButton) {
        btn.also { fab ->

            if (state) {
                fab.setImageResource(R.drawable.bookmark_select)

            } else {
                fab.setImageResource(R.drawable.bookmark_unselect)
            }
        }
    }

    fun bookmarkBtnClick(state: Boolean, tv: TextView, color1: Int, color2: Int) {
        tv.also {
            if (state) {
                it.setBackgroundResource(R.drawable.bookmark_btn_select)
                it.setTextColor(color2)
                it.setText(R.string.bookmarked)
            } else {
                it.setBackgroundResource(R.drawable.bookmark_btn_unselect)
                it.setTextColor(color1)
                it.setText(R.string.click_to_bookmark)
            }
        }
    }

    fun showMessage(state: Boolean, view: View, anchor: Int) {
        if(state) {
            val snackbar = Snackbar.make(
                view,
                "Bookmark added.",
                Snackbar.LENGTH_SHORT
            )
            val params = snackbar.view.layoutParams as (FrameLayout.LayoutParams)
            params.width = FrameLayout.LayoutParams.WRAP_CONTENT
            params.bottomMargin = 40
            params.gravity = Gravity.BOTTOM or Gravity.CENTER // Bottom centre
            snackbar.view.layoutParams = params
            snackbar.apply {
                setAnchorView(anchor)
                setActionTextColor(Color.WHITE)
                show()
            }
        } else {
            val snackbar = Snackbar.make(
                view,
                "Bookmark removed.",
                Snackbar.LENGTH_SHORT
            )
            val params = snackbar.view.layoutParams as (FrameLayout.LayoutParams)
            params.width = FrameLayout.LayoutParams.WRAP_CONTENT
            params.bottomMargin = 40
            params.gravity = Gravity.BOTTOM or Gravity.CENTER // Bottom centre
            snackbar.view.layoutParams = params
            snackbar.apply {
                setAnchorView(anchor)
                setActionTextColor(Color.WHITE)
                show()
            }
        }
    }

}