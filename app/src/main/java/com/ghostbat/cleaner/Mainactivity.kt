package com.ghostbat.cleaner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(40,40,40,40) }
        val title = TextView(this).apply { text = "GhostBat Cleaner\nAnti APK Spider"; textSize = 24f }
        val btn = Button(this).apply { text = "🛡️ SCAN MY PHONE"; textSize = 20f }
        val listView = TextView(this).apply { text = "Tap scan to hunt dangerous apps" }

        btn.setOnClickListener {
            val found = Analyzer.hunt(this)
            if (found.isEmpty()) {
                listView.text = "✅ No GhostBat found.\nDevice Secured"
            } else {
                var txt = "⚠️ FOUND ${found.size} DANGEROUS APPS:\n\n"
                found.forEach {
                    txt += "🔴 ${it.name} - ${it.riskScore}% RISK\n${it.reason}\nPackage: ${it.packageName}\n[Tap to Kill]\n\n"
                }
                listView.text = txt
                // Open accessibility settings to disable
                found.firstOrNull()?.let {
                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }
            }
        }
        layout.addView(title)
        layout.addView(btn)
        layout.addView(listView)
        setContentView(layout)
    }
}