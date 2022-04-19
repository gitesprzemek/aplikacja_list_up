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
import com.pz.zrobseliste.models.GroupUserModel;

import java.util.ArrayList;

public class Group_Management_Adapter extends RecyclerView.Adapter<Group_Management_Adapter.ViewHolder>{
    private ArrayList<GroupUserModel> users = new ArrayList<>();
    GroupManagementInterface groupManagementInterface;


    public Group_Management_Adapter(GroupManagementInterface groupManagementInterface) {
        this.groupManagementInterface = groupManagementInterface;
    }
    public Group_Management_Adapter(ArrayList<GroupUserModel> users) {
        this.users = users;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_user_card,parent,false);
        return new Group_Management_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupUserModel user = users.get(position);
        holder.username.setText(user.getUsername());
        holder.deluser.setText("usuń");
        holder.deluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupManagementInterface.onDeleteUserClick(holder.getAdapterPosition());
            }
        });

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
        Button deluser;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.group_user_name);
            deluser = itemView.findViewById(R.id.delete_user_button);
        }
    }

}
