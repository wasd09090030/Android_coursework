package com.example.coursework;

import android.content.Context;
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
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NotesFragment extends Fragment {

    private static final String PREFS_NAME = "NotesPreferences";
    private static final String NOTES_KEY = "notes";

    private EditText notesInput;
    private Button addNoteButton;
    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private ArrayList<String> notes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        applyBackground();

        notesInput = view.findViewById(R.id.notesInput);
        addNoteButton = view.findViewById(R.id.addNoteButton);
        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);

        // 初始化笔记列表并加载数据
        notes = loadNotes();
        notesAdapter = new NotesAdapter(notes);

        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notesRecyclerView.setAdapter(notesAdapter);

        // 添加笔记按钮监听
        addNoteButton.setOnClickListener(v -> addNote());

        return view;
    }

    private void addNote() {
        String noteText = notesInput.getText().toString().trim();
        if (!noteText.isEmpty()) {
            notes.add(noteText);
            notesAdapter.notifyDataSetChanged();
            notesInput.setText("");

            // 保存笔记
            saveNotes();
        }
    }

    private void saveNotes() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> notesSet = new HashSet<>(notes); // 将 ArrayList 转为 Set
        editor.putStringSet(NOTES_KEY, notesSet);
        editor.apply();
    }

    private ArrayList<String> loadNotes() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> notesSet = sharedPreferences.getStringSet(NOTES_KEY, new HashSet<>());
        return new ArrayList<>(notesSet); // 将 Set 转为 ArrayList
    }

    private void applyBackground() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String imageUri = sharedPreferences.getString("background_image_uri", null);

        if (imageUri != null) {
            try {
                Uri uri = Uri.parse(imageUri);
                InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                View rootView = requireActivity().getWindow().getDecorView();
                Drawable background = new BitmapDrawable(getResources(), bitmap);
                rootView.setBackground(background);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
