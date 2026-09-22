package com.example.todo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {

    // Key constant for passing task ID via Intent
    public static final String EXTRA_TASK_ID = "extra_task_id";

    // UI elements and helper declarations
    private EditText editTaskName;
    private EditText editDescription;
    private TextView textDueDate;
    private CheckBox checkBoxDone;
    private DBHelper dbHelper;
    private int taskId = -1;
    private String selectedDueDate = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Initialize database helper and find views by ID
        dbHelper = new DBHelper(this);
        editTaskName = findViewById(R.id.editTaskName);
        editDescription = findViewById(R.id.editDescription);
        textDueDate = findViewById(R.id.textDueDate);
        checkBoxDone = findViewById(R.id.checkBoxDone);
        Button buttonSave = findViewById(R.id.buttonSave);
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        TextView textTitle = findViewById(R.id.textTitle);

        // Get task ID from Intent extras (-1 indicates new task)
        taskId = getIntent().getIntExtra(EXTRA_TASK_ID, -1);
        if (taskId != -1) {
            textTitle.setText(R.string.edit_task);
            loadTask();
        }

        // Set click listeners
        buttonBack.setOnClickListener(v -> finish());
        textDueDate.setOnClickListener(v -> showDatePicker());
        buttonSave.setOnClickListener(v -> saveTask());
    }

    // Populate UI fields with existing task details from database
    private void loadTask() {
        Task task = dbHelper.getTaskById(taskId);
        if (task == null) {
            finish();
            return;
        }
        editTaskName.setText(task.getName());
        editDescription.setText(task.getDescription() == null ? "" : task.getDescription());
        selectedDueDate = task.getDueDate() == null ? "" : task.getDueDate();
        if (!selectedDueDate.isEmpty()) {
            textDueDate.setText(selectedDueDate);
        }
        checkBoxDone.setChecked(task.isDone());
    }

    // Display DatePickerDialog to let user pick a due date
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // Format selected date as YYYY-MM-DD
                    selectedDueDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    textDueDate.setText(selectedDueDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    // Validate inputs and save task to database (Insert or Update)
    private void saveTask() {
        String name = editTaskName.getText().toString().trim();
        // Validate required task name field
        if (name.isEmpty()) {
            editTaskName.setError(getString(R.string.task_name_required));
            editTaskName.requestFocus();
            return;
        }

        String description = editDescription.getText().toString().trim();
        int status = checkBoxDone.isChecked() ? Task.STATUS_DONE : Task.STATUS_TODO;
        Task task = new Task(name, description, status, selectedDueDate);
        // Perform insert if new task, or update if existing task
        if (taskId == -1) {
            dbHelper.insertTask(task);
        } else {
            task.setId(taskId);
            dbHelper.updateTask(task);
        }

        // Show confirmation toast and close screen
        Toast.makeText(this, R.string.task_saved, Toast.LENGTH_SHORT).show();
        finish();
    }
}
