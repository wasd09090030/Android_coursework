package com.example.coursework;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HomeFragment extends Fragment {

    private ImageView courseImageView;
    private Button uploadButton;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        courseImageView = view.findViewById(R.id.courseImageView);
        uploadButton = view.findViewById(R.id.uploadButton);


        // 加载之前保存的图片 URI
        loadImageFromPreferences();

        // 设置图片选择器
        setupImagePicker();


        // 设置按钮点击事件
        uploadButton.setOnClickListener(v -> pickImage());

        return view;
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            // 设置 ImageView 显示图片
                            courseImageView.setImageURI(imageUri);

                            // 保存 URI 到 SharedPreferences
                            saveImageUriToPreferences(imageUri);
                            Toast.makeText(getActivity(), "课表图片上传成功！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "未选择图片", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void saveImageUriToPreferences(Uri imageUri) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_image_uri", imageUri.toString());
        editor.apply();
    }

    private void loadImageFromPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String imageUriString = sharedPreferences.getString("selected_image_uri", null);

        if (imageUriString != null) {
            try {
                Uri imageUri = Uri.parse(imageUriString);

                // 检查 URI 是否有效
                if (imageUri != null && isUriValid(imageUri)) {
                    courseImageView.setImageURI(imageUri);
                } else {
                    Toast.makeText(requireContext(), "图片无效，重新选择图片", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
             //   Toast.makeText(requireContext(), "加载图片失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "没有保存的图片", Toast.LENGTH_SHORT).show();
        }
    }

    // 检查 URI 是否有效
    private boolean isUriValid(Uri uri) {
        try {
            // 使用 ContentResolver 检查文件是否可访问
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                inputStream.close();
                return true;  // 文件可访问
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // 文件不可访问
    }

}



