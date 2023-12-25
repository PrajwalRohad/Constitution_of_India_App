package com.example.constitutionofindia.articles

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R

class Adapter_Articleslist(
    val articlesList : List<Element_Articleslist>,
    val listener : ArticlesListInterface
) : RecyclerView.Adapter<Adapter_Articleslist.ArticleListItemHolder>()  {

    inner class ArticleListItemHolder(itemcard : View) : RecyclerView.ViewHolder(itemcard), View.OnClickListener{

        val Num : TextView = itemcard.findViewById(R.id.activity_articleslist_element_cvtvArticleNum)
        val Name : TextView = itemcard.findViewById(R.id.activity_articleslist_element_cvtvArticleName)

        init {
            itemcard.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.ArticleOnClick(position)
            }
        }

        fun bindArticle(num : String, name : String){
            this.Num.setText(Html.fromHtml(num, Html.FROM_HTML_MODE_LEGACY))
            this.Name.setText(Html.fromHtml(name, Html.FROM_HTML_MODE_LEGACY))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListItemHolder {
        val recyclerViewItem = LayoutInflater.from(parent.context).inflate(R.layout.activity_articleslist_elementlayout, parent, false)
        return ArticleListItemHolder(recyclerViewItem)
    }

    override fun onBindViewHolder(holder: ArticleListItemHolder, position: Int) {
        holder.bindArticle(articlesList[position].ArticleNum, articlesList[position].ArticleName)
    }

    override fun onViewDetachedFromWindow(holder: ArticleListItemHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

    interface ArticlesListInterface{
        fun ArticleOnClick(position : Int)
    }

}