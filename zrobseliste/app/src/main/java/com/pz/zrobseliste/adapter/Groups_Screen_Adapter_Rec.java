package com.pz.zrobseliste.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.interfaces.GroupsonClickInterface;
import com.pz.zrobseliste.models.GroupModel;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Groups_Screen_Adapter_Rec extends RecyclerView.Adapter<Groups_Screen_Adapter_Rec.ViewHolder> {

    private static final String TAG = "Groups_Screen_Adapter";

    private ArrayList<GroupModel> groups= new ArrayList<>();

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> group_codes = new ArrayList<>();
    private Context context;
    private GroupsonClickInterface groupsonClick_interface;

    public Groups_Screen_Adapter_Rec(Context context, ArrayList<GroupModel> groups, GroupsonClickInterface groupsonClick_interface) {
        this.groups = groups;
        this.context = context;
        this.groupsonClick_interface = groupsonClick_interface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_card,parent,false);
        return new Groups_Screen_Adapter_Rec.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupModel group = groups.get(position);
        holder.text_view_name.setText(group.getName());
        holder.group_show_button.setText(group.getGroup_code());
        holder.group_show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupsonClick_interface.onGroupButtonClick(holder.getAdapterPosition());
            }
        });
        holder.group_management_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupsonClick_interface.onManagmentButtonClick(holder.getAdapterPosition(),v);

            }
        });

    }

    @Override
    public int getItemCount() {
        return groups.size();
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
