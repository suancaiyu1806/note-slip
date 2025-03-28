const cloud = require('wx-server-sdk')
cloud.init({
  env: cloud.DYNAMIC_CURRENT_ENV
})

const db = cloud.database()
const _ = db.command

exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()
  const { key, nfcId } = event

  try {
    let query = {}
    
    if (key) {
      query.key = key
    } else if (nfcId) {
      query.nfcId = nfcId
    } else {
      return {
        success: false,
        message: '参数错误'
      }
    }

    // 查询便签
    const note = await db.collection('notes')
      .where(query)
      .get()

    if (note.data.length === 0) {
      return {
        exists: false
      }
    }

    return {
      exists: true,
      key: note.data[0].key,
      value: note.data[0].value,
      createTime: note.data[0].createTime,
      updateTime: note.data[0].updateTime
    }
  } catch (error) {
    console.error('获取便签失败：', error)
    return {
      success: false,
      message: '获取便签失败'
    }
  }
} 