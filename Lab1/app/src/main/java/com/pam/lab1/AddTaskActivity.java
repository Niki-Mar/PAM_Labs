package com.pam.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText editTaskTitle = findViewById(R.id.edit_task_title);
        Button buttonSave = findViewById(R.id.button_save);

        buttonSave.setOnClickListener(v -> {
            String taskText = editTaskTitle.getText().toString().trim();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("task_text", taskText);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
