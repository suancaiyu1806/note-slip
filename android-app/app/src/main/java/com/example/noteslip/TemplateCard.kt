package com.example.noteslip

data class TemplateCard(
    val id: String,
    val imageUrl: String,
    val title: String,
    val authorName: String,
    val authorAvatar: String,
    val likeCount: Int,
    val isLiked: Boolean = false,
    val hasAIBadge: Boolean = false,
    val usageCount: Int? = null,
    val type: TemplateType = TemplateType.REGULAR
)

enum class TemplateType {
    REGULAR,        // 普通feed
    LIVE_FEED,      // 直播feed
    TEMPLATE_FEED,  // 修同款feed
    LIKED_FEED      // 点赞feed
} 