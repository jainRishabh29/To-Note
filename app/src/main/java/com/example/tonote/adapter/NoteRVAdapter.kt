package com.example.tonote.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tonote.R
import com.example.tonote.database.Notes


class NoteRVAdapter(private val context: Context, private val listener: INoteRVAdapter) :
    RecyclerView.Adapter<NoteRVAdapter.NoteViewHolder>() {

    private var notes: ArrayList<Notes> = ArrayList()


    //  , val sources : List<Source>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder =
            NoteViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)
            )
        viewHolder.deleteButton.setOnClickListener {
            listener.onDeleteItemClicked(notes[viewHolder.adapterPosition])
        }
        viewHolder.noteItem.setOnClickListener {
            listener.onItemClicked(notes[viewHolder.adapterPosition])
        }
        return viewHolder

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteTitle.text = note.title
        holder.noteDesc.text = note.desc
        holder.cardView.setCardBackgroundColor(Color.parseColor(note.colorOfNote))
    }

    override fun getItemCount(): Int {
        return notes.size
    }


    inner class NoteViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.title)
        val noteDesc: TextView = itemView.findViewById(R.id.noteDesc)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteBtn)
        val noteItem: LinearLayout = itemView.findViewById(R.id.noteItem)
        val cardView: CardView = itemView.findViewById(R.id.cardView)

    }

    fun setNotes(notes: ArrayList<Notes>) {
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }

    interface INoteRVAdapter {
        fun onDeleteItemClicked(notes : Notes)
        fun onItemClicked(notes : Notes)
    }
}
