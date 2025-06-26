package com.example.noteslip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VipPackageAdapter(
    private val packages: List<VipPackage>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<VipPackageAdapter.ViewHolder>() {

    private var selectedPosition = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: View = view.findViewById(R.id.container)
        val title: TextView = view.findViewById(R.id.tv_title)
        val price: TextView = view.findViewById(R.id.tv_price)
        val originalPrice: TextView = view.findViewById(R.id.tv_original_price)
        val monthlyPrice: TextView = view.findViewById(R.id.tv_monthly_price)
        val badge: TextView = view.findViewById(R.id.tv_badge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vip_package, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = packages[position]
        
        holder.title.text = item.title
        holder.price.text = item.price
        
        // 原价设置
        if (item.originalPrice.isNotEmpty()) {
            holder.originalPrice.text = item.originalPrice
            holder.originalPrice.visibility = View.VISIBLE
        } else {
            holder.originalPrice.visibility = View.GONE
        }
        
        // 月均价格设置
        if (item.monthlyPrice.isNotEmpty()) {
            holder.monthlyPrice.text = item.monthlyPrice
            holder.monthlyPrice.visibility = View.VISIBLE
        } else {
            holder.monthlyPrice.visibility = View.GONE
        }
        
        // 徽章设置
        if (item.badge.isNotEmpty()) {
            holder.badge.text = item.badge
            holder.badge.visibility = View.VISIBLE
        } else {
            holder.badge.visibility = View.GONE
        }
        
        // 选中状态
        val isSelected = position == selectedPosition
        holder.container.isSelected = isSelected
        
        if (isSelected) {
            holder.container.setBackgroundResource(R.drawable.bg_vip_package_selected)
        } else {
            holder.container.setBackgroundResource(R.drawable.bg_vip_package_normal)
        }
        
        holder.itemView.setOnClickListener {
            val oldPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)
            onItemClick(position)
        }
    }

    override fun getItemCount() = packages.size
    
    fun getSelectedPackage(): VipPackage? {
        return if (selectedPosition < packages.size) packages[selectedPosition] else null
    }
} 