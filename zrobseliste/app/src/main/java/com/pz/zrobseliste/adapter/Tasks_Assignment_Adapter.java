package com.pz.zrobseliste.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.interfaces.GroupManagementInterface;
import com.pz.zrobseliste.interfaces.TaskAssignmentInterface;
import com.pz.zrobseliste.models.GroupUserModel;

import java.util.ArrayList;

public class Tasks_Assignment_Adapter extends RecyclerView.Adapter<Tasks_Assignment_Adapter.ViewHolder>{

    private ArrayList<GroupUserModel> users = new ArrayList<>();
    TaskAssignmentInterface taskAssignmentInterface;


    public Tasks_Assignment_Adapter(TaskAssignmentInterface taskAssignmentInterface) {
        this.taskAssignmentInterface = taskAssignmentInterface;
    }
    public Tasks_Assignment_Adapter(ArrayList<GroupUserModel> users) {
        this.users = users;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_assignment_card,parent,false);
        return new Tasks_Assignment_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupUserModel user = users.get(position);
        holder.username.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setusers(ArrayList<GroupUserModel> users)
    {
        this.users = users;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_assignment_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskAssignmentInterface.onAssignmentCardClick(getAdapterPosition());
                }
            });
        }
    }

}
