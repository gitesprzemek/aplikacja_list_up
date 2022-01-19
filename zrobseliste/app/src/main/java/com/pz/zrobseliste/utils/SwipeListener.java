package com.pz.zrobseliste.utils;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeListener{

    private int threshold = 100;
    private int threshold_velocity = 100;
    private SwipeType swipeType;

    public void swipeRecognition(float x1, float y1, float x2, float y2, float velX, float velY){
        float xDiff = x2 - x1;
        float yDiff = y2 - y1;

        if(Math.abs(xDiff) > Math.abs(yDiff)){
            if(Math.abs(xDiff) > threshold && Math.abs(velX) > threshold_velocity){
                if(xDiff > 0){
                    this.swipeType = SwipeType.RIGHT;
                    System.out.println(this.swipeType);
                }else{
                    this.swipeType = SwipeType.LEFT;
                    System.out.println(this.swipeType);
                }
            }
        }else {
            if(Math.abs(yDiff) > threshold && Math.abs(velY) > threshold_velocity){
                if(yDiff > 0){
                    this.swipeType = SwipeType.DOWN;
                    System.out.println(this.swipeType);

                }else{
                    this.swipeType = SwipeType.UP;
                    System.out.println(this.swipeType);
                }
            }
        }
    }

    public SwipeType getSwipeType() {
        return swipeType;
    }

    public void setSwipeType(float x1, float y1, float x2, float y2, float velX, float velY) {
        swipeRecognition(x1, y1, x2, y2, velX, velY);
    }
}
