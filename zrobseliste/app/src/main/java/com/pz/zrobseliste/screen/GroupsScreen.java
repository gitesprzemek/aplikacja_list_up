package com.pz.zrobseliste.screen;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.GroupModel;
import com.pz.zrobseliste.utils.SwipeListener;

import java.util.ArrayList;

//import android.support.v4.app.Fragment;

public class GroupsScreen extends AppCompatActivity {

    private SwipeListener swipeListener;
    //    RelativeLayout relativeLayout;
//    private List<GroupModel> groupsList;
    private TableLayout table;
    private MainScreen mainScreen;
    private ArrayList<GroupModel> groupModels;
//    public View currentView;
//    private Context viewContext;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_screen);
        createGroups();
//        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

//        scrollView.addView(new Button(this));
//        scrollView.addView(tableWithButtons(this));
        ScrollView scrollView = findViewById(R.id.scrollView);

        TableLayout table = tableWithButtons(this);
        scrollView.addView(table);
    }

    private void createGroups() {
        groupModels = new ArrayList<>();
        int n = 21;
        for (int i = 1; i < n; i++) {
            groupModels.add(new GroupModel(i, "Grupa " + i));
        }

    }

    private TableLayout tableWithButtons(Context context) {
//        int buttonDim = Resources.getSystem().getDisplayMetrics().widthPixels / 3;
//        ArrayList<GroupModel> groupsList = new ArrayList<>();
        int groupsListIndex = 0;
        TableLayout table = new TableLayout(context);
//        System.out.println(table);
        TableRow tableRow = null;
        while (groupsListIndex < groupModels.size()) {

//            System.out.println(groupsListIndex);
            tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1.0f)
            );
            System.out.println(tableRow);
            table.addView(tableRow);
            for (int i = 0; i < 2; i++) {
                if (groupsListIndex == groupModels.size()) break;
                GroupModel group = groupModels.get(groupsListIndex);
                tableRow.addView(createButton(group, context));
                groupsListIndex++;
            }
        }
        if (groupModels.size() % 2 == 0) {
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
    private Button createButton(GroupModel group, Context context) {

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


    private Button createNewGroupButton(Context context) {
        int buttonDim = Resources.getSystem().getDisplayMetrics().widthPixels / 4;

        Button button = new Button(context);
//        button.setLayoutParams(new TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT,
//                1.0f
//        ));

        button.setText("Dodaj Grupe");
        button.setWidth(buttonDim / 2);
        button.setHeight(buttonDim);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("DODANO GRUPE");
                int id = groupModels.size();
                String name = "Grupa " + id;
                GroupModel group = new GroupModel(id, name);
                groupModels.add(group);

            }
        });

        return button;
    }
}