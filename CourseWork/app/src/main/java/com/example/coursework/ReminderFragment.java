package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

enum ReminderType {
    SIGN_IN,  // 签到提醒
    HOMEWORK  // 作业提醒
}
/**
 * ReminderFragment 负责显示提醒列表，并跳转到添加提醒页面。
 */
public class ReminderFragment extends Fragment {

    private RecyclerView reminderRecyclerView;
    private ReminderAdapter reminderAdapter;
    public static ReminderType reminderType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        // 初始化 RecyclerView
        reminderRecyclerView = view.findViewById(R.id.reminderRecyclerView);
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 设置适配器
        ReminderManager reminderManager = ReminderManager.getInstance();
        List<Reminder> reminders = reminderManager.getReminders();
        reminderAdapter = new ReminderAdapter(reminders);
        reminderRecyclerView.setAdapter(reminderAdapter);

        // 设置添加提醒按钮
        view.findViewById(R.id.addReminderButton).setOnClickListener(v -> {
            reminderType = ReminderType.SIGN_IN;
            Intent intent = new Intent(getActivity(), AddReminderActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.courseHomeworkReminderButton).setOnClickListener(v -> {
            reminderType = ReminderType.HOMEWORK;
            Intent intent = new Intent(getActivity(), AddReminderActivity.class);
            startActivity(intent);
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 更新提醒列表
        reminderAdapter.notifyDataSetChanged();
    }
}
