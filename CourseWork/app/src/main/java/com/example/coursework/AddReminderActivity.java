package com.example.coursework;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity {

    private EditText courseNameInput, reminderTimeInput;
    private Button addReminderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        courseNameInput = findViewById(R.id.courseNameInput);
        reminderTimeInput = findViewById(R.id.reminderTimeInput);
        addReminderButton = findViewById(R.id.addReminderButton);

        // 设置时间选择器
        reminderTimeInput.setOnClickListener(v -> showTimePicker());

        // 设置添加提醒按钮点击事件
        addReminderButton.setOnClickListener(v -> addReminder());
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    reminderTimeInput.setText(formattedTime);
                },
                hour, minute, true);
        timePickerDialog.show();
    }

    private void addReminder() {
        String courseName = courseNameInput.getText().toString().trim();
        String reminderTime = reminderTimeInput.getText().toString().trim();


        if (courseName.isEmpty() || reminderTime.isEmpty()) {
            Toast.makeText(this, "课程名称和提醒时间不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        Reminder reminder = new Reminder(courseName, reminderTime, ReminderFragment.reminderType);
        ReminderManager.getInstance().addReminder(reminder);

        // 设置定时提醒
        setReminderAlarm(courseName, reminder);

        Toast.makeText(this, "提醒已添加！", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 设置提醒闹钟。
     *
     * @param courseName 课程名称
     * @param reminder   Reminder 对象
     */
    @SuppressLint("ScheduleExactAlarm")
    private void setReminderAlarm(String courseName, Reminder reminder) {
        long reminderTimestamp = reminder.getReminderTimestamp();
        if (reminderTimestamp > 0) {
            long adjustedTimestamp ;

            // 根据提醒类型调整时间
            if (ReminderFragment.reminderType == ReminderType.HOMEWORK) {
                adjustedTimestamp = reminderTimestamp -  60 * 60 * 1000; // 提前 1 小时
            } else {
                adjustedTimestamp = reminderTimestamp - 20 * 60 * 1000; // 提前 20 分钟
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(adjustedTimestamp);

            Intent intent = new Intent(this, ReminderReceiver.class);
            intent.putExtra("courseName", courseName);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    (int) System.currentTimeMillis(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
