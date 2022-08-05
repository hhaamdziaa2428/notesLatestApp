package com.example.navigationdrawer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.navigationdrawer.R

class AdapterNotes : RecyclerView.Adapter<AdapterNotes.ViewHolder>() {
    private var titles= arrayOf("Notes 1","Notes 2","Notes 3","Notes 4","Notes 5")
    private var descriptions= arrayOf("Description of Notes 1 Description of Notes 2 Description of Notes 3","Description of Notes 4 Description of Notes 5 Description of Notes 6","Description of Notes 7 Description of Notes 8 Description of Notes 9","Description of Notes 10 Description of Notes 11 Description of Notes 12","Description of Notes 13 Description of Notes 14 Description of Notes 15")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview,parent,false)
        return  ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return  titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notesTitle.text=titles[position]
        holder.notesDescription.setText(descriptions[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var notesTitle: TextView
        var notesDescription: TextView
        init{
            notesTitle=itemView.findViewById(R.id.notesName)
            notesDescription=itemView.findViewById(R.id.notesDescription)
        }
    }
}