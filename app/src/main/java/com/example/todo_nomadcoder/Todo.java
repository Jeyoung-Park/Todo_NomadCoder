package com.example.todo_nomadcoder;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo")
//기본적으로 Room은 클래스 이름을 db 이름으로 사용
public class Todo {

    Todo(){}

    Todo(String todo){
        this.todo=todo;
        this.isChecked=false;
    }
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "todo") // 데이터 베이스의 열 이름을 다르게 지정하고 싶을 떄 사용
    public String todo;

    @ColumnInfo(name = "isChecked")
    public boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


}