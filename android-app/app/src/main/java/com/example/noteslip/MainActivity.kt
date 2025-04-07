package com.example.noteslip

import android.app.AlertDialog
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
import com.example.noteslip.R
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
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // 初始化NFC
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            if (nfcAdapter == null || !nfcAdapter.isEnabled) {
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
            noteAdapter = NoteAdapter(notes, { note ->
                // 点击便签跳转到编辑页面
                val editIntent = Intent(this, NoteEditActivity::class.java)
                editIntent.putExtra("note_key", note.key)
                startActivity(editIntent)
            }, { note ->
                showDeleteConfirmationDialog(note)
            })
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
        } catch (e: Exception) {
            Log.e("MainActivity", "onCreate: 发生异常", e)
            Toast.makeText(this, "应用启动时发生错误，请检查日志", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // 确保 nfcAdapter 不为 null 且已启用
        if (nfcAdapter != null && nfcAdapter.isEnabled) {
            try {
                // 启用 APP 的 NFC 前台调度，禁用系统默认的 NFC 读取能力
                nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to enable foreground dispatch", e)
                Toast.makeText(this, "无法启用NFC前台调度，请检查设置", Toast.LENGTH_SHORT).show()
            }
        }
        loadNotes()
    }

    override fun onPause() {
        super.onPause()
        // 确保 nfcAdapter 不为 null 且已启用
        if (nfcAdapter != null && nfcAdapter.isEnabled) {
            try {
                // 禁用 APP 的 NFC 前台调度，恢复系统默认的 NFC 读取能力
                nfcAdapter.disableForegroundDispatch(this)
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to disable foreground dispatch", e)
            }
        }
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
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action|| NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
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
            Log.d("MainActivity", "NFC标签已绑定便签，跳转到编辑页面")
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
            writeSchemeToTag(tag, nfcId)
        } catch (e: Exception) {
            Toast.makeText(this, "写入NFC标签失败", Toast.LENGTH_SHORT).show()
        }
    
        // 跳转到编辑页面
        val editIntent = Intent(this, NoteEditActivity::class.java)
        editIntent.putExtra("note_key", newNote.key)
        startActivity(editIntent)
    }

    private fun writeSchemeToTag(tag: Tag, nfcId: String) {
        // 修改为当前 APP 的 scheme，并包含 nfcId
        val scheme = "noteslip://note-edit?nfcId=$nfcId"
        
        // 创建URI Record
        val uriRecord = NdefRecord.createUri(scheme)
        
        // 创建Android Application Record
        val aarRecord = NdefRecord.createApplicationRecord("com.example.noteslip")
        
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

    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(this)
           .setTitle("确认删除")
           .setMessage("确定要删除这条便签吗？")
           .setPositiveButton("删除") { _, _ ->
                deleteNote(note)
            }
           .setNegativeButton("取消", null)
           .show()
    }

    private fun deleteNote(note: Note) {
        try {
            val context = applicationContext
            val db = NoteDatabase.getInstance(context)
            db.noteDao().deleteNote(note)
            notes.remove(note)
            noteAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e("NoteDatabaseError", "Failed to delete note", e)
            Toast.makeText(this, "删除便签失败，请检查日志", Toast.LENGTH_SHORT).show()
        }
    }
}