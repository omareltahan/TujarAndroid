package com.SB.SBtugar.Activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.SB.SBtugar.R;
import com.bumptech.glide.Glide;

public class FullScreenImage extends AppCompatActivity {
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images_item_with_back);
        imageView = findViewById(R.id.image);
        Glide.with(this).load(getIntent().getExtras().getString("URL")).into(imageView);
        findViewById(R.id.back).setOnClickListener(view -> {
            finish();
        });
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}
