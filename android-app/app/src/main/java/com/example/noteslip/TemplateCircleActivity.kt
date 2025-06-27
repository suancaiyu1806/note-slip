package com.example.noteslip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TemplateCircleActivity : AppCompatActivity() {

    private lateinit var leftColumn: LinearLayout
    private lateinit var rightColumn: LinearLayout
    private var leftColumnHeight = 0
    private var rightColumnHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template_circle)

        initViews()
        setupClickListeners()
        loadFeedData()
    }

    private fun initViews() {
        leftColumn = findViewById(R.id.layout_left_column)
        rightColumn = findViewById(R.id.layout_right_column)
    }

    private fun setupClickListeners() {
        // 返回按钮
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // 分享按钮
        findViewById<ImageView>(R.id.btn_share).setOnClickListener {
            // 处理分享逻辑
        }

        // 精选标签
        findViewById<View>(R.id.tab_selected).setOnClickListener {
            switchToSelectedTab()
        }

        // 最新标签
        findViewById<TextView>(R.id.tab_latest).setOnClickListener {
            switchToLatestTab()
        }

        // 浮动按钮
        findViewById<FloatingActionButton>(R.id.fab_publish).setOnClickListener {
            // 处理发布逻辑
        }
    }

    private fun switchToSelectedTab() {
        // 更新标签UI状态
        findViewById<TextView>(R.id.tv_tab_selected).setTextColor(getColor(R.color.primary_text))
        findViewById<View>(R.id.underline_selected).visibility = View.VISIBLE
        findViewById<TextView>(R.id.tab_latest).setTextColor(getColor(R.color.secondary_text))
        
        // 加载精选内容
        loadSelectedContent()
    }

    private fun switchToLatestTab() {
        // 更新标签UI状态
        findViewById<TextView>(R.id.tv_tab_selected).setTextColor(getColor(R.color.secondary_text))
        findViewById<View>(R.id.underline_selected).visibility = View.GONE
        findViewById<TextView>(R.id.tab_latest).setTextColor(getColor(R.color.primary_text))
        
        // 加载最新内容
        loadLatestContent()
    }

    private fun loadFeedData() {
        // 模拟数据
        val feedItems = listOf(
            FeedItem(
                type = FeedType.LIKED,
                imageUrl = "https://example.com/image1.jpg",
                title = "超自然精致伪素颜超看！！",
                username = "Aarttion",
                likeCount = 223,
                isLiked = true
            ),
            FeedItem(
                type = FeedType.LIVE,
                imageUrl = "https://example.com/image2.jpg",
                title = "大家快来看看超自然精致...",
                username = "Aarttion",
                likeCount = 223,
                isLiked = false
            ),
            FeedItem(
                type = FeedType.TEMPLATE,
                imageUrl = "https://example.com/image3.jpg",
                title = "迪士尼人物特效+涂鸦超级好看！！",
                username = "Christian",
                likeCount = 0,
                isLiked = false
            ),
            FeedItem(
                type = FeedType.LIKED,
                imageUrl = "https://example.com/image4.jpg",
                title = "买完水果都要先尝尝，美其名曰，替你踩雷",
                username = "Aarttion",
                likeCount = 223,
                isLiked = false
            ),
            FeedItem(
                type = FeedType.LIVE,
                imageUrl = "https://example.com/image5.jpg",
                title = "即使出门在外，也不忘抓拍你的出糗瞬间📸",
                username = "Aarttion",
                likeCount = 223,
                isLiked = false
            )
        )

        feedItems.forEach { item ->
            addFeedCard(item)
        }
    }

    private fun addFeedCard(feedItem: FeedItem) {
        val cardView = LayoutInflater.from(this).inflate(R.layout.item_feed_card, null)
        
        // 设置卡片数据
        setupCardData(cardView, feedItem)
        
        // 选择较短的列添加卡片
        val targetColumn = if (leftColumnHeight <= rightColumnHeight) {
            leftColumnHeight += getCardHeight(feedItem)
            leftColumn
        } else {
            rightColumnHeight += getCardHeight(feedItem)
            rightColumn
        }
        
        targetColumn.addView(cardView)
    }

    private fun setupCardData(cardView: View, feedItem: FeedItem) {
        val imageView = cardView.findViewById<ImageView>(R.id.iv_card_image)
        val liveBadge = cardView.findViewById<ImageView>(R.id.iv_live_badge)
        val titleText = cardView.findViewById<TextView>(R.id.tv_card_title)
        val usernameText = cardView.findViewById<TextView>(R.id.tv_username)
        val heartIcon = cardView.findViewById<ImageView>(R.id.iv_heart)
        val likeCountText = cardView.findViewById<TextView>(R.id.tv_like_count)
        val templateButton = cardView.findViewById<TextView>(R.id.btn_use_template)
        val interactionLayout = cardView.findViewById<LinearLayout>(R.id.layout_interaction)

        // 设置标题和用户名
        titleText.text = feedItem.title
        usernameText.text = feedItem.username

        // 根据类型设置不同的UI状态
        when (feedItem.type) {
            FeedType.LIKED -> {
                liveBadge.visibility = View.GONE
                templateButton.visibility = View.GONE
                interactionLayout.visibility = View.VISIBLE
                heartIcon.setImageResource(if (feedItem.isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
                likeCountText.text = feedItem.likeCount.toString()
            }
            FeedType.LIVE -> {
                liveBadge.visibility = View.VISIBLE
                templateButton.visibility = View.GONE
                interactionLayout.visibility = View.VISIBLE
                heartIcon.setImageResource(R.drawable.ic_heart_outline)
                likeCountText.text = feedItem.likeCount.toString()
            }
            FeedType.TEMPLATE -> {
                liveBadge.visibility = View.GONE
                templateButton.visibility = View.VISIBLE
                interactionLayout.visibility = View.GONE
                usernameText.setTextColor(getColor(R.color.tertiary_text))
            }
        }

        // 设置点击事件
        cardView.setOnClickListener {
            // 处理卡片点击
        }

        heartIcon.setOnClickListener {
            // 处理点赞
            toggleLike(feedItem, heartIcon, likeCountText)
        }

        templateButton.setOnClickListener {
            // 处理修同款
        }
    }

    private fun toggleLike(feedItem: FeedItem, heartIcon: ImageView, likeCountText: TextView) {
        feedItem.isLiked = !feedItem.isLiked
        if (feedItem.isLiked) {
            feedItem.likeCount++
            heartIcon.setImageResource(R.drawable.ic_heart_filled)
        } else {
            feedItem.likeCount--
            heartIcon.setImageResource(R.drawable.ic_heart_outline)
        }
        likeCountText.text = feedItem.likeCount.toString()
    }

    private fun getCardHeight(feedItem: FeedItem): Int {
        // 根据内容类型估算卡片高度
        return when (feedItem.type) {
            FeedType.TEMPLATE -> 350 // 修同款卡片稍高
            else -> 320 // 标准卡片高度
        }
    }

    private fun loadSelectedContent() {
        // 清空现有内容
        leftColumn.removeAllViews()
        rightColumn.removeAllViews()
        leftColumnHeight = 0
        rightColumnHeight = 0
        
        // 重新加载精选内容
        loadFeedData()
    }

    private fun loadLatestContent() {
        // 清空现有内容
        leftColumn.removeAllViews()
        rightColumn.removeAllViews()
        leftColumnHeight = 0
        rightColumnHeight = 0
        
        // 加载最新内容（这里可以是不同的数据）
        loadFeedData()
    }
}

// 数据类
data class FeedItem(
    val type: FeedType,
    val imageUrl: String,
    val title: String,
    val username: String,
    var likeCount: Int,
    var isLiked: Boolean = false
)

enum class FeedType {
    LIKED,      // 点赞feed
    LIVE,       // LIVE feed
    TEMPLATE    // 修同款feed
} 