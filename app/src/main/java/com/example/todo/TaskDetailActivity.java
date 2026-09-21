package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "extra_task_id";

    private DBHelper dbHelper;
    private int taskId = -1;
    private TextView textTaskName;
    private TextView textDescription;
    private TextView textDueDate;
    private TextView textStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        dbHelper = new DBHelper(this);
        taskId = getIntent().getIntExtra(EXTRA_TASK_ID, -1);

        textTaskName = findViewById(R.id.textTaskName);
        textDescription = findViewById(R.id.textDescription);
        textDueDate = findViewById(R.id.textDueDate);
        textStatus = findViewById(R.id.textStatus);
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        Button buttonEdit = findViewById(R.id.buttonEdit);
        Button buttonDelete = findViewById(R.id.buttonDelete);

        buttonBack.setOnClickListener(v -> finish());
        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTaskActivity.class);
            intent.putExtra(AddEditTaskActivity.EXTRA_TASK_ID, taskId);
            startActivity(intent);
        });
        buttonDelete.setOnClickListener(v -> confirmDelete());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTask();
    }

    private void loadTask() {
        Task task = dbHelper.getTaskById(taskId);
        if (task == null) {
            finish();
            return;
        }

        textTaskName.setText(task.getName());

        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            textDescription.setText(R.string.no_description);
        } else {
            textDescription.setText(task.getDescription());
        }

        if (task.getDueDate() == null || task.getDueDate().isEmpty()) {
            textDueDate.setText(R.string.no_due_date);
        } else {
            textDueDate.setText(task.getDueDate());
        }

        if (task.isDone()) {
            textStatus.setText(R.string.done);
            textStatus.setTextColor(getColor(R.color.done_green));
            textStatus.setBackgroundResource(R.drawable.bg_status_done);
        } else {
            textStatus.setText(R.string.todo);
            textStatus.setTextColor(getColor(R.color.todo_gray));
            textStatus.setBackgroundResource(R.drawable.bg_status_todo);
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    dbHelper.deleteTask(taskId);
                    Toast.makeText(this, R.string.task_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
