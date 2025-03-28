# Note Slip - 微信小程序便签应用

这是一个基于微信小程序云开发的便签应用，让用户可以方便地创建、查看和管理便签。通过 NFC 技术，用户可以将便签与实体 NFC 卡片关联，实现快速访问和管理。

## 主要功能

- 创建新便签
- 查看便签列表
- 查看便签详情
- 编辑和更新便签
- NFC 卡片扫描和关联
- 通过 NFC 快速访问便签

## 技术架构

本项目使用微信小程序云开发，包含以下核心能力：

- 云数据库：存储便签数据
- 云函数：处理便签的增删改查等业务逻辑
- 小程序前端：提供用户界面和交互
- NFC 能力：实现便签与实体卡片的关联

## 项目结构

```
├── miniprogram/          # 小程序前端代码
│   └── pages/           # 页面文件
│       ├── home/        # 主页（便签列表）
│       └── note-edit/   # 便签编辑页
└── cloudfunctions/      # 云函数
    ├── createNote/      # 创建便签
    ├── listNotes/       # 获取便签列表
    ├── getNote/         # 获取便签详情
    └── updateNote/      # 更新便签
```

## 开发环境

- 微信开发者工具
- 云开发环境
- 支持 NFC 功能的设备（用于测试）

## 使用说明

1. 扫描 NFC 卡片创建新便签
2. 编辑便签内容
3. 再次扫描同一张 NFC 卡片可快速访问关联的便签
4. 每张 NFC 卡片只能关联一张便签

## 参考文档

- [微信小程序开发文档](https://developers.weixin.qq.com/miniprogram/dev/framework/)
- [云开发文档](https://developers.weixin.qq.com/miniprogram/dev/wxcloud/basis/getting-started.html)
- [微信小程序 NFC 能力文档](https://developers.weixin.qq.com/miniprogram/dev/api/device/nfc/wx.getNFCAdapter.html)

