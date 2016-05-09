package com.javierarboleda.projectezio;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.javierarboleda.projectezio.comic.Panel;
import com.javierarboleda.projectezio.utils.AnimationUtil;
import com.javierarboleda.projectezio.utils.FileService;

import java.util.ArrayList;

public class ComicActivity extends AppCompatActivity {

    private SubsamplingScaleImageView mImageView;
    private TextView mCoordTextView;
    private View mTopPanel;
    private View mBottomPanel;
    private View mLeftPanel;
    private View mRightPanel;
    private Menu mMenu;
    private int mUnitSize;
    private String mActivePanels;
    private ArrayList<Panel> mSavedPanels;
    private float mOriginalScale;
    private int mPanelPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comic_activity, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.top_bottom_side_toggle:
                toggleActivePanel();
                break;
            case R.id.unitaction_bar_button:
                toggleUnitSize();
                break;
            case R.id.plus_action_bar_button:
                updatePanelSize(1);
                break;
            case R.id.minus_action_bar_button:
                updatePanelSize(-1);
                break;
            case R.id.add_panel_action_bar_button:
                savePanel();
                break;
            case R.id.play_action_bar_button:
                if (!mSavedPanels.isEmpty()) {
                    animateToPanel(mSavedPanels.get(mPanelPosition));
                    mPanelPosition++;
                    if (mPanelPosition == mSavedPanels.size()) {
                        mPanelPosition = 0;
                    }
                }
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        setContentView(R.layout.activity_comic);

        mCoordTextView = (TextView) findViewById(R.id.text);
        mTopPanel = findViewById(R.id.topPanel);
        mBottomPanel = findViewById(R.id.bottomPanel);
        mLeftPanel = findViewById(R.id.leftPanel);
        mRightPanel = findViewById(R.id.rightPanel);
        mUnitSize = 50;
        mActivePanels = "B/T";
        mSavedPanels = new ArrayList<>();
        mPanelPosition = 0;

        testLoadImage();
    }

    private void savePanel() {

        float x, y, scale;

        x = 0;
        y = mTopPanel.getHeight();
        PointF topLeftSourcePointF = mImageView.viewToSourceCoord(x, y);

        x = mImageView.getWidth();
        PointF topRightSourcePointF = mImageView.viewToSourceCoord(x, y);

        x = 0;
        y = mImageView.getHeight() - mBottomPanel.getHeight();
        PointF bottomLeftSourcePointF = mImageView.viewToSourceCoord(x, y);

        x = mImageView.getWidth();
        PointF bottomRightSourcePointF = mImageView.viewToSourceCoord(x, y);

        scale = mImageView.getScale() / mOriginalScale;

        String debugText = "TL: " + topLeftSourcePointF.x + ", " + topLeftSourcePointF.y +
                " TR: " + topRightSourcePointF.x + ", " + topRightSourcePointF.y +
                " BL: " + bottomLeftSourcePointF.x + ", " + bottomLeftSourcePointF.y +
                " BR: " + bottomRightSourcePointF.x + ", " + bottomRightSourcePointF.y;

        mCoordTextView.setText(debugText);

        Panel panel = new Panel(topLeftSourcePointF, topRightSourcePointF,
                bottomLeftSourcePointF, bottomRightSourcePointF, scale);

        mSavedPanels.add(panel);

    }

    private void animateToPanel(Panel panel) {

        PointF midPoint = panel.getMidpoint();
        float scale = panel.getScale() * mOriginalScale;

        mImageView.animateScaleAndCenter(scale, midPoint)
                .withDuration(1000)
                .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
                .withInterruptible(false)
                .start();

    }

    private PointF getMidpoint(PointF a, PointF b) {

        float x = (a.x + b.x) / 2;
        float y = (a.y + b.y) / 2;

        return new PointF(x, y);
    }

    private void updatePanelSize(int posOrNeg) {
        switch (mActivePanels) {
            case "B/T":
                mTopPanel.getLayoutParams().height += mUnitSize * posOrNeg;
                mBottomPanel.getLayoutParams().height += mUnitSize * posOrNeg;
                mTopPanel.requestLayout();
                mBottomPanel.requestLayout();
                break;
            case "L/R":
                mLeftPanel.getLayoutParams().width += mUnitSize * posOrNeg;
                mRightPanel.getLayoutParams().width += mUnitSize * posOrNeg;
                mLeftPanel.requestLayout();
                mRightPanel.requestLayout();
                break;
        }
    }

    private void toggleUnitSize() {
        switch (mUnitSize) {
            case 50:
                mUnitSize = 10;
                break;
            case 10:
                mUnitSize = 5;
                break;

            case 5:
                mUnitSize = 1;
                break;
            default:
                mUnitSize = 50;
                break;
        }
        mMenu.findItem(R.id.unitaction_bar_button).setTitle(String.valueOf(mUnitSize));
    }

    private void toggleActivePanel() {
        switch (mActivePanels) {
            case "B/T":
                mActivePanels = "L/R";
                break;
            default:
                mActivePanels = "B/T";
                break;
        }
        mMenu.findItem(R.id.top_bottom_side_toggle).setTitle(mActivePanels);
    }

    private void testLoadImage() {

        mImageView =
                (SubsamplingScaleImageView) findViewById(R.id.comicImageView);

        mImageView.setImage(ImageSource.uri(
                "/storage/emulated/0/comics/Batman Cacophony 01 (of 03) (2009) (3 covers)" +
                        " (digital) (Minutemen-PhD)/Batman- Cacophony 001-023.jpg"));

        mImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_OUTSIDE);

        mImageView.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onImageLoaded() {
                mOriginalScale = mImageView.getScale();
            }

            @Override
            public void onPreviewLoadError(Exception e) {

            }

            @Override
            public void onImageLoadError(Exception e) {

            }

            @Override
            public void onTileLoadError(Exception e) {

            }
        });

        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                PointF sCoord = mImageView.viewToSourceCoord(e.getX(), e.getY());

                String imageCoordinates = "image coord: " + sCoord.x + ", " + sCoord.y;

                String scale = "scale: " + mImageView.getScale();

                String imageViewCoordinates = "view coord: " + e.getX() + ", " + e.getY();

                String text = imageCoordinates + " " + scale + " " + imageViewCoordinates;

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

                animateBorderPanels();

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

    private void animateBorderPanels() {
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

    private void testLogFileNames() {
        String path = Environment.getExternalStorageDirectory().getPath() +
                "/comics/Batman Cacophony 01 (of 03) (2009) (3 covers) (digital) (Minutemen-PhD)";
        String fileNames = FileService.getListOfFileNamesInDir(path);

        Log.d("Files", fileNames);
    }

}
