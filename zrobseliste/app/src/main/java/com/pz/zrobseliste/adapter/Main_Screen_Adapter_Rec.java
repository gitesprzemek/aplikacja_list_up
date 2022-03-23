package com.pz.zrobseliste.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.ToDoModel;
import com.pz.zrobseliste.screen.MainScreen;
import com.pz.zrobseliste.utils.AddNewTask;

import java.util.List;

public class Main_Screen_Adapter_Rec extends RecyclerView.Adapter<Main_Screen_Adapter_Rec.ViewHolder>{
    private List<ToDoModel> todoList;
    private MainScreen activity;

    public Main_Screen_Adapter_Rec(MainScreen activity){
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.groups_task_layout,parent,false);
        return new ViewHolder(itemView);

    }
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        /*
        if(item.getStatus()==1)
        {
            holder.rel_layout.setBackgroundColor(Color.GREEN);
        }
        */
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    System.out.println("Wyslij do serwera polecenie ustawienia statusu na 1");
                }
                else
                {
                    System.out.println("Wyslij do serwera polecenie ustawienia statusu na 0");
                }
            }
        });

    }

    public int getItemCount()
    {
        return todoList.size();
    }

    public void setTasks(List<ToDoModel> todoList)
    {
        this.todoList = todoList;
        notifyDataSetChanged();
    }
    private boolean toBoolean(int n)
    {
        return n!=0;
    }

    public Context getContext()
    {
        return activity;
    }

    public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        todoList.remove(item);
        notifyItemRemoved(position);

    }

    public void editItem(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        RelativeLayout rel_layout;
        TextView text_view_person_assigned;
        ViewHolder(View view)
        {
            super(view);
            rel_layout = view.findViewById(R.id.rel_layout);
            task = view.findViewById(R.id.check_box);
            text_view_person_assigned = view.findViewById(R.id.text_view_person_assigned);

        }
    }

}
