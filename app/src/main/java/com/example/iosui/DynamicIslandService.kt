package com.example.iosui

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import android.animation.ValueAnimator
import android.view.View
import android.util.TypedValue
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.TextView
import android.widget.ImageView

class DynamicIslandService : AccessibilityService() {

    private lateinit var windowManager: WindowManager
    private lateinit var islandView: View
    private lateinit var layoutParams: WindowManager.LayoutParams

    private var islandStyle = "PILL" // "PILL" or "NOTCH"
    private var offsetX = 0
    private var offsetY = 12

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                updateIsland("Charging", R.drawable.ic_battery_charging)
                expandIsland()
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        islandView = LayoutInflater.from(this).inflate(R.layout.dynamic_island_layout, null)
        
        applyStyle(islandStyle)
        windowManager.addView(islandView, layoutParams)

        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private fun applyStyle(style: String) {
        islandStyle = style
        layoutParams = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            
            width = if (style == "PILL") WindowManager.LayoutParams.WRAP_CONTENT else WindowManager.LayoutParams.MATCH_PARENT
            height = if (style == "PILL") dpToPx(36) else dpToPx(44)
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = offsetX
            y = if (style == "PILL") dpToPx(offsetY) else 0
        }

        val backgroundRes = if (style == "PILL") R.drawable.dynamic_island_background else R.drawable.classic_notch_background
        islandView.setBackgroundResource(backgroundRes)
    }

    fun updatePosition(x: Int, y: Int) {
        offsetX = x
        offsetY = y
        applyStyle(islandStyle)
        windowManager.updateViewLayout(islandView, layoutParams)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (it.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
                updateIsland("New Notification", R.drawable.appstore_icon)
                expandIsland()
            }
        }
    }

    private fun updateIsland(text: String, iconRes: Int) {
        val textView = islandView.findViewById<TextView>(R.id.island_text)
        val imageView = islandView.findViewById<ImageView>(R.id.island_icon)
        textView.text = text
        imageView.setImageResource(iconRes)
    }

    override fun onInterrupt() {}

    private fun expandIsland() {
        val startWidth = islandView.width
        val endWidth = dpToPx(300)
        
        val animator = ValueAnimator.ofInt(startWidth, endWidth)
        animator.duration = 400
        animator.addUpdateListener { animation ->
            layoutParams.width = animation.animatedValue as Int
            windowManager.updateViewLayout(islandView, layoutParams)
        }
        animator.start()
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
        windowManager.removeView(islandView)
    }
}
