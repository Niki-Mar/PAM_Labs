package com.pam.lab1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<String> tasks;
    private final OnTaskDeleteListener deleteListener;
    private final OnAddTaskClickListener addClickListener;

    private static final int TYPE_TASK = 0;
    private static final int TYPE_ADD_BUTTON = 1;

    public TaskAdapter(Context context, ArrayList<String> tasks,
                       OnTaskDeleteListener deleteListener,
                       OnAddTaskClickListener addClickListener) {
        this.context = context;
        this.tasks = tasks;
        this.deleteListener = deleteListener;
        this.addClickListener = addClickListener;
    }

    public interface OnTaskDeleteListener {
        void onTaskDelete(int position);
    }

    public interface OnAddTaskClickListener {
        void onAddClick();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskText;
        ImageButton searchButton;
        ImageButton deleteButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskText = itemView.findViewById(R.id.task_text);
            deleteButton = itemView.findViewById(R.id.button_delete);
            searchButton = itemView.findViewById(R.id.button_search);
        }
    }

    public static class AddButtonViewHolder extends RecyclerView.ViewHolder {
        Button addButton;

        public AddButtonViewHolder(View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.button_add_task);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == tasks.size()) ? TYPE_ADD_BUTTON : TYPE_TASK;
    }

    @Override
    public int getItemCount() {
        return tasks.size() + 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TASK) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_button, parent, false);
            return new AddButtonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_TASK) {
            TaskViewHolder taskHolder = (TaskViewHolder) holder;
            String task = tasks.get(position);
            taskHolder.taskText.setText(task);

            taskHolder.searchButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/search?q=" + Uri.encode(task)));
                context.startActivity(intent);
            });

            taskHolder.deleteButton.setOnClickListener(v -> new AlertDialog.Builder(context)
                    .setTitle("Delete??")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Delete", (dialog, which) -> deleteListener.onTaskDelete(position))
                    .setNegativeButton("Cancel", null)
                    .show());

        } else {
            AddButtonViewHolder addHolder = (AddButtonViewHolder) holder;
            addHolder.addButton.setOnClickListener(v -> addClickListener.onAddClick());
        }
    }
}