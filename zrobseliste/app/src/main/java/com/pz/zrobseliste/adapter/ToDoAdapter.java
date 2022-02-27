package com.pz.zrobseliste.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.ToDoModel;
import com.pz.zrobseliste.screen.MainScreen;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>{
    private List<ToDoModel> todoList;
    private MainScreen activity;

    public ToDoAdapter(MainScreen activity){
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view)
        {
            super(view);
            task = view.findViewById(R.id.check_box);
        }
    }

}
