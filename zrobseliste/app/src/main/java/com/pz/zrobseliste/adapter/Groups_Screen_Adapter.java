package com.pz.zrobseliste.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.interfaces.Groups_onClick_Interface;
import com.pz.zrobseliste.screen.AllTasksScreen;
import com.pz.zrobseliste.screen.GroupsScreen;

import java.util.ArrayList;
import java.util.List;


public class Groups_Screen_Adapter extends RecyclerView.Adapter<Groups_Screen_Adapter.ViewHolder> {

    private static final String TAG = "Groups_Screen_Adapter";

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> group_codes = new ArrayList<>();
    private Context context;
    private Groups_onClick_Interface groups_onClick_interface;

    public Groups_Screen_Adapter( Context context, ArrayList<String> names,ArrayList<String> group_codes, Groups_onClick_Interface groups_onClick_interface) {
        this.names = names;
        this.group_codes = group_codes;
        this.context = context;
        this.groups_onClick_interface = groups_onClick_interface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_card,parent,false);
        return new Groups_Screen_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text_view_name.setText(names.get(position));
        holder.group_show_button.setText(group_codes.get(position));
        holder.group_show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groups_onClick_interface.onGroupButtonClick();
                Toast.makeText(v.getContext(),names.get(position),Toast.LENGTH_SHORT).show();
            }
        });
        holder.group_management_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groups_onClick_interface.onManagmentButtonClick();

            }
        });

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_view_name;
        Button group_show_button;
        ImageButton group_management_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_view_name = itemView.findViewById(R.id.text_view_name);
            group_show_button = itemView.findViewById(R.id.group_show_button);
            group_management_button = itemView.findViewById(R.id.group_management_button);

        }
    }
}
