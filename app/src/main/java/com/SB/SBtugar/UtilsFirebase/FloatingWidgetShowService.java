package com.SB.SBtugar.UtilsFirebase;

/**
 * Created by Juned on 9/15/2017.
 */

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


import com.SB.SBtugar.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class FloatingWidgetShowService extends Service {


    WindowManager windowManager;
    View floatingView;
//    collapsedView;
    WindowManager.LayoutParams params ;

    public FloatingWidgetShowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
    GestureDetector gestureDetector;
    @Override
    public void onCreate() {
        super.onCreate();
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout, null);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        windowManager.addView(floatingView, params);


//        collapsedView = floatingView.findViewById(R.id.Layout_Collapsed);

        floatingView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopSelf();

            }
        });
        floatingView.findViewById(R.id.MainParentRelativeLayout).setOnTouchListener(new View.OnTouchListener() {
            int X_Axis, Y_Axis;
            float TouchX, TouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    // single tap
                    Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
                    intent.putExtra("fromOut",true);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    stopSelf();

                    return true;
                } else {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            X_Axis = params.x;
                            Y_Axis = params.y;
                            TouchX = event.getRawX();
                            TouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_UP:

//                        collapsedView.setVisibility(View.GONE);
//                        expandedView.setVisibility(View.VISIBLE);
                            return true;

                        case MotionEvent.ACTION_MOVE:

                            params.x = X_Axis + (int) (event.getRawX() - TouchX);
                            params.y = Y_Axis + (int) (event.getRawY() - TouchY);
                            windowManager.updateViewLayout(floatingView, params);
                            return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
    }

}
