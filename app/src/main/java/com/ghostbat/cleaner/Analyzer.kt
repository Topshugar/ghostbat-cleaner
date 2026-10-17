package com.ghostbat.cleaner

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.Settings
import android.text.TextUtils

data class RiskyApp(
    val name: String,
    val packageName: String,
    val riskScore: Int,
    val reason: String
)

object Analyzer {
    fun hunt(context: Context): List<RiskyApp> {
        val pm = context.packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        val result = mutableListOf<RiskyApp>()
        val enabledServices = Settings.Secure.getString(
            context.contentResolver, 
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: ""

        for (app in apps) {
            if (app.flags and ApplicationInfo.FLAG_SYSTEM != 0) continue
            try {
                val pkgInfo = pm.getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS)
                val perms = pkgInfo.requestedPermissions?.toList() ?: emptyList()
                val hasAccessibility = enabledServices.contains(app.packageName)
                val hasOverlay = perms.contains("android.permission.SYSTEM_ALERT_WINDOW")
                val hasSms = perms.contains("android.permission.READ_SMS") || perms.contains("android.permission.RECEIVE_SMS")
                val hasCall = perms.contains("android.permission.CALL_PHONE") || perms.contains("android.permission.READ_CALL_LOG")

                val score = when {
                    hasAccessibility && hasOverlay && hasSms -> 100
                    hasAccessibility && hasOverlay -> 85
                    hasAccessibility && hasSms -> 75
                    hasOverlay && hasSms -> 60
                    else -> 0
                }
                if (score >= 60) {
                    result.add(RiskyApp(
                        pm.getApplicationLabel(app).toString(),
                        app.packageName,
                        score,
                        "Access=${hasAccessibility}, Overlay=${hasOverlay}, SMS=${hasSms}, Call=${hasCall}"
                    ))
                }
            } catch (e: Exception) {}
        }
        return result.sortedByDescending { it.riskScore }
    }
}