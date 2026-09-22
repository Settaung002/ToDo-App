package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import com.example.todo.Task;

public class DBHelper extends SQLiteOpenHelper {

    // Constructor to pass context, database name, and version to super class
    public DBHelper(Context context){
        super(context, TaskContract.DATABASE_NAME, null, TaskContract.DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement string to create the tasks table
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " ("
                + TaskContract.TaskEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskContract.TaskEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_DESCRIPTION + " TEXT, "
                + TaskContract.TaskEntry.COLUMN_STATUS + " INTEGER DEFAULT 0, "
                + TaskContract.TaskEntry.COLUMN_DUE_DATE + " TEXT"
                + ")";

        db.execSQL(createTable);
    }

    // Called when the database needs to be upgraded to a new version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }

    // Insert a new task into the database
    public long insertTask(Task task) {
        // Get writable database instance
        SQLiteDatabase db = getWritableDatabase();
        // Insert record and return row ID
        return db.insert(TaskContract.TaskEntry.TABLE_NAME, null,
                getContentValues(task));
    }

    // Update an existing task in the database
    public boolean updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();

        int result;
        // Update task where ID matches task.getId()
        result = db.update(
                TaskContract.TaskEntry.TABLE_NAME,
                getContentValues(task),
                TaskContract.TaskEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(task.getId())}
        );

        return result > 0;
    }

    // Update only the status of a task (done or pending)
    public boolean updateTaskStatus(int taskId, int status) {
        SQLiteDatabase db = getWritableDatabase();

        // Put only the new status into values
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_STATUS, status);

        // Update database row
        int result = db.update(
                TaskContract.TaskEntry.TABLE_NAME,
                values,
                TaskContract.TaskEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(taskId)}
        );

        return result > 0;
    }

    // Delete a task by ID
    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = getWritableDatabase();

        // Delete row where ID matches
        int result = db.delete(
                TaskContract.TaskEntry.TABLE_NAME,
                TaskContract.TaskEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(taskId)}
        );

        return result > 0;
    }

    // Query a single task by ID
    public Task getTaskById(int taskId) {
        SQLiteDatabase db = getReadableDatabase();

        // Fetch task matching ID
        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                TaskContract.TaskEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null
        );

        Task task = null;
        // If data exists, map cursor to Task object
        if (cursor.moveToFirst()) {
            task = getTaskFromCursor(cursor);
        }

        cursor.close();
        return task;
    }

    // Fetch all tasks from the database
    public List<com.example.todo.Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_STATUS + " ASC, "
                        + TaskContract.TaskEntry.COLUMN_ID + " DESC"
        );

        // Iterate through all cursor rows and add to list
        while (cursor.moveToNext()) {
            tasks.add(getTaskFromCursor(cursor));
        }

        cursor.close();
        return tasks;
    }
    // Helper method to convert a Task object into ContentValues
    private ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME, task.getName());
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskContract.TaskEntry.COLUMN_STATUS, task.getStatus());
        values.put(TaskContract.TaskEntry.COLUMN_DUE_DATE, task.getDueDate());
        return values;
    }

    // Helper method to parse a Cursor row into a Task object
    private Task getTaskFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow
                (TaskContract.TaskEntry.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow
                (TaskContract.TaskEntry.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow
                (TaskContract.TaskEntry.COLUMN_DESCRIPTION));
        int status = cursor.getInt(cursor.getColumnIndexOrThrow
                (TaskContract.TaskEntry.COLUMN_STATUS));
        String dueDate = cursor.getString(cursor.getColumnIndexOrThrow
                (TaskContract.TaskEntry.COLUMN_DUE_DATE));

        return new Task(id, name, description, status, dueDate);
    }
}