package com.example.coursework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.InputStream;

public class SettingsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button changeBackgroundButton = view.findViewById(R.id.changeBackgroundButton);
        changeBackgroundButton.setOnClickListener(v -> openGallery());


        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Apply background when the fragment is resumed
        applyBackground();
    }

    /**
     * 打开相册选择图片
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * 处理图片选择结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            saveBackgroundPreference(imageUri.toString()); // 保存背景图片路径
            applyBackground(); // 应用背景
        }
    }

    /**
     * 保存背景图片路径到 SharedPreferences
     *
     * @param imageUri 图片的 Uri 路径
     */
    private void saveBackgroundPreference(String imageUri) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("background_image_uri", imageUri);
        editor.apply();
    }

    /**
     * 应用背景图片
     */
    public void applyBackground() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String imageUri = sharedPreferences.getString("background_image_uri", null);

        if (imageUri != null) {
            try {
                Uri uri = Uri.parse(imageUri);
                InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    View rootView = requireActivity().getWindow().getDecorView();
                    Drawable background = new BitmapDrawable(getResources(), bitmap);
                    rootView.setBackground(background);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void logout() {
        // 清除用户登录信息
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // 清除所有数据
        editor.apply();

        // 跳转到登录页面
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // 结束当前活动栈
        requireActivity().finish();
    }
}
