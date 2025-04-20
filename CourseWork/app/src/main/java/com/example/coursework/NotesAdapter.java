package com.example.coursework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * NotesAdapter 用于管理笔记列表的适配器。
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private final ArrayList<String> notesList;

    public NotesAdapter(ArrayList<String> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载单个笔记项的布局
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        // 设置笔记内容
        String note = notesList.get(position);
        holder.noteTextView.setText(note);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.noteTextView);
        }
    }
}
