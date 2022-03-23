package com.pz.zrobseliste.adapter;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;

public class AllTaskScreenItemTouch extends ItemTouchHelper.SimpleCallback {

    private All_Task_Screen_Adapter_Rec adapter;

    public AllTaskScreenItemTouch(All_Task_Screen_Adapter_Rec adapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder targer){
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
    {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle(R.string.delete_task);
            builder.setMessage(R.string.do_you_really_wanna_delete_this_task);
            builder.setPositiveButton(R.string.accept,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteItem(position);
                        }
                    });
            builder.setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else
        {
            adapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
            super.onChildDraw(c, recyclerView, viewHolder, dX,dY,actionState,isCurrentlyActive);
            Drawable icon;
            ColorDrawable background;

            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 20;
            if(dX>0)
            {
                icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.icon_edit);
                background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.colorPrimaryDark));
            }
            else
            {
                icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.icon_delete_swipe);
                background = new ColorDrawable(Color.RED);
            }
            assert icon != null;
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight())/2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if(dX>0){//przesun w prawo
                int iconLeft = itemView.getLeft() + iconMargin;
                int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                icon.setBounds(iconLeft, iconTop,iconRight,iconBottom);

                background.setBounds(itemView.getLeft(), itemView.getTop(),itemView.getLeft() + ((int)dX) + backgroundCornerOffset, itemView.getBottom());
            }
            else if(dX<0){//przesun w lewo
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop,iconRight,iconBottom);

                background.setBounds(itemView.getRight() + ((int)dX) - backgroundCornerOffset, itemView.getTop(),itemView.getRight(), itemView.getBottom());
            }
            else
            {
                background.setBounds(0,0,0,0);
            }
            background.draw(c);
            icon.draw(c);
    }

}
