const app = getApp()

Page({
  data: {
    noteKey: '',
    content: '',
    loading: true
  },

  onLoad: function(options) {
    if (options.key) {
      this.setData({ noteKey: options.key })
      this.loadNoteContent()
    }
  },

  // 加载便签内容
  async loadNoteContent() {
    try {
      const { result } = await wx.cloud.callFunction({
        name: 'getNote',
        data: {
          key: this.data.noteKey
        }
      })

      this.setData({
        content: result.value || '',
        loading: false
      })
    } catch (error) {
      console.error('加载便签内容失败：', error)
      wx.showToast({
        title: '加载便签内容失败',
        icon: 'none'
      })
      this.setData({ loading: false })
    }
  },

  // 内容输入处理
  onContentInput: function(e) {
    this.setData({
      content: e.detail.value
    })
  },

  // 保存便签
  async saveNote() {
    try {
      await wx.cloud.callFunction({
        name: 'updateNote',
        data: {
          key: this.data.noteKey,
          value: this.data.content
        }
      })

      wx.showToast({
        title: '保存成功',
        icon: 'success'
      })
    } catch (error) {
      console.error('保存便签失败：', error)
      wx.showToast({
        title: '保存失败',
        icon: 'none'
      })
    }
  },

  // 清空便签
  clearNote() {
    wx.showModal({
      title: '确认清空',
      content: '确定要清空当前便签内容吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await wx.cloud.callFunction({
              name: 'updateNote',
              data: {
                key: this.data.noteKey,
                value: ''
              }
            })

            this.setData({
              content: ''
            })

            wx.showToast({
              title: '清空成功',
              icon: 'success'
            })
          } catch (error) {
            console.error('清空便签失败：', error)
            wx.showToast({
              title: '清空失败',
              icon: 'none'
            })
          }
        }
      }
    })
  }
}) 