package com.example.onlinesales.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinesales.R

class StringAdapter(private val strings: List<String>, private val results: List<String> ) : RecyclerView.Adapter<StringAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_string, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvString.text = "${strings[position]} => ${results[position]}"
    }

    override fun getItemCount(): Int = strings.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvString: TextView = view.findViewById(R.id.tvString)
    }
}