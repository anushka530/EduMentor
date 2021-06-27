package com.edumentor.edumentor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class TodoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserId;

    private ProgressDialog loader;

    private String key = "";
    private String task;
    private String description;
    private String in_time;
    private String in_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        try {
            mUser = mAuth.getCurrentUser();
        }
        catch (NullPointerException ignored){
        }
        try {
            onlineUserId = mUser.getUid();
        }
        catch(NullPointerException ignored){
        }
        try {
            reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserId);
        }
        catch (NullPointerException ignored){
        }



        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addTask();
            }
        });


    }


    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_file, null);
        myDialog.setView(myView);


        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        EditText task = myView.findViewById(R.id.task);
        EditText description = myView.findViewById(R.id.desc);
        Button save = myView.findViewById(R.id.btn_save);
        Button cancel = myView.findViewById(R.id.btn_cancel);

        EditText in_date = myView.findViewById(R.id.in_date);
        EditText in_time = myView.findViewById(R.id.in_time);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mTask = task.getText().toString().trim();
                String mDescription = description.getText().toString().trim();

                String mDate = in_date.getText().toString().trim();
                String mTime = in_time.getText().toString().trim();


                String id = reference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());


                if (TextUtils.isEmpty(mTask)) {
                    task.setError("Task Required");
                    return;
                }
                if (TextUtils.isEmpty(mDescription)) {
                    description.setError("Description Required");
                    return;
                }
                if (TextUtils.isEmpty(mTime)) {
                    in_time.setError("time Required");
                    return;
                }
                if (TextUtils.isEmpty(mDate)) {
                    in_date.setError("date Required");
                    return;
                }
                else{
                    loader.setMessage("Adding data");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    Model model = new Model(mTask, mDescription, id, onlineUserId,mDate, mTime);
                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull  Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(TodoActivity.this, "Task inserted successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String error = task.getException().toString();
                                Toast.makeText(TodoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();


                        }
                    });
                }
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference,Model.class)
                .build();
        FirebaseRecyclerAdapter<Model, MyviewHolder> adapter = new FirebaseRecyclerAdapter<Model, MyviewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull  TodoActivity.MyviewHolder holder, int position, @NonNull  Model model) {
                holder.setTask(model.getTask());
                holder.setDesc(model.getDescription());
                holder.setIn_date(model.getIn_date());
                holder.setIn_time(model.getIn_time());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        key = getRef(position).getKey();
                        task = model.getTask();
                        description = model.getDescription();
                        in_date = model.getIn_date();
                        in_time = model.getIn_time();



                        updateTask();


                    }
                });

            }

            @NonNull
            @Override
            public MyviewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout,parent,false);
                return new MyviewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public static class MyviewHolder extends RecyclerView.ViewHolder{
        View mView ;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTask(String task){
            TextView taskTextView = mView.findViewById(R.id.taskTV);
            taskTextView.setText(task);
        }
        public void setDesc(String desc){
            TextView descTextView = mView.findViewById(R.id.descTV);
            descTextView.setText(desc);

        }
        public void setIn_date(String in_date){
            TextView dateTextView = mView.findViewById(R.id.dateTV);
            dateTextView.setText(in_date);
        }
        public void setIn_time(String in_time){
            TextView timeTextView = mView.findViewById(R.id.timeTV);
            timeTextView.setText(in_time);
        }



    }
    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_task, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mTask = view.findViewById(R.id.Edittask);
        EditText mdesc = view.findViewById(R.id.Editdesc);
        EditText mTime = view.findViewById(R.id.Edittime);
        EditText mDate = view.findViewById(R.id.EditDate);



        mTask.setText(task);
        mTask.setSelection(task.length());

        mdesc.setText(description);
        mdesc.setSelection(description.length());

        mTime.setText(in_time);
        mTime.setSelection(in_time.length());

        mDate.setText(in_date);
        mDate.setSelection(in_date.length());



        Button btnDel = view.findViewById(R.id.btn_delete);
        Button btnUp = view.findViewById(R.id.btn_update);

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = mTask.getText().toString().trim();
                description = mdesc.getText().toString().trim();
                in_date = mDate.getText().toString().trim();
                in_time = mTime.getText().toString().trim();
                String date = DateFormat.getDateInstance().format(new Date());

                Model model = new Model(task,description,key,date,in_date,in_time);

                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(TodoActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String err = task.getException().toString();
                            Toast.makeText(TodoActivity.this, "Update failed"+ err, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(TodoActivity.this, "Deleted task successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String err = task.getException().toString();
                            Toast.makeText(TodoActivity.this, "Failed to delete"+err, Toast.LENGTH_SHORT).show();

                        }



                    }
                });

                dialog.dismiss();
            }
        });
        dialog.show();
    }

}