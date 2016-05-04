package com.javierarboleda.projectezio;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.javierarboleda.projectezio.utils.AnimationUtil;
import com.javierarboleda.projectezio.utils.FileService;

public class ComicActivity extends AppCompatActivity {

    SubsamplingScaleImageView mImageView;
    TextView mCoordTextView;
    View mTopPanel;
    View mBottomPanel;
    View mLeftPanel;
    View mRightPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_comic);

        mCoordTextView = (TextView) findViewById(R.id.text);
        mTopPanel = findViewById(R.id.topPanel);
        mBottomPanel = findViewById(R.id.bottomPanel);
        mLeftPanel = findViewById(R.id.leftPanel);
        mRightPanel = findViewById(R.id.rightPanel);

        testLoadImage();
    }


    private void testLoadImage() {

        mImageView =
                (SubsamplingScaleImageView) findViewById(R.id.comicImageView);

        mImageView.setImage(ImageSource.uri(
                "/storage/emulated/0/comics/Batman Cacophony 01 (of 03) (2009) (3 covers)" +
                        " (digital) (Minutemen-PhD)/Batman- Cacophony 001-023.jpg"));

        mImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_OUTSIDE);

        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                PointF sCoord = mImageView.viewToSourceCoord(e.getX(), e.getY());

                String coordinates = "coordinates: " + sCoord.x + ", " + sCoord.y;

                String scale = "scale: " + mImageView.getScale() +
                        ", scaleX: " + mImageView.getScaleX() +
                        ", scaleY: " + mImageView.getScaleY();

                String text = coordinates + " " + scale;

                mCoordTextView.setText(text);

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

                float x = e.getX();
                float y = e.getY();

                // creates an animation that moves and zooms

                mImageView.animateScaleAndCenter(1.5f, new PointF(x, y))
                        .withDuration(1000)
                        .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
                        .withInterruptible(false)
                        .start();

                // creates an animation which crops from bottom up

                ValueAnimator va1 =
                        AnimationUtil.getTopBottomPanelValueAnimator(mTopPanel, 200);

                ValueAnimator va2 =
                        AnimationUtil.getTopBottomPanelValueAnimator(mBottomPanel, 200);

                ValueAnimator va3 =
                        AnimationUtil.getLeftRightPanelValueAnimator(mLeftPanel, 20);

                ValueAnimator va4 =
                        AnimationUtil.getLeftRightPanelValueAnimator(mRightPanel, 20);

                AnimatorSet set = new AnimatorSet();
                set.setDuration(1000);
                set.playTogether(va1, va2, va3, va4);
                set.start();

            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                PointF sCoord = mImageView.viewToSourceCoord(e.getX(), e.getY());

                init();

                return true;
            }
        });

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
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
