package com.example.todo_nomadcoder;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Todo.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();
}

//@Database(entities = {Todo.class}, version = 1)
//public abstract class TodoDatabase extends RoomDatabase {
//// 데이터베이스를 싱글톤으로 사용
//    private static TodoDatabase INSTANCE;
//
//    public abstract TodoDao todoDao();
//
////    데이터베이스 객체 생성 가져오기
//    public static TodoDatabase getDatabase(Context context){
//        if(INSTANCE==null){
//            INSTANCE= Room.databaseBuilder(context, TodoDatabase.class, context.getString(R.string.DB_NAME))
//                    .build();
//        }
//        return INSTANCE;
//    }
//
//    //데이터베이스 객체 제거
//    public static void destoryInstance(){
//        INSTANCE=null;
//    }
//}
