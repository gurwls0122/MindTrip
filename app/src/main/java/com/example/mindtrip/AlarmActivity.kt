package com.example.mindtrip

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.media.app.NotificationCompat
import com.example.mindtrip.databinding.ActivityAlarmBinding
import com.example.mindtrip.databinding.MypickerdlgBinding
import java.util.*

class AlarmActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    lateinit var binding: ActivityAlarmBinding
    var message = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initLayout()
        initLayout2()
    }

    private  fun initLayout2(){
        binding.calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val dlgBinding = MypickerdlgBinding.inflate(layoutInflater)
            val dlgBuilder = AlertDialog.Builder(this)
                dlgBuilder.setView(dlgBinding.root)
                    .setPositiveButton("추가"){
                        _,_ ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            message = i2.toString() +"월 "+i3.toString()+"일 "+ dlgBinding.timePicker.hour.toString() +"시 "+dlgBinding.timePicker.minute.toString()+"분."+ dlgBinding.msg.text.toString()+"님에게 먼저 연락해보는건 어떠세요"
                        }
                        val timerTask = object : TimerTask() {
                            override fun run() {
                                makeNotification()
                            }
                        }
                        val timer = Timer()
                        timer.schedule(timerTask, 2000)
                        Toast.makeText(this, "알림이 추가됨",Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("취소"){
                        _,_->
                    }
                    .show()
        }
    }

    private fun initLayout(){
        binding.calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timePicker = TimePickerDialog(this, this, hour, minute,true )
            timePicker.show()
        }
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
      if(p0!=null){
          message = p1.toString() +" 시 "+p2.toString()+"분. 지인에게 먼저 연락해보는건 어떠세요"
          val timerTask = object : TimerTask() {
              override fun run() {
                  makeNotification()
              }
          }
            val timer = Timer()
          timer.schedule(timerTask, 2000)
           Toast.makeText(this, "알림이 추가됨",Toast.LENGTH_SHORT).show()
      }
    }

    fun makeNotification(){
        val id = "MyCall"
        val name = "TimeCheckChannel"
        val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val builder = Notification.Builder(this,id)
            .setSmallIcon(R.drawable.ic_baseline_contact_phone_24)
            .setContentTitle("전화 알람")
            .setContentText(message)
            .setAutoCancel(true)

        val intent = Intent(this, ContactsActivity::class.java)
        intent.putExtra("time",message)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(pendingIntent)

        val notification = builder.build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        manager.notify(10, notification)
    }

}