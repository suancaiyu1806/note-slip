# 灵感社区详情页 - InspirationDetailActivity

## 概述
根据 figma1.json 设计稿创建的灵感社区详情页面，采用上中下三段式布局设计。

## 页面结构

### 1. 顶部导航栏 (component_navigation_bar.xml)
- **返回按钮**: 左上角箭头图标，点击返回上一页
- **作者信息**: 头像 + 昵称（"古丽娅"）
- **关注按钮**: 可切换关注/已关注状态
- **分享按钮**: 右上角分享图标

### 2. 中部内容卡片 (component_content_card.xml)
- **主图片**: 大尺寸展示图（524dp高度）
- **对比图片**: 可选显示的对比效果图
- **对比按钮**: 右下角圆形按钮，切换对比模式
- **标题**: "一键Get你的专属虚拟偶像"
- **话题标签**: 
  - "古早低像素氛围感"
  - "OOTD今天这么穿"

### 3. 底部操作栏
包含两个部分：

#### 3.1 社交互动区 (component_social_interaction.xml)
- **点赞**: 心形图标 + 数量（123）
- **收藏**: 星形图标 + 数量（332）
- **评论**: 对话框图标 + 数量（41）

#### 3.2 主要功能按钮 (component_action_button.xml)
- **修同款按钮**: 黑色背景，白色文字"修同款1280"

## 主要功能

### 交互功能
1. **关注/取消关注**: 切换按钮文字状态
2. **点赞**: 切换点赞状态，更新数量和图标颜色
3. **收藏**: 切换收藏状态，更新数量和图标颜色
4. **对比模式**: 显示/隐藏对比图片
5. **修同款**: 核心功能按钮（待实现跳转逻辑）

### 布局特点
- **固定顶部**: 导航栏始终显示在顶部
- **可滚动中部**: 内容区域支持垂直滚动
- **固定底部**: 操作栏始终显示在底部
- **响应式设计**: 适配不同屏幕尺寸

## 文件结构

```
app/src/main/
├── res/layout/
│   ├── activity_inspiration_detail.xml      # 主布局文件
│   ├── component_navigation_bar.xml         # 导航栏组件
│   ├── component_content_card.xml           # 内容卡片组件
│   ├── component_social_interaction.xml     # 社交互动组件
│   └── component_action_button.xml          # 功能按钮组件
├── res/drawable/
│   ├── bg_follow_button.xml                 # 关注按钮背景
│   ├── bg_compare_button.xml                # 对比按钮背景
│   ├── bg_tag.xml                           # 话题标签背景
│   ├── bg_action_button.xml                 # 主要功能按钮背景
│   ├── ic_arrow_back.xml                    # 返回箭头图标
│   ├── ic_share.xml                         # 分享图标
│   ├── ic_compare.xml                       # 对比图标
│   ├── ic_heart.xml                         # 点赞图标
│   ├── ic_star.xml                          # 收藏图标
│   ├── ic_comment.xml                       # 评论图标
│   ├── ic_hashtag.xml                       # 话题标签图标
│   ├── default_image.xml                    # 默认图片占位符
│   └── default_avatar.xml                   # 默认头像占位符
└── java/com/example/noteslip/
    └── InspirationDetailActivity.kt         # Activity逻辑处理
```

## 颜色方案
- **主背景**: #FFFFFF (白色)
- **文字颜色**: #000000 (黑色)
- **话题标签背景**: #F6FDE8 (浅绿色)
- **话题标签文字**: #2B5C0C (深绿色)
- **主要按钮背景**: #000000 (黑色)
- **主要按钮文字**: #FFFFFF (白色)

## 注意事项
1. 需要添加图片加载库（如 Glide 或 Picasso）来加载网络图片
2. 圆形头像需要 CircleImageView 依赖
3. 社交功能需要对接后端 API
4. 分享功能需要实现 Intent.ACTION_SEND
5. 评论功能需要额外的评论页面或对话框 