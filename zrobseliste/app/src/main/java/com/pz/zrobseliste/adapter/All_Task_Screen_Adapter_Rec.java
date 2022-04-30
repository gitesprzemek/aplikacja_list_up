package com.pz.zrobseliste.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.interfaces.AllTaskScreenInterface;
import com.pz.zrobseliste.models.ToDoModel;
import com.pz.zrobseliste.models.ToDoModel1;
import com.pz.zrobseliste.screen.AllTasksScreen;
import com.pz.zrobseliste.utils.AddNewTask;
import com.pz.zrobseliste.utils.CustomHttpBuilder;
import com.pz.zrobseliste.utils.UpdateTask;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class All_Task_Screen_Adapter_Rec extends RecyclerView.Adapter<All_Task_Screen_Adapter_Rec.ViewHolder>{
    private List<ToDoModel1> todoList;
    private AllTasksScreen activity;
    AllTaskScreenInterface allTaskScreenInterface;

    private OkHttpClient client;
    private Request request;
    SharedPreferences sharedPreferences;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String cookie = "cookie";

    public All_Task_Screen_Adapter_Rec(AllTasksScreen activity,AllTaskScreenInterface allTaskScreenInterface){
        this.activity = activity;
        this.allTaskScreenInterface = allTaskScreenInterface;
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
        holder.task.setChecked(item.getStatus());
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    client = CustomHttpBuilder.SSL().build();
                    String task_id = "" + item.getId();
                    String text = "" + item.getTask();
                    String status_id = "" + true;

                    URL url = new HttpUrl.Builder()
                            .scheme("https")
                            .host("weaweg.mywire.org")
                            .port(8080)
                            .addPathSegments("api/tasks/"+task_id)
                            .addQueryParameter("status",status_id)
                            .addQueryParameter("desc",text)
                            .build().url();

                    Log.d("url", url.toString());

                    RequestBody body = RequestBody.create("",null);

                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                            .patch(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            System.out.println("nie udalo sie zmienic statusu");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Log.d("status code : task status", String.valueOf(response.code()));
                            if(response.code()>=200 && response.code()<300)
                            {
                                Log.d("resposne body status task", response.body().string());
                            }
                        }
                    });

                }
                else
                {
                    sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    client = CustomHttpBuilder.SSL().build();
                    String task_id = "" + item.getId();
                    String text = "" + item.getTask();
                    String status_id = "" + false;

                    URL url = new HttpUrl.Builder()
                            .scheme("https")
                            .host("weaweg.mywire.org")
                            .port(8080)
                            .addPathSegments("api/tasks/"+task_id)
                            .addQueryParameter("status",status_id)
                            .addQueryParameter("desc",text)
                            .build().url();

                    Log.d("url", url.toString());

                    RequestBody body = RequestBody.create("",null);

                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                            .patch(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            System.out.println("nie udalo sie zmienic statusu");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Log.d("status code : task status", String.valueOf(response.code()));
                            if(response.code()>=200 && response.code()<300)
                            {
                                Log.d("resposne body status task", response.body().string());
                            }
                        }
                    });
                    System.out.println("Wyslij do serwera polecenie ustawienia statusu na 0");
                }
            }
        });

    }

    public Context getContext()
    {
        return activity;
    }

    public void deleteItem(int position){
        allTaskScreenInterface.deleteTask(position);
        notifyItemRemoved(position);

    }

    public void editItem(int position){
        ToDoModel1 item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("taks_id",item.getId());
        bundle.putBoolean("task_status",item.getStatus());
        bundle.putString("task_description",item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), UpdateTask.TAG);
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
