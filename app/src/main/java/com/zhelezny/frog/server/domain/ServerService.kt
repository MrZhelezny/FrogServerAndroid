package com.zhelezny.frog.server.domain

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.zhelezny.frog.server.domain.plugins.configureRouting
import com.zhelezny.frog.server.domain.plugins.configureSockets
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.BindException

class ServerService : Service() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.i(TAG, "CoroutineExceptionHandler got $exception")
        Toast.makeText(this, "Сервер уже запущен", Toast.LENGTH_LONG).show()
    }

    val scope = Dispatchers.IO + handler

    override fun onCreate() {
        Log.i(TAG, "MainMenuService onCreate")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Card Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
            Log.d(TAG, "onCreate: createNotificationChannel ok")
        }
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
//                .setSmallIcon(R.drawable.ic_android_24dp)
            .setContentTitle("Frog.Server")
            .setContentText("Работает").build()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
//        val notificationIntent = Intent(this, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
//
//        val notification = NotificationCompat.Builder(this, channelID)
//            .setContentTitle(foregroundServiceNotificationTitle)
//            .setContentText(input)
//            .setSmallIcon(R.drawable.ic_android_24dp)
//            .setContentIntent(pendingIntent)
//            .build()
//
//        startForeground(1, notification)
        startServer()

        return START_NOT_STICKY
    }

    private fun startServer() {

        CoroutineScope(scope).launch {
            try {
                embeddedServer(CIO, /*host = "192.168.0.108",*/ port = 11111) {
                    configureSockets()
                    configureRouting()
                }.start(wait = true)
            } catch (e: BindException) {
                Log.e(TAG, e.message, e)
//                Toast.makeText(this, "Сервер уже запущен", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG, "onBind")
        return null
    }

    companion object {
        private const val TAG = "ServerService"
        private const val NOTIFICATION_CHANNEL_ID = "com.zhelezny.card.NotificationChannel.1"
    }
}