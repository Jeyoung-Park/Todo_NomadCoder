package com.example.todo_nomadcoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private static final String DB_NAME="DB_TODO";

    private EditText EditText_insert;
    private RecyclerView recyclerView_todo;
    private Button Button_insert;
    private TodoDatabase db;
    private static final String TAG="MainActivity";

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Todo> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText_insert=findViewById(R.id.EditText_todo);
        recyclerView_todo=findViewById(R.id.recyclerView_todo);
        Button_insert=findViewById(R.id.Button_insert);

//        db = TodoDatabase.getTodoDatabase(this);
        todoList=new ArrayList<>();
//        todoList.clear();
/*
        db.todoDao().getAll().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
//                for(Todo t:todos){
//                    Log.d(TAG, t.id+" / "+t.todo);
//                    todoList.add(t);
//                }
//                mAdapter = new MyAdapter(todos);
                todoList=todos;
                mAdapter = new MyAdapter(todoList);

            }
        });*/
//        todoList=db.todoDao().getAll();

        recyclerView_todo.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView_todo.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(MainActivity.this, todoList);
        recyclerView_todo.setAdapter(mAdapter);

        Button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentTodo=EditText_insert.getText().toString();
                if(currentTodo.length()<=0){
                    Toast.makeText(MainActivity.this, "한 글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
//                    new InsertAsyncTask(db.todoDao()).execute(new Todo(currentTodo));
                    insertTodo();
                    EditText_insert.setText("");
//                    mAdapter.notifyDataSetChanged();
                    Intent intent=new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

            }
        });

        getTodos();
    }

    private void getTodos() {
        class GetTodos extends AsyncTask<Void, Void, List<Todo>> {

            @Override
            protected List<Todo> doInBackground(Void... voids) {
                List<Todo> todoList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .todoDao()
                        .getAll();
                return todoList;
            }

            @Override
            protected void onPostExecute(List<Todo> todos) {
                super.onPostExecute(todos);
                todoList=todos;
//                Log.d(TAG, "onPostExecute");
                mAdapter = new MyAdapter(MainActivity.this, todoList);
                recyclerView_todo.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();

            }
        }

        GetTodos getTodos = new GetTodos();
        getTodos.execute();
    }

    public void insertTodo() {
//        final String currentTodo = EditText_insert.getText().toString();

        class SaveTodo extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //todo 생성
                Todo todo = new Todo(EditText_insert.getText().toString());

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .todoDao()
                        .insert(todo);
                return null;
            }

//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                finish();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
//            }
        }

        SaveTodo st = new SaveTodo();
        st.execute();
//        mAdapter.notifyDataSetChanged();
    }

//    public void deleteTodo(final Todo todo) {
//        class DeleteTodo extends AsyncTask<Void, Void, Void> {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                        .todoDao()
//                        .delete(todo);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
////                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
////                finish();
////                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
//            }
//        }
//
//        DeleteTodo deleteTodo = new DeleteTodo();
//        deleteTodo.execute();
//
//    }

/*
//    메인스레드에서 데이터베이스에 접근할 수 없음 =>AsyncTask 사용
    public static class InsertAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mTodoDao;

        public InsertAsyncTask(TodoDao todoDao){
            this.mTodoDao=todoDao;
        }
    //    백그라운드작업(메인스레드에서 작업하면 오류)
        @Override
        protected Void doInBackground(Todo... todos) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            mTodoDao.insert(todos[0]);
            return null;
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);						// 태스크를 백그라운드로 이동
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
        }
        Process.killProcess(Process.myPid());	// 앱 프로세스 종료
    }
}