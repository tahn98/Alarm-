package com.tahn.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
class AlarmReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, RingtonePlayingService::class.java)
        serviceIntent.putExtra(Const.EXTRA, intent?.extras?.getString(Const.EXTRA))
        context?.startService(serviceIntent)
    }
}