package com.pz.zrobseliste.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.interfaces.MenuHandlerInterface;
import com.pz.zrobseliste.models.MenuModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Menu_Screen_Adapter_Rec extends RecyclerView.Adapter<Menu_Screen_Adapter_Rec.ViewHolder>{

    private ArrayList<MenuModel> options = new ArrayList<>();
    private MenuHandlerInterface menuHandlerInterface;

    public Menu_Screen_Adapter_Rec(ArrayList<MenuModel> options, MenuHandlerInterface menuHandlerInterface)
    {
        this.options=options;
        this.menuHandlerInterface = menuHandlerInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item,parent,false);
        return new Menu_Screen_Adapter_Rec.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuModel option = options.get(position);
        holder.textView.setText(option.getText());
        holder.imageView.setImageResource(option.getImage());
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_menu);
            textView = itemView.findViewById(R.id.text_view_menu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuHandlerInterface.onMainMenuItemClick(getAdapterPosition());
                }
            });

        }
    }
}
