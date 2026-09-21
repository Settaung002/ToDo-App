package com.example.todo;

public class TaskContract {
    private TaskContract(){
    }

    public static final String DATABASE_NAME = "ToDo.db";
    public static final int DATABASE_VERSION = 2;

    public static final class TaskEntry{
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_DUE_DATE = "due_date";
     }

}
