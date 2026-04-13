package com.example.iosui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import android.widget.TextClock
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher

class LockScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ensure the activity shows even when locked
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        
        setContentView(R.layout.activity_lock_screen)

        val pinEntry = findViewById<EditText>(R.id.pin_entry)
        pinEntry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 4) {
                    // Simple check for demo purposes
                    if (s.toString() == "1234") {
                        finish()
                    } else {
                        s.clear()
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onBackPressed() {
        // Disable back button on lock screen
    }
}
