package com.nhatle.notificationkotlin

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NotificationDetailActivity : AppCompatActivity() {
    private lateinit var tv_title: TextView
    private lateinit var tv_content: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tv_title = findViewById(R.id.tv_title)
        tv_content = findViewById(R.id.tv_content)
        tv_content.text = intent.getStringExtra(EXTRA_TEXT)
    }

    companion object{
        const val EXTRA_TEXT = "com.nhatle.notificationkotlin.EXTRA_TEXT"

    }
}