package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity  implements TaskAdapter.TaskActionListener{

    private DBHelper dbHelper;
    private TaskAdapter adapter;
    private View emptyState;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Views များကို အရင် Bind လုပ်ပါ
        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.recyclerViewTasks);
        emptyState = findViewById(R.id.emptyState);
        fabAddTask = findViewById(R.id.fabAddTask);

        // 2. ရရှိလာသော RecyclerView ပေါ်တွင် Adapter နှင့် LayoutManager ထည့်ပါ
        adapter = new TaskAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        List<Task> tasks = dbHelper.getAllTasks();
        adapter.setTasks(tasks);

        if (tasks.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTaskClick(Task task) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, task.getId());
        startActivity(intent);
    }

    @Override
    public void onStatusChanged(Task task, boolean isDone) {
        int status = isDone ? Task.STATUS_DONE : Task.STATUS_TODO;
        dbHelper.updateTaskStatus(task.getId(), status);
        loadTasks();
    }

    @Override
    public void onEdit(Task task) {
        Intent intent = new Intent(this, AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskActivity.EXTRA_TASK_ID, task.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(Task task) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    dbHelper.deleteTask(task.getId());
                    Toast.makeText(this, R.string.task_deleted, Toast.LENGTH_SHORT).show();
                    loadTasks();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}


