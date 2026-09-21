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

    public DBHelper(Context context){
        super(context, TaskContract.DATABASE_NAME, null, TaskContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " ("
                + TaskContract.TaskEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskContract.TaskEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_DESCRIPTION + " TEXT, "
                + TaskContract.TaskEntry.COLUMN_STATUS + " INTEGER DEFAULT 0, "
                + TaskContract.TaskEntry.COLUMN_DUE_DATE + " TEXT"
                + ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }

    public long insertTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TaskContract.TaskEntry.TABLE_NAME, null,
                getContentValues(task));
    }

    public boolean updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();

        int result;
        result = db.update(
                TaskContract.TaskEntry.TABLE_NAME,
                getContentValues(task),
                TaskContract.TaskEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(task.getId())}
        );

        return result > 0;
    }

    public boolean updateTaskStatus(int taskId, int status) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_STATUS, status);

        int result = db.update(
                TaskContract.TaskEntry.TABLE_NAME,
                values,
                TaskContract.TaskEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(taskId)}
        );

        return result > 0;
    }

    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(
                TaskContract.TaskEntry.TABLE_NAME,
                TaskContract.TaskEntry.COLUMN_ID + "=?",
                new String[]{String.valueOf(taskId)}
        );

        return result > 0;
    }

    public Task getTaskById(int taskId) {
        SQLiteDatabase db = getReadableDatabase();

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

        if (cursor.moveToFirst()) {
            task = getTaskFromCursor(cursor);
        }

        cursor.close();
        return task;
    }

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

        while (cursor.moveToNext()) {
            tasks.add(getTaskFromCursor(cursor));
        }

        cursor.close();
        return tasks;
    }
    private ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME, task.getName());
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskContract.TaskEntry.COLUMN_STATUS, task.getStatus());
        values.put(TaskContract.TaskEntry.COLUMN_DUE_DATE, task.getDueDate());
        return values;
    }

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