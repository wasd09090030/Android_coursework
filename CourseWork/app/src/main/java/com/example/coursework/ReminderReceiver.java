package com.example.coursework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String courseName = intent.getStringExtra("courseName");

        if (courseName == null) {
            Log.e("ReminderReceiver", "接收到的课程名称为空");
            return;
        }

        if (Reminder.reminderType == ReminderType.HOMEWORK){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminderChannel")
                    .setSmallIcon(R.drawable.ic_reminder)
                    .setContentTitle("作业提醒")
                    .setContentText("该课程距离收作业还有60分钟！课程: " + courseName)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(courseName.hashCode(), builder.build());
            } else {
                Log.e("ReminderReceiver", "无法发送通知，通知权限被禁用");
            }
        }
        else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminderChannel")
                    .setSmallIcon(R.drawable.ic_reminder)
                    .setContentTitle("课程提醒")
                    .setContentText("该课程距离签到还有20分钟！课程: " + courseName)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(courseName.hashCode(), builder.build());
            } else {
                Log.e("ReminderReceiver", "无法发送通知，通知权限被禁用");
            }
        }


    }

}
