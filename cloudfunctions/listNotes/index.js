const cloud = require('wx-server-sdk')
cloud.init({
  env: cloud.DYNAMIC_CURRENT_ENV
})

const db = cloud.database()
const _ = db.command

exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()

  try {
    // 获取当前用户的所有便签
    const notes = await db.collection('notes')
      .where({
        userId: wxContext.OPENID
      })
      .orderBy('updateTime', 'desc')
      .get()

    // 格式化时间
    const data = notes.data.map(note => ({
      key: note.key,
      value: note.value,
      createTime: note.createTime,
      updateTime: note.updateTime
    }))

    return {
      success: true,
      data
    }
  } catch (error) {
    console.error('获取便签列表失败：', error)
    return {
      success: false,
      message: '获取便签列表失败'
    }
  }
} 