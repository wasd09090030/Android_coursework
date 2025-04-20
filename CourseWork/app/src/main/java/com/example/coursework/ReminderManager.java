package com.example.coursework;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ReminderManager 管理提醒数据。
 */
public class ReminderManager {
    private static ReminderManager instance;
    private List<Reminder> reminders;

    private ReminderManager() {
        reminders = new ArrayList<>();
    }

    public static synchronized ReminderManager getInstance() {
        if (instance == null) {
            instance = new ReminderManager();
        }
        return instance;
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
    }

    public List<Reminder> getReminders() {
        return reminders;
    }
}


class Reminder {
    private String courseName;
    private String reminderTime; // 格式为 "HH:mm"
    public static ReminderType reminderType;


    /**
     * 构造函数。
     *
     * @param courseName   课程名称
     * @param reminderTime 提醒时间，格式为 "HH:mm"
     * @param reminderType
     */
    public Reminder(String courseName, String reminderTime, ReminderType reminderType ) {
        this.courseName = courseName;
        this.reminderTime = reminderTime;
        this.reminderType=reminderType;
    }

    /**
     * 获取课程名称。
     *
     * @return 课程名称
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * 获取提醒时间。
     *
     * @return 提醒时间
     */
    public String getReminderTime() {
        return reminderTime;
    }

    /**
     * 获取提醒时间的时间戳（相对于当天）。
     *
     * @return 提醒时间的时间戳（毫秒），如果解析失败返回 -1。
     */
    public long getReminderTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date timeDate = sdf.parse(reminderTime);
            if (timeDate != null) {
                Calendar now = Calendar.getInstance();
                Calendar reminder = Calendar.getInstance();

                reminder.setTime(timeDate);
                reminder.set(Calendar.YEAR, now.get(Calendar.YEAR));
                reminder.set(Calendar.MONTH, now.get(Calendar.MONTH));
                reminder.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

                // 如果提醒时间已过，将提醒时间调整为第二天
                if (reminder.before(now)) {
                    reminder.add(Calendar.DAY_OF_MONTH, 1);
                }

                return reminder.getTimeInMillis();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 提供易读的 Reminder 描述。
     *
     * @return Reminder 的字符串描述
     */
    @Override
    public String toString() {
        return "课程名称: " + courseName + ", 提醒时间: " + reminderTime;
    }
}

