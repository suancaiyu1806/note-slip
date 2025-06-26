package com.example.noteslip

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VipSubscriptionActivity : AppCompatActivity() {
    
    private lateinit var toolbarAdapter: ToolbarAdapter
    private lateinit var vipPackageAdapter: VipPackageAdapter
    private lateinit var agreementCheckbox: CheckBox
    private lateinit var subscribeButton: View
    private lateinit var priceText: TextView
    
    private var selectedPackageIndex = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vip_subscription)
        
        initViews()
        setupToolbar()
        setupVipPackages()
        setupBottomActions()
    }
    
    private fun initViews() {
        val toolbarRecyclerView = findViewById<RecyclerView>(R.id.rv_toolbar)
        val vipPackagesRecyclerView = findViewById<RecyclerView>(R.id.rv_vip_packages)
        agreementCheckbox = findViewById(R.id.cb_agreement)
        subscribeButton = findViewById(R.id.btn_subscribe)
        priceText = findViewById(R.id.tv_price)
        
        toolbarRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        vipPackagesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
    
    private fun setupToolbar() {
        val toolbarItems = listOf(
            ToolbarItem("染发体验", R.drawable.ic_portrait_beauty, true),
            ToolbarItem("极致面部", R.drawable.ic_feature_smart, false),
            ToolbarItem("体贴美颜", R.drawable.ic_effect, false),
            ToolbarItem("智能调节", R.drawable.ic_beauty_auto, false)
        )
        
        toolbarAdapter = ToolbarAdapter(toolbarItems)
        findViewById<RecyclerView>(R.id.rv_toolbar).adapter = toolbarAdapter
    }
    
    private fun setupVipPackages() {
        val vipPackages = listOf(
            VipPackage("连续包月首月", "¥9", "¥30", "折合¥9.8/月", "幸运折扣", true),
            VipPackage("连续包年", "¥118", "¥198", "折合¥9.8/月", "优惠最多", false),
            VipPackage("年卡", "¥198", "", "折合¥16/月", "", false),
            VipPackage("月卡", "¥30", "", "", "", false)
        )
        
        vipPackageAdapter = VipPackageAdapter(vipPackages) { position ->
            selectedPackageIndex = position
            updateSubscribeButton()
        }
        findViewById<RecyclerView>(R.id.rv_vip_packages).adapter = vipPackageAdapter
        
        // 默认选中第一个套餐
        updateSubscribeButton()
    }
    
    private fun setupBottomActions() {
        agreementCheckbox.setOnCheckedChangeListener { _, _ ->
            updateSubscribeButton()
        }
        
        subscribeButton.setOnClickListener {
            if (agreementCheckbox.isChecked) {
                val selectedPackage = vipPackageAdapter.getSelectedPackage()
                Toast.makeText(this, "订阅 ${selectedPackage?.title} - ${selectedPackage?.price}", Toast.LENGTH_SHORT).show()
                // TODO: 实现订阅逻辑
            } else {
                Toast.makeText(this, "请先同意会员协议", Toast.LENGTH_SHORT).show()
            }
        }
        
        // 关闭按钮
        findViewById<View>(R.id.btn_close).setOnClickListener {
            finish()
        }
        
        // 移除效果
        findViewById<TextView>(R.id.tv_remove_effect).setOnClickListener {
            finish()
        }
        
        // 会员协议
        findViewById<TextView>(R.id.tv_member_agreement).setOnClickListener {
            // TODO: 打开会员协议页面
            Toast.makeText(this, "会员协议", Toast.LENGTH_SHORT).show()
        }
        
        // 我已是VIP
        findViewById<TextView>(R.id.tv_already_vip).setOnClickListener {
            // TODO: 处理已是VIP逻辑
            Toast.makeText(this, "我已是VIP", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateSubscribeButton() {
        val selectedPackage = vipPackageAdapter.getSelectedPackage()
        val isEnabled = agreementCheckbox.isChecked && selectedPackage != null
        
        subscribeButton.isEnabled = isEnabled
        subscribeButton.alpha = if (isEnabled) 1.0f else 0.6f
        
        selectedPackage?.let {
            priceText.text = it.price
        }
    }
}

// 工具栏项数据类
data class ToolbarItem(
    val title: String,
    val iconRes: Int,
    val isSelected: Boolean
)

// VIP套餐数据类
data class VipPackage(
    val title: String,
    val price: String,
    val originalPrice: String,
    val monthlyPrice: String,
    val badge: String,
    val isSelected: Boolean
) 