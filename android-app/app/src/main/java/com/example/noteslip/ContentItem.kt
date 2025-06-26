package com.example.noteslip

/**
 * 内容项数据模型
 * 用于表示灵感社区页面中瀑布流中的各种内容卡片
 */
data class ContentItem(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val author: Author,
    val likeCount: Int = 0,
    val isLiked: Boolean = false,
    val type: ContentType = ContentType.NORMAL,
    val usageCount: String? = null,
    val isAI: Boolean = false,
    val isLive: Boolean = false,
    val hasModifyButton: Boolean = false
)

/**
 * 作者信息
 */
data class Author(
    val name: String,
    val avatarUrl: String? = null
)

/**
 * 内容类型枚举
 */
enum class ContentType {
    NORMAL,      // 普通内容
    LIVE,        // 直播内容
    TEMPLATE,    // 模板内容
    AI_GENERATED // AI生成内容
}

/**
 * 话题信息数据模型
 */
data class TopicInfo(
    val title: String,
    val description: String,
    val viewCount: String,
    val modifyCount: String
)

/**
 * 筛选标签枚举
 */
enum class FilterTab {
    FEATURED,  // 精选
    LATEST     // 最新
} 