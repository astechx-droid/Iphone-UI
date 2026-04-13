package com.example.iosui

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val iconMap = HashMap<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup Icon Mapping
        iconMap["com.android.chrome"] = R.drawable.safari_icon
        iconMap["com.google.android.googlequicksearchbox"] = R.drawable.appstore_icon

        recyclerView = findViewById(R.id.app_grid)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        
        loadApps()
    }

    private fun loadApps() {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = packageManager.queryIntentActivities(mainIntent, 0)
        recyclerView.adapter = AppAdapter(apps)
    }

    inner class AppAdapter(private val apps: List<ResolveInfo>) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val app = apps[position]
            val packageName = app.activityInfo.packageName
            
            // Check for custom iOS icon mapping
            if (iconMap.containsKey(packageName)) {
                holder.icon.setImageResource(iconMap[packageName]!!)
                holder.label.text = if (packageName == "com.android.chrome") "Safari" else "App Store"
            } else {
                holder.icon.setImageDrawable(app.loadIcon(packageManager))
                holder.label.text = app.loadLabel(packageManager)
            }

            holder.itemView.setOnClickListener {
                val intent = packageManager.getLaunchIntentForPackage(app.activityInfo.packageName)
                startActivity(intent)
            }
        }

        override fun getItemCount() = apps.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val icon: ImageView = view.findViewById(R.id.app_icon)
            val label: TextView = view.findViewById(R.id.app_label)
        }
    }
}
