package com.example.noteslip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(private val notes: MutableList<Note>, private val onItemClick: (Note) -> Unit, private val onItemLongClick: (Note) -> Unit) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentText: TextView = view.findViewById(R.id.contentText)
        val timeText: TextView = view.findViewById(R.id.timeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        
        // 设置内容
        holder.contentText.text = note.content
        
        // 设置时间
        holder.timeText.text = dateFormat.format(Date(note.createTime))
        
        // 设置点击事件
        holder.itemView.setOnClickListener {
            onItemClick(note)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(note)
            true
        }
    }

    override fun getItemCount() = notes.size
}