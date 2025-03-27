const app = getApp()

Page({
  data: {
    notes: [],
    loading: true
  },

  onLoad: function() {
    this.loadNotes()
  },

  onShow: function() {
    this.loadNotes()
  },

  // 加载用户的所有便签
  async loadNotes() {
    try {
      const { result } = await wx.cloud.callFunction({
        name: 'listNotes'
      })
      
      this.setData({
        notes: result.data || [],
        loading: false
      })
    } catch (error) {
      console.error('加载便签失败：', error)
      wx.showToast({
        title: '加载便签失败',
        icon: 'none'
      })
      this.setData({ loading: false })
    }
  },

  // 点击便签卡片
  onNoteTap: function(e) {
    const { key } = e.currentTarget.dataset
    app.globalData.curKey = key;
    wx.switchTab({
      url: `/pages/note-edit/index`,
    })
  },

  // 扫描NFC
  async scanNFC() {
    try {
      const nfcAdapter = wx.getNFCAdapter()
      nfcAdapter.startDiscovery({
        success: (res) => {
          console.log('开始搜索NFC设备')
        },
        fail: (err) => {
          console.error('NFC搜索失败：', err)
          wx.showToast({
            title: 'NFC搜索失败',
            icon: 'none'
          })
        }
      })

      nfcAdapter.onDiscovered((res) => {
        console.log('发现NFC设备：', res)
        // 处理NFC设备发现事件
        this.handleNFCDiscovery(res)
      })
    } catch (error) {
      console.error('NFC初始化失败：', error)
      wx.showToast({
        title: 'NFC初始化失败',
        icon: 'none'
      })
    }
  },

  // 处理NFC设备发现
  async handleNFCDiscovery(res) {
    try {
      const { result } = await wx.cloud.callFunction({
        name: 'getNote',
        data: {
          nfcId: res.id
        }
      })

      if (result?.exists) {
        // 便签已存在，跳转到编辑页面
          app.globalData.curKey = result.key
          wx.switchTab({
            url: `/pages/note-edit/index`
          })
      } else {
        // 创建新便签
        const createResult = await wx.cloud.callFunction({
          name: 'createNote',
          data: {
            nfcId: res.id
          }
        })
        
        if (createResult.result.success) {
          app.globalData.curKey = createResult.result.key
          wx.switchTab({
            url: `/pages/note-edit/index`
          })
        }
      }
    } catch (error) {
      console.error('处理NFC设备失败：', error)
      wx.showToast({
        title: '处理NFC设备失败',
        icon: 'none'
      })
    }
  }
}) 