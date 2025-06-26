package com.example.noteslip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * 内容适配器
 * 用于处理灵感社区页面中的瀑布流内容展示
 */
class ContentAdapter(
    private var contentList: List<ContentItem>,
    private val onItemClick: (ContentItem) -> Unit = {},
    private val onLikeClick: (ContentItem) -> Unit = {},
    private val onModifyClick: (ContentItem) -> Unit = {}
) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val ivAvatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        val ivLike: ImageView = itemView.findViewById(R.id.iv_like)
        val tvLikeCount: TextView = itemView.findViewById(R.id.tv_like_count)
        val layoutLike: LinearLayout = itemView.findViewById(R.id.layout_like)
        val btnCopyStyle: TextView = itemView.findViewById(R.id.btn_copy_style)
        val tvUsageCount: TextView = itemView.findViewById(R.id.tv_usage_count)
        val layoutAiTag: LinearLayout = itemView.findViewById(R.id.layout_ai_tag)
        val layoutLiveTag: LinearLayout = itemView.findViewById(R.id.layout_live_tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_content_card, parent, false)
        return ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = contentList[position]
        
        // 绑定基本信息
        holder.tvTitle.text = item.title
        holder.tvUsername.text = item.author.name
        holder.tvLikeCount.text = item.likeCount.toString()
        
        // 设置点赞状态
        if (item.isLiked) {
            holder.ivLike.setImageResource(android.R.drawable.btn_star)
            holder.ivLike.setColorFilter(android.graphics.Color.RED)
        } else {
            holder.ivLike.setImageResource(android.R.drawable.btn_star)
            holder.ivLike.setColorFilter(android.graphics.Color.GRAY)
        }
        
        // 根据内容类型显示不同的标签
        when (item.type) {
            ContentType.TEMPLATE -> {
                holder.btnCopyStyle.visibility = View.VISIBLE
                holder.tvUsageCount.visibility = if (item.usageCount != null) View.VISIBLE else View.GONE
                holder.tvUsageCount.text = item.usageCount ?: ""
            }
            ContentType.LIVE -> {
                holder.layoutLiveTag.visibility = View.VISIBLE
                holder.btnCopyStyle.visibility = View.GONE
            }
            ContentType.AI_GENERATED -> {
                holder.layoutAiTag.visibility = View.VISIBLE
                holder.btnCopyStyle.visibility = View.GONE
            }
            else -> {
                holder.btnCopyStyle.visibility = View.GONE
                holder.tvUsageCount.visibility = View.GONE
                holder.layoutAiTag.visibility = View.GONE
                holder.layoutLiveTag.visibility = View.GONE
            }
        }
        
        // 如果内容有修同款功能，显示修同款按钮
        if (item.hasModifyButton) {
            holder.btnCopyStyle.visibility = View.VISIBLE
        }
        
        // 设置点击事件
        holder.itemView.setOnClickListener { onItemClick(item) }
        holder.layoutLike.setOnClickListener { onLikeClick(item) }
        holder.btnCopyStyle.setOnClickListener { onModifyClick(item) }
        
        // 模拟图片加载 - 在实际应用中应该使用图片加载库如Glide
        if (item.imageUrl != null) {
            // 这里应该使用图片加载库加载图片
            // Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.ivImage)
        }
        
        // 设置模拟头像
        if (item.author.avatarUrl != null) {
            // 这里应该使用图片加载库加载头像
            // Glide.with(holder.itemView.context).load(item.author.avatarUrl).into(holder.ivAvatar)
        }
    }

    override fun getItemCount(): Int = contentList.size
    
    /**
     * 更新数据
     */
    fun updateData(newContentList: List<ContentItem>) {
        contentList = newContentList
        notifyDataSetChanged()
    }
    
    /**
     * 添加更多数据
     */
    fun addData(newItems: List<ContentItem>) {
        val oldSize = contentList.size
        contentList = contentList + newItems
        notifyItemRangeInserted(oldSize, newItems.size)
    }
} 