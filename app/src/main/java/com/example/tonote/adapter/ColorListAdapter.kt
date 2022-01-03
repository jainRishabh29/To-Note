package com.example.tonote.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tonote.R
import com.example.tonote.database.Notes

class ColorListAdapter(private val context: Context, private val listener: IColorListAdapter, var isSelected: Int) :
    RecyclerView.Adapter<ColorListAdapter.ColorListViewHolder>()  {

    private val colorList = listOf("#D5D7FF", "#cabbe9", "#6c5fa7", "#e77c7c", "#293462" , "#1a2639")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorListViewHolder {
        val viewHolder =
            ColorListViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.color_chooser_item_view, parent, false)
            )
        viewHolder.color.setOnClickListener {
            listener.onItemClicked(colorList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ColorListViewHolder, position: Int) {
        val color = colorList[position]
        holder.color.setBackgroundColor(Color.parseColor(color))
        holder.relativeLayout.setOnClickListener {
            isSelected = if(isSelected==position)
                -1
            else
                position

            notifyDataSetChanged()
        }

        if(isSelected==position){
            holder.checkIcon.visibility = VISIBLE
        }else{
            holder.checkIcon.visibility = GONE
        }
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    inner class ColorListViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val color: ImageView = itemView.findViewById(R.id.color)
        val checkIcon : ImageView = itemView.findViewById(R.id.isSelected)
        var relativeLayout : RelativeLayout = itemView.findViewById(R.id.relativeLayout)

    }

    interface IColorListAdapter {
        fun onItemClicked(colorName : String)
    }
}