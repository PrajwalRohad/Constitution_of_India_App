package com.example.constitutionofindia.faqs

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R


data class faqQnA(
    val question : String,
    val answer : String
)

class Adapter_FAQsList(
    val listFAQ : List<faqQnA>
) : RecyclerView.Adapter<Adapter_FAQsList.FAQsListItemHolder>() {

    inner class FAQsListItemHolder(item : View): RecyclerView.ViewHolder(item), View.OnClickListener{
        val Question = item.findViewById<TextView>(R.id.activity_faqs_element_cvtvQuestion)
        val Answer = item.findViewById<TextView>(R.id.activity_faqs_element_cvtvAnswer)

        init {
            item.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                if(Answer.visibility == View.GONE){
                    Answer.visibility = View.VISIBLE
                }else{
                    Answer.visibility = View.GONE
                }
            }
        }


        fun bindFAQ(que : String, ans : String){
            Question.setText(Html.fromHtml(que, Html.FROM_HTML_MODE_LEGACY))
            Answer.setText(Html.fromHtml(ans, Html.FROM_HTML_MODE_LEGACY))

            Answer.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQsListItemHolder {
        val recyclerViewItem = LayoutInflater.from(parent.context).inflate(R.layout.activity_faqs_element, parent, false)
        return FAQsListItemHolder(recyclerViewItem)
    }

    override fun onBindViewHolder(holder: FAQsListItemHolder, position: Int) {
        holder.also {
            it.bindFAQ(listFAQ[position].question, listFAQ[position].answer)
        }
    }

    override fun getItemCount(): Int {
        return listFAQ.size
    }

}