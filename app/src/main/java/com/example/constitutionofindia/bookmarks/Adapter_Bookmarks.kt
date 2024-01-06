package com.example.constitutionofindia.bookmarks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R
import com.example.constitutionofindia.bookmarks.bookmarksViewModel.BookmarkViewModel
import com.example.constitutionofindia.data.entity.Element_Bookmark

class Adapter_Bookmarks (
    var bookmarkslist : MutableList<Element_Bookmark>,
    val listener : BookmarkInterface,
    val viewModel: BookmarkViewModel
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ArticleBookmarkViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val Num : TextView = itemView.findViewById(R.id.activity_articleslist_element_cvtvArticleNum)
        val Name : TextView = itemView.findViewById(R.id.activity_articleslist_element_cvtvArticleName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.BookmarkOnClick(position, bookmarkslist[position])
            }
        }

        fun bind(bookmark: Element_Bookmark){
            this.Num.setText(bookmark.data[0])
            this.Name.setText(bookmark.data[1])
        }
    }

    inner class ScheduleBookmarkViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val num : TextView = itemView.findViewById(R.id.activity_scheduleslist_element_cvtvScheduleNum)
        val name : TextView = itemView.findViewById(R.id.activity_scheduleslist_element_cvtvScheduleName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.BookmarkOnClick(position, bookmarkslist[position])
            }
        }

        fun bind(bookmark: Element_Bookmark){
            this.num.setText(bookmark.data[0])
            this.name.setText(bookmark.data[1])
        }
    }

    inner class AmendmentBookmarkViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val Name : TextView = itemView.findViewById(R.id.activity_amendmentslist_element_cvtvAmendmentName)
        val Year : TextView = itemView.findViewById(R.id.activity_amendmentslist_element_cvtvAmendmentYear)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.BookmarkOnClick(position, bookmarkslist[position])
            }
        }

        fun bind(bookmark: Element_Bookmark){
            this.Name.setText(bookmark.data[0])
            this.Year.setText(bookmark.data[1])
        }
    }


    override fun getItemViewType(position: Int): Int {
        return bookmarkslist[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            Element_Bookmark.TYPE_ARTICLE -> {
                ArticleBookmarkViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.activity_articleslist_elementlayout, parent, false)
                )
            }

            Element_Bookmark.TYPE_SCHEDULE -> {
                ScheduleBookmarkViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.activity_scheduleslist_elementlayout, parent, false)
                )
            }

            Element_Bookmark.TYPE_AMENDMENT -> {
                AmendmentBookmarkViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.activity_amendmentslists_elementlayout, parent, false)
                )
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return bookmarkslist.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookmark = bookmarkslist[position]
        when(bookmark.type) {

            Element_Bookmark.TYPE_ARTICLE -> {
                (holder as ArticleBookmarkViewHolder).bind(bookmark)
            }
            Element_Bookmark.TYPE_SCHEDULE -> {
                (holder as ScheduleBookmarkViewHolder).bind(bookmark)
            }
            Element_Bookmark.TYPE_AMENDMENT -> {
                (holder as AmendmentBookmarkViewHolder).bind(bookmark)
            }

        }
    }

//    fun updateList(newlist : List<Element_Bookmark>) {
//
//    }

    fun removeAt(index: Int) {
        val item = bookmarkslist.get(index)
        viewModel.deleteBookmark(item.name)
        bookmarkslist.removeAt(index)   // items is a MutableList
        notifyItemRemoved(index)
    }

    interface BookmarkInterface{
        fun BookmarkOnClick(position: Int, bookmark: Element_Bookmark)
    }


}