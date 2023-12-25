package com.example.constitutionofindia.parts

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R

class Adapter_Partslist (
    val partsList : List<Element_Partslist>,
    val listener : PartsListInterface
) : RecyclerView.Adapter<Adapter_Partslist.PartListItemHolder>() {

    inner class PartListItemHolder(itemcard : View) : RecyclerView.ViewHolder(itemcard), View.OnClickListener{

        val partNum : TextView = itemcard.findViewById(R.id.activity_partslist_element_cvtvPartNum)
        val partName : TextView = itemcard.findViewById(R.id.activity_partslist_element_cvtvPartName)
        val partRange : TextView = itemcard.findViewById(R.id.activity_partslist_element_cvtvArticleRange)

        init {
            itemcard.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.PartOnClick(position)
            }
        }

        fun bindPart(num : String, name : String, range: String){
            partNum.setText(Html.fromHtml(num, Html.FROM_HTML_MODE_LEGACY))
            partName.setText(Html.fromHtml(name, Html.FROM_HTML_MODE_LEGACY))
            partRange.setText(range)
//            partRange.setText(Html.fromHtml(range, Html.FROM_HTML_MODE_LEGACY))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartListItemHolder {
        val recyclerViewitem = LayoutInflater.from(parent.context).inflate(R.layout.activity_partslist_elementlayout,parent, false)
        return PartListItemHolder(recyclerViewitem)
    }

    override fun onBindViewHolder(holder: PartListItemHolder, position: Int) {
        holder.bindPart(partsList[position].PartNum, partsList[position].PartName, partsList[position].PartRange)
    }

//    override fun onViewDetachedFromWindow(holder: PartListItemHolder) {
//        super.onViewDetachedFromWindow(holder)
//        holder.itemView.setOnClickListener(null)
//    }

    override fun getItemCount(): Int {
        return partsList.size
    }

    interface PartsListInterface{
        fun PartOnClick(position: Int)
    }
}
