package com.pz.zrobseliste.screen;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.GroupModel;
import com.pz.zrobseliste.utils.SwipeListener;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.app.Fragment;

public class GroupsScreen extends AppCompatActivity {

    private SwipeListener swipeListener;
    //    RelativeLayout relativeLayout;
//    private List<GroupModel> groupsList;
    private TableLayout table;
    private MainScreen mainScreen;
//    public View currentView;
//    private Context viewContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks_screen);


    }

    private TableLayout tableWithButtons(Context context) {
//        int buttonDim = Resources.getSystem().getDisplayMetrics().widthPixels / 3;
        ArrayList<GroupModel> groupsList = new ArrayList<>();
        int groupsListIndex = 0;
        TableLayout table = new TableLayout(context);
//        System.out.println(table);
        TableRow tableRow = null;
        while (groupsListIndex < groupsList.size()){

//            System.out.println(groupsListIndex);
            tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1.0f)
            );
            System.out.println(tableRow);
            table.addView(tableRow);
            for(int i = 0; i < 2; i++){
                if(groupsListIndex == groupsList.size()) break;
                GroupModel group = groupsList.get(groupsListIndex);
                tableRow.addView(createButton(group, context));
                groupsListIndex++;
            }
        }
        if (groupsList.size() % 2 == 0) {
            tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1.0f)
            );
            table.addView(tableRow);
            tableRow.addView(createNewGroupButton(context));
        } else {
//            table.addView(tableRow);
            tableRow.addView(createNewGroupButton(context));

        }
        return table;
    }

    //
    private Button createButton(GroupModel group, Context context){

        int buttonDim = Resources.getSystem().getDisplayMetrics().widthPixels / 3;

        Button button = new Button(context);
//        button.setLayoutParams(new TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT,
//                        1.0f));
        button.setText(group.getAbb());
        button.setWidth(buttonDim);
        button.setHeight(buttonDim);
        button.callOnClick();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(group.registrationDatatoJSON()


                );
            }
        });
        return button;
    }


    private Button createNewGroupButton(Context context){
        int buttonDim = Resources.getSystem().getDisplayMetrics().widthPixels / 4;

        Button button = new Button(context);
//        button.setLayoutParams(new TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT,
//                1.0f
//        ));

        button.setText("Dodaj Grupe");
        button.setWidth(buttonDim/2);
        button.setHeight(buttonDim);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("DODANO GRUPE");
                int id = mainScreen.getGroupsList().size();
                String name = "Grupa " + id;
                GroupModel group = new GroupModel(id, name);
                mainScreen.addGroup(group);

            }
        });

        return button;
    }
}