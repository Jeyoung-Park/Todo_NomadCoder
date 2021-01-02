package com.example.todo_nomadcoder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG="MyAdapter";
    private Context context;
    private List<Todo> todoList;
    private String changeText="";
//    public static TextView TextView_todoItem;
//    public static ImageButton ImageButton_delete, ImageButton_edit, ImageButton_editCheck;
//    public static CheckBox CheckBox_todoItem;
//    public static EditText EditText_todoItem;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView TextView_todoItem;
        public ImageButton ImageButton_delete, ImageButton_edit, ImageButton_editCheck;
        public CheckBox CheckBox_todoItem;
        public EditText EditText_todoItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            TextView_todoItem=itemView.findViewById(R.id.TextView_todoitem);
            ImageButton_delete=itemView.findViewById(R.id.ImageButton_deleteItem);
            ImageButton_edit=itemView.findViewById(R.id.ImageButton_editItem);
            CheckBox_todoItem=itemView.findViewById(R.id.CheckBox_todoItem);
            EditText_todoItem=itemView.findViewById(R.id.EditText_todoitem);
            ImageButton_editCheck=itemView.findViewById(R.id.ImageButton_editCheck);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, List<Todo> myDataset) {
        this.context=context;
        todoList = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Todo currentTodo=new Todo();
        currentTodo.setId(todoList.get(position).id);
        currentTodo.setTodo(todoList.get(position).todo);
        currentTodo.setChecked(todoList.get(position).isChecked);

        holder.TextView_todoItem.setText(todoList.get(position).todo);
        holder.ImageButton_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.EditText_todoItem.setVisibility(View.VISIBLE);
                holder.TextView_todoItem.setVisibility(View.INVISIBLE);
                holder.EditText_todoItem.setText(todoList.get(position).todo);
                Log.d(TAG, position+"번");
                holder.ImageButton_editCheck.setVisibility(View.VISIBLE);
                holder.ImageButton_delete.setVisibility(View.INVISIBLE);
                holder.ImageButton_edit.setVisibility(View.INVISIBLE);
            }
        });
        holder.ImageButton_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTodo(currentTodo);
                notifyDataSetChanged();
            }
        });
        holder.ImageButton_editCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.EditText_todoItem.setVisibility(View.INVISIBLE);
                holder.TextView_todoItem.setVisibility(View.VISIBLE);
                holder.ImageButton_editCheck.setVisibility(View.INVISIBLE);
                holder.ImageButton_delete.setVisibility(View.VISIBLE);
                holder.ImageButton_edit.setVisibility(View.VISIBLE);
                changeText=holder.EditText_todoItem.getText().toString();
                updateTodo(currentTodo, 0);
                Log.d(TAG, currentTodo.getTodo()+"");
            }
        });

        if(currentTodo.isChecked){
            holder.CheckBox_todoItem.setChecked(true);
            holder.TextView_todoItem.setPaintFlags(holder.TextView_todoItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.CheckBox_todoItem.setChecked(false);
//            TextView_todoItem.setPaintFlags(TextView_todoItem.getPaintFlags() |(~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.CheckBox_todoItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d(TAG, isChecked+"");
                holder.CheckBox_todoItem.setChecked(isChecked);
//                if(isChecked) holder.TextView_todoItem.setPaintFlags(holder.TextView_todoItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                updateTodo(currentTodo, 1);
            }
        });

//        if(CheckBox_todoItem.isChecked()){
//            Log.d(TAG, "check");
//            CheckBox_todoItem.setChecked(true);
//            TextView_todoItem.setPaintFlags(TextView_todoItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        }
//        else{
//            Log.d(TAG, "check x");
//            CheckBox_todoItem.setChecked(false);
//            TextView_todoItem.setPaintFlags(TextView_todoItem.getPaintFlags() |(~Paint.STRIKE_THRU_TEXT_FLAG));
//        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return todoList.size();
    }


    private void updateTodo(final Todo todo, int flag) {
        //flag==0이면 editText edit
        //flag==1이면 checkBox edit


//        final String sTask = editTextTask.getText().toString().trim();
//        final String sDesc = editTextDesc.getText().toString().trim();
//        final String sFinishBy = editTextFinishBy.getText().toString().trim();

        class UpdateTodo extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                if(flag==0){
                    todo.setTodo(changeText);
                    todo.setId(todo.getId());
                    todo.setChecked(todo.isChecked);
                    DatabaseClient.getInstance(context).getAppDatabase()
                            .todoDao()
                            .update(todo);
                }
                else if(flag==1){
                    todo.setTodo(todo.getTodo());
                    todo.setId(todo.getId());
                    todo.setChecked(!todo.isChecked);
                    DatabaseClient.getInstance(context).getAppDatabase()
                            .todoDao()
                            .update(todo);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
//                finish();
//                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
                Intent intent=new Intent(context,  MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }
        }

        UpdateTodo ut = new UpdateTodo();
        ut.execute();
    }

    public void deleteTodo(final Todo todo) {
        class DeleteTodo extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .todoDao()
                        .delete(todo);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
//                finish();
//                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
                Intent intent=new Intent(context,  MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }
        }

        DeleteTodo deleteTodo = new DeleteTodo();
        deleteTodo.execute();

    }
}

