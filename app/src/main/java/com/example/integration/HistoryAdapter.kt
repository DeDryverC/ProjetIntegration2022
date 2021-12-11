package com.example.integration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val historyList : ArrayList<History>) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_history,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val history = historyList[position]

        holder.action.text = history.action
        holder.date.text = history.date
        holder.points.text = history.points.toString()

    }

    override fun getItemCount(): Int {
        return historyList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val action : TextView = itemView.findViewById(R.id.tvhistory_action)
        val date : TextView = itemView.findViewById(R.id.tvhistory_date)
        val points : TextView = itemView.findViewById(R.id.tvhistory_points)

    }

}