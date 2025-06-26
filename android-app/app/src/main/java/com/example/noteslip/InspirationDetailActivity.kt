package com.example.noteslip

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InspirationDetailActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tvAuthorName: TextView
    private lateinit var btnFollow: Button
    private lateinit var btnShare: ImageView
    private lateinit var ivMainImage: ImageView
    private lateinit var btnCompare: Button
    private lateinit var tvTitle: TextView
    private lateinit var llLike: LinearLayout
    private lateinit var tvLikeCount: TextView
    private lateinit var llFavorite: LinearLayout
    private lateinit var tvFavoriteCount: TextView
    private lateinit var llComment: LinearLayout
    private lateinit var tvCommentCount: TextView
    private lateinit var btnAction: Button

    private var isLiked = false
    private var isFavorited = false
    private var isFollowed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspiration_detail)

        initViews()
        setupClickListeners()
        loadData()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btn_back)
        tvAuthorName = findViewById(R.id.tv_author_name)
        btnFollow = findViewById(R.id.btn_follow)
        btnShare = findViewById(R.id.btn_share)
        ivMainImage = findViewById(R.id.iv_main_image)
        btnCompare = findViewById(R.id.btn_compare)
        tvTitle = findViewById(R.id.tv_title)
        llLike = findViewById(R.id.ll_like)
        tvLikeCount = findViewById(R.id.tv_like_count)
        llFavorite = findViewById(R.id.ll_favorite)
        tvFavoriteCount = findViewById(R.id.tv_favorite_count)
        llComment = findViewById(R.id.ll_comment)
        tvCommentCount = findViewById(R.id.tv_comment_count)
        btnAction = findViewById(R.id.btn_action)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener { finish() }
        
        btnFollow.setOnClickListener {
            isFollowed = !isFollowed
            btnFollow.text = if (isFollowed) "已关注" else "关注"
        }

        llLike.setOnClickListener {
            isLiked = !isLiked
            val currentCount = tvLikeCount.text.toString().toIntOrNull() ?: 0
            val newCount = if (isLiked) currentCount + 1 else currentCount - 1
            tvLikeCount.text = newCount.toString()
        }

        llFavorite.setOnClickListener {
            isFavorited = !isFavorited
            val currentCount = tvFavoriteCount.text.toString().toIntOrNull() ?: 0
            val newCount = if (isFavorited) currentCount + 1 else currentCount - 1
            tvFavoriteCount.text = newCount.toString()
        }

        btnAction.setOnClickListener {
            // 修同款功能
        }
    }

    private fun loadData() {
        tvAuthorName.text = "古丽娅"
        tvTitle.text = "一键Get你的专属虚拟偶像"
        tvLikeCount.text = "123"
        tvFavoriteCount.text = "332"
        tvCommentCount.text = "41"
    }
} 