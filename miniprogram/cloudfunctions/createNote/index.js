const cloud = require('wx-server-sdk')
cloud.init({
  env: cloud.DYNAMIC_CURRENT_ENV
})

const db = cloud.database()
const _ = db.command

// 生成随机key
function generateKey() {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  let key = ''
  for (let i = 0; i < 16; i++) {
    key += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return key
}

exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()
  const { nfcId } = event

  try {
    // 检查NFC ID是否已存在
    const existingNote = await db.collection('notes')
      .where({
        nfcId: nfcId
      })
      .get()

    if (existingNote.data.length > 0) {
      return {
        success: false,
        message: '该NFC卡片已被使用'
      }
    }

    // 生成新的便签key
    const key = generateKey()

    // 创建新便签
    await db.collection('notes').add({
      data: {
        key,
        nfcId,
        value: '',
        userId: wxContext.OPENID,
        createTime: db.serverDate(),
        updateTime: db.serverDate()
      }
    })

    return {
      success: true,
      key
    }
  } catch (error) {
    console.error('创建便签失败：', error)
    return {
      success: false,
      message: '创建便签失败'
    }
  }
} 