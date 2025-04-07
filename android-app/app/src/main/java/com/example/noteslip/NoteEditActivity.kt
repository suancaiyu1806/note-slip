package com.example.noteslip

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.noteslip.R
import java.util.*

class NoteEditActivity : AppCompatActivity() {
    private lateinit var noteKey: String
    private lateinit var editText: EditText
    private lateinit var db: NoteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)

        // 检查是否通过scheme打开活动
        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {
            val data = intent.data
            if (data != null) {
                // 从scheme中提取note_key
                noteKey = data.getQueryParameter("key") ?: UUID.randomUUID().toString()
            } else {
                // 如果没有从scheme中获取到note_key，使用默认逻辑
                noteKey = intent.getStringExtra("note_key") ?: UUID.randomUUID().toString()
            }
        } else {
            // 正常启动活动，使用默认逻辑
            noteKey = intent.getStringExtra("note_key") ?: UUID.randomUUID().toString()
        }

        // 初始化数据库
        db = NoteDatabase.getInstance(this)

        // 初始化EditText
        editText = findViewById(R.id.editText)

        // 加载便签内容
        loadNote()

        // 设置文本变化监听
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                saveNote()
            }
        })
    }

    private fun loadNote() {
        val note = db.noteDao().getNoteByKey(noteKey)
        if (note != null) {
            editText.setText(note.content)
        }
    }

    private fun saveNote() {
        val content = editText.text.toString()
        val nfcId = db.noteDao().getNoteByKey(noteKey)?.nfcId ?: noteKey
        val note = Note(
            key = noteKey,
            nfcId = nfcId,
            content = content,
            createTime = System.currentTimeMillis()
        )
        db.noteDao().insertNote(note)
    }

    override fun onDestroy() {
        super.onDestroy()
        saveNote()
    }
}