package com.pz.zrobseliste.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.ToDoModel;
import com.pz.zrobseliste.models.ToDoModel1;
import com.pz.zrobseliste.screen.AllTasksScreen;
import com.pz.zrobseliste.screen.MainScreen;

import java.text.BreakIterator;
import java.util.List;

public class ToDoAdapter1 extends RecyclerView.Adapter<ToDoAdapter1.ViewHolder>{
    private List<ToDoModel1> todoList;
    private AllTasksScreen activity;

    public ToDoAdapter1(AllTasksScreen activity){
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_task_layout,parent,false);
        return new ViewHolder(itemView);

    }
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        ToDoModel1 item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.button.setText(item.getGroup());
        holder.task.setChecked(toBoolean(item.getStatus()));

    }

    public int getItemCount()
    {
        return todoList.size();
    }

    public void setTasks(List<ToDoModel1> todoList)
    {
        this.todoList = todoList;
        notifyDataSetChanged();
    }
    private boolean toBoolean(int n)
    {
        return n!=0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        Button button;
        CheckBox task;
        ViewHolder(View view)
        {
            super(view);
            task = view.findViewById(R.id.check_box);
            button = view.findViewById(R.id.button);
        }
    }

}