package com.nhatle.notificationkotlin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private var notificationId: Int = 0
    private lateinit var containerLayout: View
    private lateinit var builder: NotificationCompat.Builder
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        // Đã cấp quyền để hiển thị thông báo
        if (it) {
            Sendnotification()
        } else {
            // Hiển thị thông báo cho người dùng biết rằng ứng dụng sẽ không hiển thị thông báo sử dụng snackbar
            Snackbar.make(
                containerLayout,
                getString(R.string.txt_permission_deny),
                Snackbar.LENGTH_SHORT
            ).show()
            // TODO: Inform user that your app will not show notifications.
        }
    }

    @SuppressLint("MissingPermission")
    private fun Sendnotification() {
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId++, builder.build())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.btn_send_notification).setOnClickListener {
            createNotification()
        }
        createNotificationChannel()
        containerLayout = findViewById(R.id.main)
    }

    fun createNotification() {
        // Bấm vào thì mở activity mới
        val notifyContent = getString(R.string.content_notification) + " " + Ultils.currentDateTime()
        val intent = Intent(this, NotificationDetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(NotificationDetailActivity.EXTRA_TEXT, notifyContent)
        }
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, notificationId++, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, notificationId++, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.title_notification))
            .setContentText(notifyContent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line...")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    baseContext,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    Snackbar.make(
                        containerLayout, R.string.notify_permission_require,
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        requestPermissionLauncher
                            .launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
                return
            }
            notify(notificationId++, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "com.nhatle.notificationkotlin.learn_notification"
    }
}
