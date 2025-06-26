package com.example.noteslip

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.LinearLayout
import android.graphics.Color
import android.view.View
import android.util.Log

class MembershipActivity : AppCompatActivity() {
    
    private var backButton: ImageView? = null
    private var monthlyPackage: LinearLayout? = null
    private var yearlyPackage: LinearLayout? = null
    private var firstMonthPackage: LinearLayout? = null
    private var regularYearlyPackage: LinearLayout? = null
    private var agreeCheckbox: CheckBox? = null
    private var subscribeButton: LinearLayout? = null
    private var currentSelectedPackage: LinearLayout? = null
    private var subscribeButtonText: TextView? = null
    
    private val packagePrices = mapOf(
        "monthly" to "¥30",
        "yearly" to "¥118", 
        "firstMonth" to "¥9",
        "regularYearly" to "¥198"
    )
    
    private var selectedPackage = "firstMonth"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_membership)
            
            // 简化的状态栏设置
            try {
                window.statusBarColor = Color.parseColor("#2A2C2B")
                window.navigationBarColor = Color.BLACK
            } catch (e: Exception) {
                Log.w("MembershipActivity", "无法设置状态栏颜色: ${e.message}")
            }
            
            if (initViews()) {
                setupClickListeners()
                selectPackage(firstMonthPackage, "firstMonth")
            } else {
                Log.e("MembershipActivity", "初始化视图失败")
                Toast.makeText(this, "页面加载失败", Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (e: Exception) {
            Log.e("MembershipActivity", "onCreate异常: ${e.message}", e)
            Toast.makeText(this, "页面初始化失败: ${e.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private fun initViews(): Boolean {
        return try {
            backButton = findViewById(R.id.backButton)
            monthlyPackage = findViewById(R.id.monthlyPackage)
            yearlyPackage = findViewById(R.id.yearlyPackage)
            firstMonthPackage = findViewById(R.id.firstMonthPackage)
            regularYearlyPackage = findViewById(R.id.regularYearlyPackage)
            agreeCheckbox = findViewById(R.id.agreeCheckbox)
            subscribeButton = findViewById(R.id.subscribeButton)
            subscribeButtonText = findViewById(R.id.subscribeButtonText)
            
            // 检查关键视图是否找到
            val allViewsFound = backButton != null && 
                              monthlyPackage != null && 
                              yearlyPackage != null && 
                              firstMonthPackage != null && 
                              regularYearlyPackage != null && 
                              agreeCheckbox != null && 
                              subscribeButton != null &&
                              subscribeButtonText != null
            
            if (!allViewsFound) {
                Log.e("MembershipActivity", "某些视图未找到")
                Log.e("MembershipActivity", "backButton: $backButton")
                Log.e("MembershipActivity", "monthlyPackage: $monthlyPackage")
                Log.e("MembershipActivity", "yearlyPackage: $yearlyPackage")
                Log.e("MembershipActivity", "firstMonthPackage: $firstMonthPackage")
                Log.e("MembershipActivity", "regularYearlyPackage: $regularYearlyPackage")
                Log.e("MembershipActivity", "agreeCheckbox: $agreeCheckbox")
                Log.e("MembershipActivity", "subscribeButton: $subscribeButton")
                Log.e("MembershipActivity", "subscribeButtonText: $subscribeButtonText")
            }
            
            allViewsFound
        } catch (e: Exception) {
            Log.e("MembershipActivity", "initViews异常: ${e.message}", e)
            false
        }
    }
    
    private fun setupClickListeners() {
        try {
            backButton?.setOnClickListener {
                finish()
            }
            
            monthlyPackage?.setOnClickListener {
                selectPackage(monthlyPackage, "monthly")
            }
            
            yearlyPackage?.setOnClickListener {
                selectPackage(yearlyPackage, "yearly")
            }
            
            firstMonthPackage?.setOnClickListener {
                selectPackage(firstMonthPackage, "firstMonth")
            }
            
            regularYearlyPackage?.setOnClickListener {
                selectPackage(regularYearlyPackage, "regularYearly")
            }
            
            subscribeButton?.setOnClickListener {
                if (agreeCheckbox?.isChecked == true) {
                    handleSubscription()
                } else {
                    Toast.makeText(this, "请先阅读并同意会员协议", Toast.LENGTH_SHORT).show()
                }
            }
            
            findViewById<TextView>(R.id.memberAgreement)?.setOnClickListener {
                // 打开会员协议页面
                Toast.makeText(this, "会员协议页面", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("MembershipActivity", "setupClickListeners异常: ${e.message}", e)
        }
    }
    
    private fun selectPackage(packageView: LinearLayout?, packageType: String) {
        try {
            if (packageView == null) {
                Log.w("MembershipActivity", "packageView为空，无法选择套餐")
                return
            }
            
            // 重置所有包的选中状态
            resetPackageSelection()
            
            // 设置选中状态
            currentSelectedPackage = packageView
            selectedPackage = packageType
            packageView.setBackgroundResource(R.drawable.selected_package_background)
            
            // 更新订阅按钮价格
            updateSubscribeButtonPrice()
        } catch (e: Exception) {
            Log.e("MembershipActivity", "selectPackage异常: ${e.message}", e)
        }
    }
    
    private fun resetPackageSelection() {
        try {
            monthlyPackage?.setBackgroundResource(R.drawable.unselected_package_background)
            yearlyPackage?.setBackgroundResource(R.drawable.unselected_package_background)
            firstMonthPackage?.setBackgroundResource(R.drawable.unselected_package_background)
            regularYearlyPackage?.setBackgroundResource(R.drawable.unselected_package_background)
        } catch (e: Exception) {
            Log.e("MembershipActivity", "resetPackageSelection异常: ${e.message}", e)
        }
    }
    
    private fun updateSubscribeButtonPrice() {
        try {
            val price = packagePrices[selectedPackage] ?: "¥9"
            subscribeButtonText?.text = price
        } catch (e: Exception) {
            Log.e("MembershipActivity", "updateSubscribeButtonPrice异常: ${e.message}", e)
        }
    }
    
    private fun handleSubscription() {
        try {
            val packageName = when(selectedPackage) {
                "monthly" -> "月卡"
                "yearly" -> "连续包年"
                "firstMonth" -> "连续包月首月"
                "regularYearly" -> "年卡"
                else -> "未知套餐"
            }
            
            val price = packagePrices[selectedPackage] ?: "¥0"
            
            Toast.makeText(this, "正在购买 $packageName ($price)...", Toast.LENGTH_LONG).show()
            
            // 这里应该调用支付SDK进行实际的支付处理
            // 模拟支付成功
            simulatePaymentSuccess()
        } catch (e: Exception) {
            Log.e("MembershipActivity", "handleSubscription异常: ${e.message}", e)
            Toast.makeText(this, "订阅失败，请重试", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun simulatePaymentSuccess() {
        try {
            Toast.makeText(this, "订阅成功！欢迎成为VIP会员", Toast.LENGTH_LONG).show()
            
            // 返回主页面
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("MembershipActivity", "simulatePaymentSuccess异常: ${e.message}", e)
            finish()
        }
    }
} 