package com.pz.zrobseliste.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.ListModel;

import java.util.ArrayList;
import java.util.List;

public class Drop_Down_Menu_Adapter extends ArrayAdapter<ListModel> {

    Context context;
    int resource, textViewResourceId;
    String gowno;
    List<ListModel> items, tempItems, suggestions;

    public Drop_Down_Menu_Adapter(Context context, int resource, int textViewResourceId, List<ListModel> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<ListModel>(items); // this makes the difference.
        suggestions = new ArrayList<ListModel>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        ListModel list = items.get(position);
        if (list != null) {
            TextView lblName = (TextView) view.findViewById(R.id.text_view_list_name);
            if (lblName != null) {
                lblName.setText(list.getName());
            }
        }
        return view;
    }

}