package com.example.constitutionofindia.amendments

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R

class Adapter_Amendmentslist(
    val amendmentsList : List<Element_Amendmentslist>,
    val listener: AmendmentListInterface
) : RecyclerView.Adapter<Adapter_Amendmentslist.AmendmentListItemHolder>() {

    inner class AmendmentListItemHolder(itemcard : View) : RecyclerView.ViewHolder(itemcard), View.OnClickListener{

        val Name : TextView = itemcard.findViewById(R.id.activity_amendmentslist_element_cvtvAmendmentName)
        val Year : TextView = itemcard.findViewById(R.id.activity_amendmentslist_element_cvtvAmendmentYear)

        init {
            itemcard.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.AmendmentOnClick(position)
            }
        }

        fun bindAmendment(name : String, year : String){

//            this.Name.setText(name)
//            this.Year.setText(year)
            this.Name.setText(Html.fromHtml(name, Html.FROM_HTML_MODE_LEGACY))
            this.Year.setText(Html.fromHtml(year, Html.FROM_HTML_MODE_LEGACY))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmendmentListItemHolder {
        val recyclerItemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_amendmentslists_elementlayout, parent, false)
        return AmendmentListItemHolder(recyclerItemView)
    }

    override fun onBindViewHolder(holder: AmendmentListItemHolder, position: Int) {
        holder.bindAmendment(amendmentsList[position].AmendmentName, amendmentsList[position].AmendmentYear)
    }

//    override fun onViewDetachedFromWindow(holder: AmendmentListItemHolder) {
//        super.onViewDetachedFromWindow(holder)
//        holder.itemView.setOnClickListener(null)
//    }

    override fun getItemCount(): Int {
        return amendmentsList.size
    }

    interface AmendmentListInterface{
        fun AmendmentOnClick(position: Int)
    }

}