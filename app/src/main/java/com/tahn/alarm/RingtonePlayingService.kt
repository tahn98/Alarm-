package com.tahn.alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi


class RingtonePlayingService : Service() {

    private var stateId : Int = -1
    private var isRunning : Boolean = false
    private var mediaPlayer : MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        // Creates a connection with a client
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Execute your operations

        buildNotification()

        var state : String? = intent?.extras?.getString(Const.EXTRA)

        stateId = when(state){
            Const.ON -> {
                1
            }
            Const.OFF -> {
                0
            }
            else -> {
                0
            }
        }

        //no music and alarm press on
        if (!isRunning && stateId == 1){
            mediaPlayer = MediaPlayer.create(this, R.raw.music)
            mediaPlayer?.start()

            isRunning = true
            stateId = 0
        }
        //music and alarm press off
        else if (!isRunning && stateId == 0){
            isRunning = false
            stateId = 0
        }
        //music and alarm press on
        else if (isRunning && stateId == 1){
            isRunning = true
            stateId = 0
        }
        //music and end sound
        else {
            mediaPlayer?.stop()
            mediaPlayer?.reset()

            isRunning = false
            stateId = 0
        }

        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buildNotification(){
        val id = "my_channel_01"
        val important = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, "name", important)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intentMain = Intent(this.applicationContext, MainActivity::class.java)
        val pendingIntentMain = PendingIntent.getActivity(this, 0, intentMain, 0)

        notificationManager.createNotificationChannel(channel)

        val notificationPopup = Notification.Builder(this.applicationContext, id)
            .setContentTitle("An Alarm is going off")
            .setContentIntent(pendingIntentMain)
            .setSmallIcon(R.drawable.notification_icon_background)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notificationPopup)
    }
}