package com.example.noteslip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToolbarAdapter(private val items: List<ToolbarItem>) : RecyclerView.Adapter<ToolbarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iv_icon)
        val title: TextView = view.findViewById(R.id.tv_title)
        val container: View = view.findViewById(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_toolbar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        
        holder.icon.setImageResource(item.iconRes)
        holder.title.text = item.title
        
        // 设置选中状态样式
        if (item.isSelected) {
            holder.title.setTextColor(holder.itemView.context.getColor(android.R.color.white))
            holder.container.alpha = 1.0f
        } else {
            holder.title.setTextColor(holder.itemView.context.getColor(android.R.color.white))
            holder.container.alpha = 0.6f
        }
    }

    override fun getItemCount() = items.size
} 