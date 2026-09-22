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

// Activity for displaying detailed information of a single task
public class TaskDetailActivity extends AppCompatActivity {

    // Key constant for passing task ID via Intent
    public static final String EXTRA_TASK_ID = "extra_task_id";

    // UI elements and helper declarations
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

        // Initialize database helper and get task ID from Intent extras
        dbHelper = new DBHelper(this);
        taskId = getIntent().getIntExtra(EXTRA_TASK_ID, -1);

        // Bind UI views by ID
        textTaskName = findViewById(R.id.textTaskName);
        textDescription = findViewById(R.id.textDescription);
        textDueDate = findViewById(R.id.textDueDate);
        textStatus = findViewById(R.id.textStatus);
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        Button buttonEdit = findViewById(R.id.buttonEdit);
        Button buttonDelete = findViewById(R.id.buttonDelete);

        // Set click listeners for action buttons
        buttonBack.setOnClickListener(v -> finish());
        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTaskActivity.class);
            intent.putExtra(AddEditTaskActivity.EXTRA_TASK_ID, taskId);
            startActivity(intent);
        });
        buttonDelete.setOnClickListener(v -> confirmDelete());
    }

    // Reload task details whenever activity resumes to reflect changes
    @Override
    protected void onResume() {
        super.onResume();
        loadTask();
    }

    // Fetch task from database and update UI components
    private void loadTask() {
        Task task = dbHelper.getTaskById(taskId);
        if (task == null) {
            finish();
            return;
        }

        // Set task name
        textTaskName.setText(task.getName());

        // Set description or default placeholder
        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            textDescription.setText(R.string.no_description);
        } else {
            textDescription.setText(task.getDescription());
        }

        // Set due date or default placeholder
        if (task.getDueDate() == null || task.getDueDate().isEmpty()) {
            textDueDate.setText(R.string.no_due_date);
        } else {
            textDueDate.setText(task.getDueDate());
        }

        // Style status view based on task completion
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

    // Display confirmation dialog before deleting task
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
