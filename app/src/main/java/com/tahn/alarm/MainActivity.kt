package com.tahn.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var alarmManager : AlarmManager
    private lateinit var timePicker : TimePicker
    private lateinit var btAlarmOn : Button
    private lateinit var btAlarmOff : Button
    private lateinit var calendar : Calendar
    private lateinit var tvTime : TextView
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViewId()
        init()
    }

    private fun init() {
        calendar = Calendar.getInstance()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        btAlarmOff.setOnClickListener(this)
        btAlarmOn.setOnClickListener(this)
    }

    private fun bindViewId() {
        timePicker = findViewById(R.id.timePicker)
        btAlarmOn = findViewById(R.id.btAlarmOn)
        btAlarmOff = findViewById(R.id.btAlarmOff)
        tvTime = findViewById(R.id.tvTime)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(view: View?) {
        val intent = Intent(this, AlarmReceiver::class.java)
        when (view?.id) {
            /**
             * set alarm manager
             * intent : tell which receiver to send signal to
             * pending intent : tell the alarm manager to send delay intent
             */
            R.id.btAlarmOn -> {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                calendar.set(Calendar.MINUTE, timePicker.minute)

                val hour = timePicker.hour
                val minute = timePicker.minute

                tvTime.text = "Alarm set at $hour : $minute"

                intent.putExtra(Const.EXTRA, Const.ON)

                //perform a broadcast
                pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
            R.id.btAlarmOff -> {
                tvTime.text = "Alarm not set yet"
                if (pendingIntent != null){
                    alarmManager.cancel(pendingIntent)
                }

                intent.putExtra(Const.EXTRA, Const.OFF)
                sendBroadcast(intent)
            }
        }
    }
}
