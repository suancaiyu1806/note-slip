const cloud = require('wx-server-sdk')
cloud.init({
  env: cloud.DYNAMIC_CURRENT_ENV
})

const db = cloud.database()
const _ = db.command

exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()
  const { key, value } = event

  try {
    // 检查便签是否存在且属于当前用户
    const note = await db.collection('notes')
      .where({
        key: key,
        userId: wxContext.OPENID
      })
      .get()

    if (note.data.length === 0) {
      return {
        success: false,
        message: '便签不存在或无权限'
      }
    }

    // 更新便签内容
    await db.collection('notes')
      .where({
        key: key,
        userId: wxContext.OPENID
      })
      .update({
        data: {
          value: value,
          updateTime: db.serverDate()
        }
      })

    return {
      success: true
    }
  } catch (error) {
    console.error('更新便签失败：', error)
    return {
      success: false,
      message: '更新便签失败'
    }
  }
} 