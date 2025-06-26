package com.example.noteslip

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * 灵感社区主页面Activity
 * 实现设计稿中的上中下垂直布局结构和各组件功能
 */
class InspirationCommunityActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "InspirationCommunity"
    }

    // UI组件
    private lateinit var btnBack: View
    private lateinit var btnShare: View
    private lateinit var tvTopicTitle: TextView
    private lateinit var tvStats: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tabFeatured: LinearLayout
    private lateinit var tabLatest: LinearLayout
    private lateinit var tvFeatured: TextView
    private lateinit var tvLatest: TextView
    private lateinit var indicatorFeatured: View
    private lateinit var indicatorLatest: View
    private lateinit var rvContentFeed: RecyclerView
    private lateinit var fabPublish: FloatingActionButton
    private lateinit var layoutLoadMore: LinearLayout

    // 数据和适配器
    private lateinit var contentAdapter: ContentAdapter
    private var currentTab = FilterTab.FEATURED
    private var contentList = mutableListOf<ContentItem>()

    // 话题信息
    private val topicInfo = TopicInfo(
        title = "绝美ins自拍模版",
        description = "百万粉颜值博主们都在用的ins特效\n使用同款模版，保存后从醒图APP主页选择图片，分享至...",
        viewCount = "1102万次浏览量",
        modifyCount = "1102万次修同款"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate started")
        
        try {
            setContentView(R.layout.activity_inspiration_community)
            Log.d(TAG, "setContentView completed")
            
            initViews()
            Log.d(TAG, "initViews completed")
            
            initData()
            Log.d(TAG, "initData completed")
            
            setupRecyclerView()
            Log.d(TAG, "setupRecyclerView completed")
            
            setupClickListeners()
            Log.d(TAG, "setupClickListeners completed")
            
            updateTopicInfo()
            Log.d(TAG, "updateTopicInfo completed")
            
            loadContentData()
            Log.d(TAG, "loadContentData completed")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            Toast.makeText(this, "页面加载失败: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    /**
     * 初始化视图组件
     */
    private fun initViews() {
        try {
            // 导航栏组件 - 需要通过include布局访问
            val navBarLayout = findViewById<View>(R.id.include_navigation_bar)
            if (navBarLayout == null) {
                throw Exception("Cannot find navigation bar layout")
            }
            btnBack = navBarLayout.findViewById(R.id.btn_back)
            btnShare = navBarLayout.findViewById(R.id.btn_share)
            
            // 话题头部组件 - 需要通过include布局访问
            val topicHeaderLayout = findViewById<View>(R.id.include_topic_header)
            if (topicHeaderLayout == null) {
                throw Exception("Cannot find topic header layout")
            }
            tvTopicTitle = topicHeaderLayout.findViewById(R.id.tv_topic_title)
            tvStats = topicHeaderLayout.findViewById(R.id.tv_stats)
            tvDescription = topicHeaderLayout.findViewById(R.id.tv_description)
            
            // 筛选标签组件 - 需要通过include布局访问
            val filterTabsLayout = findViewById<View>(R.id.include_filter_tabs)
            if (filterTabsLayout == null) {
                throw Exception("Cannot find filter tabs layout")
            }
            tabFeatured = filterTabsLayout.findViewById(R.id.tab_featured)
            tabLatest = filterTabsLayout.findViewById(R.id.tab_latest)
            tvFeatured = filterTabsLayout.findViewById(R.id.tv_featured)
            tvLatest = filterTabsLayout.findViewById(R.id.tv_latest)
            indicatorFeatured = filterTabsLayout.findViewById(R.id.indicator_featured)
            indicatorLatest = filterTabsLayout.findViewById(R.id.indicator_latest)
            
            // 内容区域 - 直接在主布局中
            rvContentFeed = findViewById(R.id.rv_content_feed)
            if (rvContentFeed == null) {
                throw Exception("Cannot find content feed recycler view")
            }
            
            layoutLoadMore = findViewById(R.id.layout_load_more)
            if (layoutLoadMore == null) {
                throw Exception("Cannot find load more layout")
            }
            
            // 浮动发布按钮 - 需要通过include布局访问
            fabPublish = findViewById<View>(R.id.include_floating_publish_button) as FloatingActionButton
            if (fabPublish == null) {
                throw Exception("Cannot find floating publish button")
            }
            
            Log.d(TAG, "All views initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views", e)
            throw e
        }
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        contentList = generateMockData().toMutableList()
    }

    /**
     * 设置RecyclerView
     */
    private fun setupRecyclerView() {
        contentAdapter = ContentAdapter(
            contentList = contentList,
            onItemClick = { item ->
                Toast.makeText(this, "点击了: ${item.title}", Toast.LENGTH_SHORT).show()
            },
            onLikeClick = { item ->
                handleLikeClick(item)
            },
            onModifyClick = { item ->
                Toast.makeText(this, "修同款: ${item.title}", Toast.LENGTH_SHORT).show()
            }
        )
        
        // 设置瀑布流布局管理器 (双列)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvContentFeed.layoutManager = layoutManager
        rvContentFeed.adapter = contentAdapter
    }

    /**
     * 设置点击事件监听器
     */
    private fun setupClickListeners() {
        // 导航栏点击事件
        btnBack.setOnClickListener {
            finish()
        }
        
        btnShare.setOnClickListener {
            Toast.makeText(this, "分享功能", Toast.LENGTH_SHORT).show()
        }
        
        // 筛选标签点击事件
        tabFeatured.setOnClickListener {
            switchTab(FilterTab.FEATURED)
        }
        
        tabLatest.setOnClickListener {
            switchTab(FilterTab.LATEST)
        }
        
        // 浮动发布按钮点击事件
        fabPublish.setOnClickListener {
            Toast.makeText(this, "发布内容", Toast.LENGTH_SHORT).show()
        }
        
        // 底部加载更多点击返回顶部
        layoutLoadMore.setOnClickListener {
            rvContentFeed.smoothScrollToPosition(0)
        }
    }

    /**
     * 更新话题信息
     */
    private fun updateTopicInfo() {
        tvTopicTitle.text = topicInfo.title
        tvStats.text = "${topicInfo.viewCount}·${topicInfo.modifyCount}"
        tvDescription.text = topicInfo.description
    }

    /**
     * 切换筛选标签
     */
    private fun switchTab(tab: FilterTab) {
        if (currentTab == tab) return
        
        currentTab = tab
        
        when (tab) {
            FilterTab.FEATURED -> {
                // 更新UI状态
                tvFeatured.setTextColor(resources.getColor(android.R.color.black))
                tvFeatured.setTypeface(null, android.graphics.Typeface.BOLD)
                indicatorFeatured.visibility = View.VISIBLE
                
                tvLatest.setTextColor(resources.getColor(android.R.color.darker_gray))
                tvLatest.setTypeface(null, android.graphics.Typeface.NORMAL)
                indicatorLatest.visibility = View.INVISIBLE
                
                // 加载精选内容
                loadFeaturedContent()
            }
            FilterTab.LATEST -> {
                // 更新UI状态
                tvLatest.setTextColor(resources.getColor(android.R.color.black))
                tvLatest.setTypeface(null, android.graphics.Typeface.BOLD)
                indicatorLatest.visibility = View.VISIBLE
                
                tvFeatured.setTextColor(resources.getColor(android.R.color.darker_gray))
                tvFeatured.setTypeface(null, android.graphics.Typeface.NORMAL)
                indicatorFeatured.visibility = View.INVISIBLE
                
                // 加载最新内容
                loadLatestContent()
            }
        }
    }

    /**
     * 处理点赞点击
     */
    private fun handleLikeClick(item: ContentItem) {
        val index = contentList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            val updatedItem = if (item.isLiked) {
                item.copy(isLiked = false, likeCount = item.likeCount - 1)
            } else {
                item.copy(isLiked = true, likeCount = item.likeCount + 1)
            }
            contentList[index] = updatedItem
            contentAdapter.notifyItemChanged(index)
        }
    }

    /**
     * 加载内容数据
     */
    private fun loadContentData() {
        loadFeaturedContent()
    }

    /**
     * 加载精选内容
     */
    private fun loadFeaturedContent() {
        // 这里应该从网络或数据库加载精选内容
        // 暂时使用模拟数据
        val featuredContent = generateMockData()
        contentAdapter.updateData(featuredContent)
    }

    /**
     * 加载最新内容
     */
    private fun loadLatestContent() {
        // 这里应该从网络或数据库加载最新内容
        // 暂时使用模拟数据
        val latestContent = generateMockData().shuffled()
        contentAdapter.updateData(latestContent)
    }

    /**
     * 生成模拟数据
     */
    private fun generateMockData(): List<ContentItem> {
        return listOf(
            ContentItem(
                id = "1",
                title = "超自然精致伪素颜超看！！",
                author = Author("Aarttion"),
                likeCount = 223,
                type = ContentType.NORMAL
            ),
            ContentItem(
                id = "2",
                title = "大家快来看看超自然精致...",
                author = Author("Aarttion"),
                likeCount = 223,
                type = ContentType.LIVE,
                isLive = true
            ),
            ContentItem(
                id = "3",
                title = "买完水果都要先尝尝，美其名曰，替你踩雷",
                author = Author("Aarttion"),
                likeCount = 223,
                type = ContentType.LIVE,
                isLive = true
            ),
            ContentItem(
                id = "4",
                title = "迪士尼人物特效+涂鸦超级好看！！",
                author = Author("Christian"),
                likeCount = 0,
                type = ContentType.TEMPLATE,
                hasModifyButton = true
            ),
            ContentItem(
                id = "5",
                title = "大家快来看看超自然精致仿妆伪素颜超好看！！",
                author = Author("Aarttion"),
                likeCount = 223,
                type = ContentType.NORMAL
            ),
            ContentItem(
                id = "6",
                title = "即使出门在外，也不忘抓拍你的出糗瞬间📸",
                author = Author("Aarttion"),
                likeCount = 223,
                type = ContentType.LIVE,
                isLive = true
            ),
            ContentItem(
                id = "7",
                title = "岩彩画邮票模版",
                author = Author("Aarttion"),
                likeCount = 0,
                type = ContentType.TEMPLATE,
                usageCount = "使用量1234",
                hasModifyButton = true
            ),
            ContentItem(
                id = "8",
                title = "岩彩画邮票模版",
                author = Author("Aarttion"),
                likeCount = 0,
                type = ContentType.AI_GENERATED,
                isAI = true,
                usageCount = "使用量1234",
                hasModifyButton = true
            ),
            ContentItem(
                id = "9",
                title = "💗爱是深夜里抢你手中最后一口泡面🍜",
                author = Author("Aarttion"),
                likeCount = 223,
                type = ContentType.NORMAL
            ),
            ContentItem(
                id = "10",
                title = "氛围感超自然滤镜！！",
                author = Author("Aarttion"),
                likeCount = 223,
                type = ContentType.NORMAL
            )
        )
    }
} 