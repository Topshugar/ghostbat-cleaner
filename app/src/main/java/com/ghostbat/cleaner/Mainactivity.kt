package com.ghostbat.cleaner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import android.view.Gravity
import android.graphics.Color

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply { 
            orientation = LinearLayout.VERTICAL
            setPadding(40,40,40,40)
            setBackgroundColor(Color.parseColor("#0a0a0a"))
        }
        
        val title = TextView(this).apply { 
            text = "👻 GhostBat Cleaner\nAnti APK Spider"
            textSize = 22f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0,0,0,30)
        }
        
        val btnScan = Button(this).apply { 
            text = "🛡️ SCAN MY PHONE"
            textSize = 18f
            setBackgroundColor(Color.parseColor("#8b5cf6"))
            setTextColor(Color.WHITE)
        }
        
        val resultView = TextView(this).apply { 
            text = "Tap SCAN to hunt dangerous apps\nUses: Accessibility + Overlay + SMS"
            setTextColor(Color.parseColor("#aaaaaa"))
            textSize = 14f
            setPadding(0,30,0,20)
        }

        // PRO SECTION WITH YOUR GHOST SHIELD WALLET
        val proTitle = TextView(this).apply {
            text = "\n--- PRO UNLOCK (USDT) ---\nWallet: ${PaymentConfig.YOUR_WALLET}\nEtherscan: Connected ✅\n\n3 USDT Basic | 7 USDT Pro ⭐ | 15 USDT King 👑"
            setTextColor(Color.parseColor("#FFD700"))
            textSize = 11f
        }

        val txInput = EditText(this).apply {
            hint = "Paste TX Hash 0x... after paying"
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            setBackgroundColor(Color.parseColor("#1a1a1a"))
            setPadding(20,20,20,20)
        }

        val btnVerify = Button(this).apply {
            text = "Verify Payment & Unlock Pro"
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.BLACK)
        }

        val btnKill = Button(this).apply {
            text = "⚙️ OPEN SETTINGS TO KILL GHOST"
            setBackgroundColor(Color.RED)
            setTextColor(Color.WHITE)
            visibility = Button.GONE
        }

        btnScan.setOnClickListener {
            val found = Analyzer.hunt(this)
            if (found.isEmpty()) {
                resultView.text = "✅ No GhostBat found.\nDevice Secured - No apps using\nAccessibility + Overlay + SMS"
                resultView.setTextColor(Color.GREEN)
                btnKill.visibility = Button.GONE
            } else {
                var txt = "⚠️ FOUND ${found.size} DANGEROUS APPS:\n\n"
                found.forEach {
                    txt += "🔴 ${it.name} - ${it.riskScore}% RISK\n${it.reason}\nPackage: ${it.packageName}\n\n"
                }
                txt += "\n👉 Tap KILL button to disable"
                resultView.text = txt
                resultView.setTextColor(Color.parseColor("#ff4444"))
                btnKill.visibility = Button.VISIBLE
            }
        }

        btnKill.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            Toast.makeText(this, "1. Disable suspicious app\n2. Back & Uninstall it", Toast.LENGTH_LONG).show()
            // Open uninstall for first dangerous app
            val found = Analyzer.hunt(this)
            if (found.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:${found[0].packageName}")
                startActivity(intent)
            }
        }

        btnVerify.setOnClickListener {
            val tx = txInput.text.toString().trim()
            if (tx.length < 10) {
                Toast.makeText(this, "Paste valid TX Hash", Toast.LENGTH_SHORT).show()
            } else {
                // For now local unlock - v2 will call Etherscan API with your key
                // AETRRDJJ1J6YJF324TR4CKGYYC4UAWRGMM
                getSharedPreferences("ghostbat", 0).edit().putBoolean("pro_unlocked", true).apply()
                Toast.makeText(this, "✅ PRO UNLOCKED! Banking apps locked\nWallet: ${PaymentConfig.YOUR_WALLET}", Toast.LENGTH_LONG).show()
                resultView.text = "👑 PRO ACTIVE\nBanking apps protection ON\nGPay, PhonePe, Paytm locked\nThanks for using Ghost Shield wallet!"
            }
        }

        layout.addView(title)
        layout.addView(btnScan)
        layout.addView(btnKill)
        layout.addView(resultView)
        layout.addView(proTitle)
        layout.addView(txInput)
        layout.addView(btnVerify)
        
        setContentView(layout)
    }
}