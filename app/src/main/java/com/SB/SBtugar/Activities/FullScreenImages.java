package com.SB.SBtugar.Activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.SB.SBtugar.Adapters.BottomImagesProductDetails;
import com.SB.SBtugar.Adapters.ImagesProductDetails;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FullScreenImages extends AppCompatActivity {
    private ImagesProductDetails adapterImages;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    RecyclerView productImage;
    private BottomImagesProductDetails adapterImagesBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagefull_screen);

        final Product product = (Product) getIntent().getExtras().getSerializable("data");
        productImage = findViewById(R.id.list_images);
        productImage.setNestedScrollingEnabled(true);
        productImage.setHasFixedSize(true);
        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(FullScreenImages.this, LinearLayoutManager.HORIZONTAL, false);
        productImage.setLayoutManager(layoutManager2);
        adapterImages = new ImagesProductDetails(this, product.getImages(),null);
        productImage.setAdapter(adapterImages);


        final RecyclerView productImageBottom = findViewById(R.id.list_images_bottom);
        productImageBottom.setNestedScrollingEnabled(true);
        productImageBottom.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(FullScreenImages.this, LinearLayoutManager.HORIZONTAL, false);
        productImageBottom.setLayoutManager(layoutManager);
        adapterImagesBottom = new BottomImagesProductDetails(FullScreenImages.this, product.getImages().size(),0);
        productImageBottom.setAdapter(adapterImagesBottom);

        productImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int visibleItemCount = layoutManager2.getChildCount();
//                int totalItemCount = layoutManager2.getItemCount();
                int pastVisibleItems = layoutManager2.findFirstVisibleItemPosition();
                adapterImagesBottom = new BottomImagesProductDetails(FullScreenImages.this, product.getImages().size(),pastVisibleItems);
                productImageBottom.setAdapter(adapterImagesBottom);

            }
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
            productImage.setScaleX(mScaleFactor);
            productImage.setScaleY(mScaleFactor);
            return true;
        }
    }
}
