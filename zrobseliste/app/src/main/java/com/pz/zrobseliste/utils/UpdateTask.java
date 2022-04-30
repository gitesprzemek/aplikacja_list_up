package com.pz.zrobseliste.utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.interfaces.DialogCloseListener;
import com.pz.zrobseliste.models.ToDoModel;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;
    private Button newTaskCancelButton;

    private OkHttpClient client;
    private Request request;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String cookie = "cookie";
    public static final String listid = "listid";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static UpdateTask newInstance(){
        return new UpdateTask();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskEditText);
        newTaskSaveButton = getView().findViewById(R.id.add_Task);
        newTaskCancelButton = getView().findViewById(R.id.cancel_Task);

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle !=null)
        {
            isUpdate = true;
            String task = bundle.getString("task_description");
            newTaskText.setText(task);
            if(task.length()>0)
            {
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            }

        }

        newTaskSaveButton.setEnabled(false);
        newTaskSaveButton.setTextColor(Color.GRAY);

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().equals(""))
                {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                }

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else
                {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String text = newTaskText.getText().toString();
                if(finalIsUpdate)
                {
                    sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    client = CustomHttpBuilder.SSL().build();
                    String task_id = "" + bundle.getInt("taks_id");
                    String status_id = "" + bundle.getBoolean("task_status");


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
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Log.d("status code update task",String.valueOf(response.code()));
                            if(response.code()>= 200 & response.code() > 300)
                            {
                                Log.d("resposne body update task",response.body().string());
                            }
                        }
                    });

                    System.out.println("task updated");
                }
                dismiss();
            }
    });
        newTaskCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
        {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
