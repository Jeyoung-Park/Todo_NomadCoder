package com.example.todo_nomadcoder;

import android.content.Context;

import androidx.room.Room;

//TodoDatabase의 객체를 만들기 -> 비쌈 -> 싱글톤 형태
public class DatabaseClient {
    private Context context;
    private static DatabaseClient mInstance;

    //our app database object
    private TodoDatabase todoDatabase;

    private DatabaseClient(Context context) {
        this.context = context;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        todoDatabase = Room.databaseBuilder(context, TodoDatabase.class, context.getString(R.string.DB_NAME)).build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public TodoDatabase getAppDatabase() {
        return todoDatabase;
    }
}
