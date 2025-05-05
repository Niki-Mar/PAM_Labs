package com.pam.lab1;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Main activity that shows the task list and handles adding/removing
public class MainActivity extends AppCompatActivity {

    private ArrayList<String> tasks;
    private TaskAdapter adapter;
    private ActivityResultLauncher<Intent> addTaskLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.task_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTasks();

        adapter = new TaskAdapter(this, tasks,
                position -> {
                    tasks.remove(position);
                    adapter.notifyItemRemoved(position);
                    saveTasks();
                },
                () -> {
                    Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                    addTaskLauncher.launch(intent);
                });

        recyclerView.setAdapter(adapter);

        addTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String taskText = data.getStringExtra("task_text");
                            tasks.add(taskText);
                            adapter.notifyItemInserted(tasks.size() - 1);
                            saveTasks();
                        }
                    }
                }
        );
    }

    private void saveTasks() {
        SharedPreferences prefs = getSharedPreferences("MyTasks", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(tasks);

        editor.putString("task_list", json);
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences prefs = getSharedPreferences("MyTasks", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("task_list", null);

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        tasks = gson.fromJson(json, type);

        if (tasks == null) {
            tasks = new ArrayList<>();
        }
    }

}
