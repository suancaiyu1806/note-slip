const app = getApp()

Page({
  data: {
    notes: [],
    loading: true,
    isCreatingNote: false // 添加状态标记，用于控制NFC监听
  },

  onLoad: function() {
    this.loadNotes()
    this.startScanning()
  },

  onShow: function() {
    this.loadNotes()
    this.startScanning()
  },

  onHide: function() {
    this.stopScanning()
  },

  onUnload: function() {
    this.stopScanning()
  },

  // 格式化时间
  formatDate: function(dateStr) {
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}.${month}.${day} ${hours}:${minutes}:${seconds}`;
  },

  // 加载用户的所有便签
  async loadNotes() {
    try {
      const { result } = await wx.cloud.callFunction({
        name: 'listNotes'
      })
      
      // 处理时间格式
      const formattedNotes = (result.data || []).map(note => ({
        ...note,
        createTime: this.formatDate(note.createTime)
      }));
      
      this.setData({
        notes: formattedNotes,
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

  // 开始扫描
  startScanning: function() {
    if (this.data.isCreatingNote) return; // 如果正在创建便签，不启动扫描
    
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

  // 停止扫描
  stopScanning: function() {
    try {
      const nfcAdapter = wx.getNFCAdapter()
      nfcAdapter.stopDiscovery({
        success: (res) => {
          console.log('停止搜索NFC设备')
        },
        fail: (err) => {
          console.error('停止NFC搜索失败：', err)
        }
      })
    } catch (error) {
      console.error('停止NFC失败：', error)
    }
  },

  // 写入scheme到NFC标签
  async writeSchemeToTag(nfcAdapter, scheme) {
    try {
      // 获取ISO-DEP实例
      const isoDep = nfcAdapter.getIsoDep();
      
      // 连接NFC标签
      await isoDep.connect();
      
      // 清空NFC标签
      try {
        await isoDep.transceive({
          data: new ArrayBuffer(0) // 发送空数据来清空标签
        });
        console.log('清空NFC标签成功');
      } catch (clearError) {
        console.log('清空NFC标签失败，可能标签已经是空的：', clearError);
      }
      
      // 写入scheme
      await isoDep.transceive({
        data: scheme
      });
      
      // 断开连接
      isoDep.close();
      
      console.log('写入scheme成功，scheme内容：', scheme);
    } catch (error) {
      console.error('写入scheme失败：', error);
      throw error;
    }
  },

  // 处理NFC设备发现
  async handleNFCDiscovery(res) {
    if (this.data.isCreatingNote) return; // 如果正在创建便签，不处理新的NFC发现事件
    
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
        // 设置创建状态，停止NFC监听
        this.setData({ isCreatingNote: true });
        this.stopScanning();

        try {
          // 创建新便签
          const createResult = await wx.cloud.callFunction({
            name: 'createNote',
            data: {
              nfcId: res.id
            }
          })
          
          if (createResult.result.success) {
            // 写入scheme到NFC标签
            const nfcAdapter = wx.getNFCAdapter()
            // 使用明文URL Scheme格式
            const scheme = `weixin://dl/business/?appid=wx7fc49c74d54fdadd&path=/pages/note-edit/index&query=key=${createResult.result.key}&env_version=develop`
            
            try {
              await this.writeSchemeToTag(nfcAdapter, scheme);
            } catch (writeError) {
              console.error('写入scheme失败：', writeError)
              // 即使写入失败也继续执行，不影响创建便签的功能
            }

            app.globalData.curKey = createResult.result.key
            wx.switchTab({
              url: `/pages/note-edit/index`
            })
          }
        } catch (error) {
          console.error('创建便签失败：', error)
          wx.showToast({
            title: '创建便签失败',
            icon: 'none'
          })
        } finally {
          // 无论成功失败，都重置状态并重新开始扫描
          this.setData({ isCreatingNote: false });
          this.startScanning();
        }
      }
    } catch (error) {
      console.error('处理NFC设备失败：', error)
      wx.showToast({
        title: '处理NFC设备失败',
        icon: 'none'
      })
      // 发生错误时也要重置状态
      this.setData({ isCreatingNote: false });
      this.startScanning();
    }
  }
}) 