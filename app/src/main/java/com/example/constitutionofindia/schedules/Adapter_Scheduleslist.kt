package com.example.constitutionofindia.schedules

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.IndiaCanon.constitutionofindia.R

class Adapter_Scheduleslist(
    val schedulesList : List<Element_Scheduleslist>,
    val listener: SchedulesListInterface
) : RecyclerView.Adapter<Adapter_Scheduleslist.ScheduleListItemHolder>(){

    inner class ScheduleListItemHolder(itemcard : View) : RecyclerView.ViewHolder(itemcard), View.OnClickListener{

        val num : TextView = itemcard.findViewById(R.id.activity_scheduleslist_element_cvtvScheduleNum)
        val name : TextView = itemcard.findViewById(R.id.activity_scheduleslist_element_cvtvScheduleName)

        init {
            itemcard.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.ScheduleOnClick(position)
            }
        }

        fun bindSchedule(num : String, name : String){
            this.num.setText(Html.fromHtml(num, Html.FROM_HTML_MODE_LEGACY))
            this.name.setText(Html.fromHtml(name, Html.FROM_HTML_MODE_LEGACY))
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListItemHolder {
        val recyclerViewitem = LayoutInflater.from(parent.context).inflate(R.layout.activity_scheduleslist_elementlayout, parent, false)
        return ScheduleListItemHolder(recyclerViewitem)
    }

    override fun onBindViewHolder(holder: ScheduleListItemHolder, position: Int) {
        holder.bindSchedule(schedulesList[position].ScheduleNum, schedulesList[position].ScheduleName)
    }

    override fun onViewDetachedFromWindow(holder: ScheduleListItemHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return schedulesList.size
    }

    interface SchedulesListInterface{
        fun ScheduleOnClick(position: Int)
    }
}