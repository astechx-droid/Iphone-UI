package com.example.iosui

import android.os.Bundle
import android.widget.SeekBar
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val styleSwitch = findViewById<Switch>(R.id.style_switch)
        val heightSeekBar = findViewById<SeekBar>(R.id.height_seekbar)
        val curveSeekBar = findViewById<SeekBar>(R.id.curve_seekbar)

        styleSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Send broadcast to Service to change style
            // 0 = Pill, 1 = Notch
        }

        heightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update Y position
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
