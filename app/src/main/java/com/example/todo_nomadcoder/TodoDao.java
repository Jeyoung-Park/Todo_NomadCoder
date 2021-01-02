package com.example.todo_nomadcoder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM todo")
    /*LiveData<List<Todo>> getAll();//LiveData => Todo테이블에 있는 모든 객체를 계속 관찰하고있다가 변경이 일어나면 그것을 자동으로 업데이트하도록한다.
    //getAll() 은 관찰 가능한 객체가 된다.(즉 디비변경시 반응하는)*/
    List<Todo> getAll();

    @Query("SELECT * FROM todo WHERE id IN (:ids)")
    List<Todo> loadAllByIds(int[] ids);

    @Query("SELECT * FROM todo WHERE id=:myId")
    Todo findById(int myId);

    @Insert
    void insert(Todo... todos); // ...: 동일한 파라미터를 여러 개 받을 때 자동으로 배열 처리한다는 의미

    @Delete
    void delete(Todo todo);

    @Update
    void update(Todo todo);
}

