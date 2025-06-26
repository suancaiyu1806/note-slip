package com.example.noteslip

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class InspirationCommunityActivity : AppCompatActivity() {
    
    private lateinit var templatesRecyclerView: RecyclerView
    private lateinit var templateAdapter: TemplateAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var fab: FloatingActionButton
    
    private var allTemplates = mutableListOf<TemplateCard>()
    private var currentTemplates = mutableListOf<TemplateCard>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspiration_community)
        
        initViews()
        setupRecyclerView()
        setupTabLayout()
        setupFloatingActionButton()
        loadSampleData()
    }
    
    private fun initViews() {
        templatesRecyclerView = findViewById(R.id.templatesRecyclerView)
        tabLayout = findViewById(R.id.tabLayout)
        fab = findViewById(R.id.fab)
        
        // 设置返回按钮
        findViewById<android.widget.ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }
        
        // 设置分享按钮
        findViewById<android.widget.ImageView>(R.id.shareButton).setOnClickListener {
            Toast.makeText(this, "分享功能", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupRecyclerView() {
        templateAdapter = TemplateAdapter(
            currentTemplates,
            onItemClick = { template ->
                Toast.makeText(this, "点击了: ${template.title}", Toast.LENGTH_SHORT).show()
            },
            onLikeClick = { template ->
                handleLikeClick(template)
            }
        )
        
        templatesRecyclerView.apply {
            layoutManager = GridLayoutManager(this@InspirationCommunityActivity, 2)
            adapter = templateAdapter
        }
    }
    
    private fun setupTabLayout() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showFeaturedTemplates()
                    1 -> showLatestTemplates()
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupFloatingActionButton() {
        fab.setOnClickListener {
            Toast.makeText(this, "创建新模版", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadSampleData() {
        allTemplates.apply {
            add(TemplateCard(
                id = "1",
                imageUrl = "",
                title = "超自然精致伪素颜超看！！",
                authorName = "Aarttion",
                authorAvatar = "",
                likeCount = 223,
                hasAIBadge = true,
                type = TemplateType.LIKED_FEED
            ))
            
            add(TemplateCard(
                id = "2",
                imageUrl = "",
                title = "岩彩画邮票模版",
                authorName = "Aarttion",
                authorAvatar = "",
                likeCount = 456,
                usageCount = 1234,
                type = TemplateType.TEMPLATE_FEED
            ))
            
            add(TemplateCard(
                id = "3",
                imageUrl = "",
                title = "大家快来看看超自然精致...",
                authorName = "Aarttion",
                authorAvatar = "",
                likeCount = 189,
                type = TemplateType.LIVE_FEED
            ))
            
            add(TemplateCard(
                id = "4",
                imageUrl = "",
                title = "买完水果都要先尝尝，美其名曰，替你踩雷",
                authorName = "Aarttion",
                authorAvatar = "",
                likeCount = 334,
                type = TemplateType.REGULAR
            ))
            
            add(TemplateCard(
                id = "5",
                imageUrl = "",
                title = "迪士尼人物特效+涂鸦超级好看！！",
                authorName = "Christian",
                authorAvatar = "",
                likeCount = 567,
                type = TemplateType.TEMPLATE_FEED
            ))
            
            add(TemplateCard(
                id = "6",
                imageUrl = "",
                title = "即使出门在外，也不忘抓拍你的出糗瞬间📸",
                authorName = "Aarttion",
                authorAvatar = "",
                likeCount = 445,
                type = TemplateType.REGULAR
            ))
            
            add(TemplateCard(
                id = "7",
                imageUrl = "",
                title = "💗爱是深夜里抢你手中最后一口泡面🍜",
                authorName = "Aarttion",
                authorAvatar = "",
                likeCount = 678,
                type = TemplateType.REGULAR
            ))
            
            add(TemplateCard(
                id = "8",
                imageUrl = "",
                title = "氛围感超自然滤镜！！",
                authorName = "Aarttion",
                authorAvatar = "",
                likeCount = 234,
                type = TemplateType.REGULAR
            ))
        }
        
        // 默认显示精选内容
        showFeaturedTemplates()
    }
    
    private fun showFeaturedTemplates() {
        currentTemplates.clear()
        currentTemplates.addAll(allTemplates.filter { 
            it.type == TemplateType.TEMPLATE_FEED || it.hasAIBadge || it.usageCount != null 
        })
        templateAdapter.notifyDataSetChanged()
    }
    
    private fun showLatestTemplates() {
        currentTemplates.clear()
        currentTemplates.addAll(allTemplates.sortedByDescending { it.id })
        templateAdapter.notifyDataSetChanged()
    }
    
    private fun handleLikeClick(template: TemplateCard) {
        val index = currentTemplates.indexOfFirst { it.id == template.id }
        if (index != -1) {
            val updatedTemplate = template.copy(
                isLiked = !template.isLiked,
                likeCount = if (template.isLiked) template.likeCount - 1 else template.likeCount + 1
            )
            currentTemplates[index] = updatedTemplate
            templateAdapter.notifyItemChanged(index)
        }
    }
} 