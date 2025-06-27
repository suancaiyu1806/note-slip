package com.example.noteslip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * AI修图应用主页面Activity
 * 根据设计稿实现的页面布局，包含：
 * 1. 顶部Banner区域 (id: 2979:51146) - 包含AI人像品牌标识和绿色渐变背景
 * 2. VIP和搜索栏 (id: 2979:51188) - 提供VIP入口和搜索功能
 * 3. 核心功能面板 (id: 2979:51194) - 展示主要工具入口
 * 4. 内容推荐模块 (id: 2979:51195) - 展示功能推荐卡片
 * 5. 底部导航栏 (id: 2979:51197) - 包含修图、灵感、我的三个标签
 */
public class AIPhotoEditorActivity extends AppCompatActivity {

    // 功能按钮
    private LinearLayout importImageButton;
    private LinearLayout cameraButton;
    private LinearLayout liveEditButton;
    private LinearLayout portraitBeautyButton;
    private LinearLayout collageButton;
    private LinearLayout vipButton;

    // 底部导航
    private LinearLayout editTab;
    private LinearLayout inspirationTab;
    private LinearLayout profileTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_photo_editor);

        // 初始化视图
        initViews();
        
        // 设置点击事件
        setupClickListeners();
    }

    /**
     * 初始化所有视图控件
     */
    private void initViews() {
        // 主要功能按钮
        importImageButton = findViewById(R.id.importImageButton);
        cameraButton = findViewById(R.id.cameraButton);
        liveEditButton = findViewById(R.id.liveEditButton);
        portraitBeautyButton = findViewById(R.id.portraitBeautyButton);
        collageButton = findViewById(R.id.collageButton);
        vipButton = findViewById(R.id.vipButton);

        // 底部导航
        editTab = findViewById(R.id.editTab);
        inspirationTab = findViewById(R.id.inspirationTab);
        profileTab = findViewById(R.id.profileTab);
    }

    /**
     * 设置所有按钮的点击事件
     */
    private void setupClickListeners() {
        // 导入图片按钮
        if (importImageButton != null) {
            importImageButton.setOnClickListener(v -> {
                showToast("导入图片功能");
                // TODO: 实现图片导入功能
            });
        }

        // 相机按钮
        if (cameraButton != null) {
            cameraButton.setOnClickListener(v -> {
                showToast("相机功能");
                // TODO: 实现相机功能
            });
        }

        // 修Live按钮
        if (liveEditButton != null) {
            liveEditButton.setOnClickListener(v -> {
                showToast("修Live功能");
                // TODO: 实现Live编辑功能
            });
        }

        // 人像美化按钮
        if (portraitBeautyButton != null) {
            portraitBeautyButton.setOnClickListener(v -> {
                showToast("人像美化功能");
                // TODO: 实现人像美化功能
            });
        }

        // 拼图按钮
        if (collageButton != null) {
            collageButton.setOnClickListener(v -> {
                showToast("拼图功能");
                // TODO: 实现拼图功能
            });
        }

        // VIP按钮
        if (vipButton != null) {
            vipButton.setOnClickListener(v -> {
                showToast("开通会员");
                // TODO: 实现VIP功能
            });
        }

        // 底部导航 - 修图
        if (editTab != null) {
            editTab.setOnClickListener(v -> {
                showToast("修图页面");
                // 当前页面就是修图页面，可以刷新或执行其他逻辑
            });
        }

        // 底部导航 - 灵感
        if (inspirationTab != null) {
            inspirationTab.setOnClickListener(v -> {
                showToast("灵感页面");
                // TODO: 跳转到灵感页面
            });
        }

        // 底部导航 - 我的
        if (profileTab != null) {
            profileTab.setOnClickListener(v -> {
                showToast("我的页面");
                // TODO: 跳转到个人页面
            });
        }
    }

    /**
     * 显示Toast消息
     * @param message 要显示的消息
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 页面结构说明：
     * 
     * 根据设计稿 figma1.json 中的 combine 规则：
     * - 页面采用上下固定、中间可滚动的布局结构
     * - 顶部是固定的页面头部区域，包含品牌视觉和VIP/搜索栏
     * - 中间是可垂直滚动的主内容区，由核心功能面板和内容推荐模块组成
     * - 底部是固定的主导航栏，提供主要功能模块切换
     * 
     * 各组件功能：
     * 1. Banner (component_banner.xml) - 视觉焦点区域，展示AI人像品牌标识
     * 2. TopBar (component_top_bar.xml) - VIP入口和搜索功能
     * 3. FunctionPanel (component_function_panel.xml) - 核心功能入口，网格布局展示主要工具
     * 4. ContentRecommendation (component_content_recommendation.xml) - 内容推荐，横向滚动卡片
     * 5. BottomNavigation (component_bottom_navigation.xml) - 应用主导航，包含修图、灵感、我的
     */
} 