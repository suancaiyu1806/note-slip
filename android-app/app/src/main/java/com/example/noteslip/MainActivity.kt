package com.example.noteslip

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.nio.charset.Charset
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private lateinit var noteAdapter: NoteAdapter
    private var notes = mutableListOf<Note>()
    private var isCreatingNote = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (!nfcAdapter.isEnabled) {
            Toast.makeText(this, "设备不支持NFC", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 创建PendingIntent
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        // 初始化RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        noteAdapter = NoteAdapter(notes) { note ->
            // 点击便签跳转到编辑页面
            val editIntent = Intent(this, NoteEditActivity::class.java)
            editIntent.putExtra("note_key", note.key)
            startActivity(editIntent)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }

        // 加载本地便签
        loadNotes()

        // 设置悬浮按钮
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val editIntent = Intent(this, NoteEditActivity::class.java)
            startActivity(editIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isCreatingNote) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
        }
        loadNotes()
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    private fun loadNotes() {
        try {
            // 从本地数据库加载便签
            val context = applicationContext
            val db = NoteDatabase.getInstance(context)
            notes.clear()
            notes.addAll(db.noteDao().getAllNotes())
            noteAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            // 捕获异常并打印日志
            Log.e("NoteDatabaseError", "Failed to get database instance", e)
            // 可以在这里添加更多的错误处理逻辑，比如给用户提示
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let { handleNFCDiscovery(it) }
        }
    }

    private fun handleNFCDiscovery(tag: Tag) {
        if (isCreatingNote) return

        val db = NoteDatabase.getInstance(this)
        val nfcId = bytesToHexString(tag.id)
        
        // 检查便签是否存在
        val existingNote = db.noteDao().getNoteByNfcId(nfcId)
        if (existingNote != null) {
            // 便签已存在，跳转到编辑页面
            val editIntent = Intent(this, NoteEditActivity::class.java)
            editIntent.putExtra("note_key", existingNote.key)
            startActivity(editIntent)
            return
        }

        // 创建新便签
        isCreatingNote = true
        val newNote = Note(
            key = UUID.randomUUID().toString(),
            nfcId = nfcId,
            content = "",
            createTime = System.currentTimeMillis()
        )
        
        // 保存便签
        db.noteDao().insertNote(newNote)

        // 写入NFC标签
        try {
            val scheme = "weixin://dl/business/?appid=wx7fc49c74d54fdadd&path=/pages/note-edit/index&query=key=${newNote.key}&env_version=release"
            writeSchemeToTag(tag, scheme)
        } catch (e: Exception) {
            Toast.makeText(this, "写入NFC标签失败", Toast.LENGTH_SHORT).show()
        }

        // 跳转到编辑页面
        val editIntent = Intent(this, NoteEditActivity::class.java)
        editIntent.putExtra("note_key", newNote.key)
        startActivity(editIntent)
    }

    private fun writeSchemeToTag(tag: Tag, scheme: String) {
        // 创建URI Record
        val uriRecord = NdefRecord.createUri(scheme)
        
        // 创建Android Application Record
        val aarRecord = NdefRecord.createApplicationRecord("com.tencent.mm")
        
        // 创建NDEF消息
        val message = NdefMessage(arrayOf(uriRecord, aarRecord))
        
        // 写入NFC标签
        val ndef = Ndef.get(tag)
        if (ndef != null) {
            ndef.connect()
            if (!ndef.isWritable) {
                throw Exception("NFC标签不可写")
            }
            if (ndef.maxSize < message.byteArrayLength) {
                throw Exception("NFC标签容量不足")
            }
            ndef.writeNdefMessage(message)
            ndef.close()
        } else {
            throw Exception("不支持的NFC标签类型")
        }
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            val hex = Integer.toHexString(0xFF and b.toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }
} 