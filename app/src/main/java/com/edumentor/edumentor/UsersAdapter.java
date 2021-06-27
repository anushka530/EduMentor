package com.edumentor.edumentor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edumentor.edumentor.User;
import com.edumentor.edumentor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolder> {

    ArrayList<User> list;
    Context context;

    public UsersAdapter(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        User users= list.get(position);


        holder.userName.setText(users.getUsername());
        holder.userTech.setText(users.getTech());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ChatsDetailActivity.class);
                intent.putExtra("userId", users.getUserId());

                intent.putExtra("username", users.getUsername());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public void setFilter(ArrayList<User> userArrayList){
        list=new ArrayList<>();
        list.addAll(userArrayList);
        notifyDataSetChanged();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView userName, userTech;

        public viewHolder(@NonNull View itemView) {
            super(itemView);


            userName= itemView.findViewById(R.id.username);
            userTech = itemView.findViewById(R.id.Tech);

        }
    }

}
