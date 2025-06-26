package com.example.noteslip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TemplateAdapter(
    private val templates: List<TemplateCard>,
    private val onItemClick: (TemplateCard) -> Unit,
    private val onLikeClick: (TemplateCard) -> Unit
) : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_template_card, parent, false)
        return TemplateViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        holder.bind(templates[position])
    }

    override fun getItemCount(): Int = templates.size

    inner class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val templateImage: ImageView = itemView.findViewById(R.id.templateImage)
        private val templateTitle: TextView = itemView.findViewById(R.id.templateTitle)
        private val authorName: TextView = itemView.findViewById(R.id.authorName)
        private val authorAvatar: ImageView = itemView.findViewById(R.id.authorAvatar)
        private val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        private val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        private val aiBadge: TextView = itemView.findViewById(R.id.aiBadge)
        private val usageTag: TextView = itemView.findViewById(R.id.usageTag)
        private val liveBadge: View = itemView.findViewById(R.id.liveBadge)
        private val actionButton: TextView = itemView.findViewById(R.id.actionButton)

        fun bind(template: TemplateCard) {
            templateTitle.text = template.title
            authorName.text = template.authorName
            likeCount.text = template.likeCount.toString()

            // 设置AI标签显示
            aiBadge.visibility = if (template.hasAIBadge) View.VISIBLE else View.GONE

            // 设置使用量标签
            template.usageCount?.let {
                usageTag.text = "使用量${it}"
                usageTag.visibility = View.VISIBLE
            } ?: run {
                usageTag.visibility = View.GONE
            }

            // 根据类型设置不同的显示
            when (template.type) {
                TemplateType.LIVE_FEED -> {
                    liveBadge.visibility = View.VISIBLE
                    actionButton.visibility = View.GONE
                }
                TemplateType.TEMPLATE_FEED -> {
                    liveBadge.visibility = View.GONE
                    actionButton.visibility = View.VISIBLE
                    actionButton.text = "修同款"
                }
                else -> {
                    liveBadge.visibility = View.GONE
                    actionButton.visibility = View.GONE
                }
            }

            // 设置点击事件
            itemView.setOnClickListener { onItemClick(template) }
            likeButton.setOnClickListener { onLikeClick(template) }

            // 设置点赞状态
            likeButton.setImageResource(
                if (template.isLiked) R.drawable.ic_heart_filled
                else R.drawable.ic_heart_outline
            )
        }
    }
} 