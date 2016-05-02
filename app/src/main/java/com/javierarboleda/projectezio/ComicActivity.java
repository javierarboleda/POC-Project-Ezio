package com.javierarboleda.projectezio;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.javierarboleda.projectezio.comic.FileService;

public class ComicActivity extends AppCompatActivity {

    SubsamplingScaleImageView mImageView;
    TextView mCoordTextView;
    float mX;
    float mY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);

        mCoordTextView = (TextView) findViewById(R.id.text);

        testLoadImage();
    }

    private void testLoadImage() {

        mImageView =
                (SubsamplingScaleImageView) findViewById(R.id.comicImageView);

        mImageView.setImage(ImageSource.uri(
                "/storage/emulated/0/comics/Batman Cacophony 01 (of 03) (2009) (3 covers)" +
                        " (digital) (Minutemen-PhD)/Batman- Cacophony 001-000.jpg"));

        // imageView.startAnimation(someAnimation);

        mImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_OUTSIDE);



        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mX = event.getX();
                mY = event.getY();

                mCoordTextView.setText("coordinates: " + mX + ", " + mY);

                return false;
            }
        });

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                /**
                   Using techniques below, going to try to move, zoom in or out, and crop all at
                   once.

                   Second animation cancels first, so comment one animation out to see the other.

                   I think next step is to research and test animations out more, see if there
                   is a way to combine what I'm looking for in one animation, i.e. move, zoom, crop.
                */

                // creates an animation that moves and zooms

                mImageView.animateScaleAndCenter(1.5f, new PointF(mX + mX/2, mY + mY/2))
                        .withDuration(2000)
                        .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
                        .withInterruptible(false)
                        .start();

                // creates an animation which crops from bottom up

                ValueAnimator anim = ValueAnimator.ofInt(mImageView.getMeasuredHeight(), 0);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
                        layoutParams.height = val;
                        mImageView.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(1000);
                anim.start();

                return false;
            }
        });




    }

    private void testLogFileNames() {
        String path = Environment.getExternalStorageDirectory().getPath() +
                "/comics/Batman Cacophony 01 (of 03) (2009) (3 covers) (digital) (Minutemen-PhD)";
        String fileNames = FileService.getListOfFileNamesInDir(path);

        Log.d("Files", fileNames);
    }

}
