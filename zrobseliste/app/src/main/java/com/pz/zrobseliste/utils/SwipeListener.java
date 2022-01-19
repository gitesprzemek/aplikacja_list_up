package com.pz.zrobseliste.utils;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeListener implements View.OnTouchListener{

    private SwipeType swipeType;
    GestureDetector gestureDetector;

    public SwipeListener(View view) {
        GestureDetector.SimpleOnGestureListener listener =
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onDown(MotionEvent e){
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        float x1 = e1.getX();
                        float y1 = e1.getY();
                        float x2 = e2.getX();
                        float y2 = e2.getY();

                        try {
                            return swipeRecognition(x1, y1, x2, y2, velocityX, velocityY);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        return  false;
                    }

                };
        gestureDetector = new GestureDetector(listener);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public boolean swipeRecognition(float x1, float y1, float x2, float y2, float velX, float velY){
        int threshold = 100;
        int vel_threshold = 100;

        float xDiff = x2 - x1;
        float yDiff = y2 - y1;

        if(Math.abs(xDiff) > Math.abs(yDiff)){
            if(Math.abs(xDiff) > threshold && Math.abs(velX) > vel_threshold){
                if(xDiff > 0){
                    this.swipeType = SwipeType.RIGHT;
                    System.out.println(this.swipeType);
                }else{
                    this.swipeType = SwipeType.LEFT;
                    System.out.println(this.swipeType);
                }
                return true;
            }
        }else {
            if(Math.abs(yDiff) > threshold && Math.abs(velY) > vel_threshold){
                if(yDiff > 0){
                    this.swipeType = SwipeType.DOWN;
                    System.out.println(this.swipeType);
                    return true;
                }else{
                    this.swipeType = SwipeType.UP;
                    System.out.println(this.swipeType);
                }
                return true;
            }
        }
        return false;
    }

    public SwipeType getSwipeType() {
        return swipeType;
    }

    public void setSwipeType() {
        this.swipeType = swipeType;
    }
}
