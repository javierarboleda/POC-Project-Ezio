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
                else {
                    playTestPanels();
                }
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void playTestPanels() {

        float w = mImageView.getSWidth();
        float h = mImageView.getSHeight();

        Panel panel1 = new Panel(
                new PointF(0.0427797f, 0.067912504f),
                new PointF(0.35272756f, 0.067912504f),
                new PointF(0.0427797f, 0.33226973f),
                new PointF(0.35272756f, 0.33226973f),
                new PointF(0.0427797f, 0f),
                new PointF(0.35272756f, 0f),
                new PointF(0f, 0.105251096f),
                new PointF(0f, 0.29493114f),
                3.7827604f);
        Panel panel2 = new Panel(
                new PointF(0.3935239f, 0.034182917f),
                new PointF(0.8306046f, 0.034182917f),
                new PointF(0.3935239f, 0.4069729f),
                new PointF(0.8306046f, 0.4069729f),
                new PointF(0.45422956f, 0f),
                new PointF(0.769899f, 0f),
                new PointF(0f, 0.034182917f),
                new PointF(0f, 0.4069729f),
                2.6824758f);

        mSavedPanels.add(panel1);
        mSavedPanels.add(panel2);

//        Panel panel1 = new Panel(new PointF(-0.08622924f, 0.0f), new PointF(1.0862293f, 0.0f),
//                new PointF(-0.08622924f, 1.0f), new PointF(1.0862293f, 1.0f),
//                new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), 1.0f);
//        Panel panel2 = new Panel(new PointF(-0.016998665f, 0.03454772f),
//                new PointF(0.42091873f, 0.03454772f), new PointF(-0.016998665f, 0.40805125f),
//                new PointF(0.42091873f, 0.40805125f), new PointF(0.032647118f, 0f), new PointF(0.37127295f, 0f), new PointF(0f, 0.03651141f), new PointF(0f, 0.40608755f), 2.7058024f);
//        Panel panel3 = new Panel(new PointF(0.46769738f, 0.019759871f),
//                new PointF(0.9539177f, 0.019759871f), new PointF(0.46769738f, 0.4344614f),
//                new PointF(0.9539177f, 0.4344614f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), 2.4659185f);
//        Panel panel4= new Panel(new PointF(0.065099634f, 0.40284166f),
//                new PointF(0.3839439f, 0.40284166f), new PointF(0.065099634f, 0.6747867f),
//                new PointF(0.3839439f, 0.6747867f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), 3.6363726f);
//        Panel panel5 = new Panel(new PointF(0.40621263f, 0.4091911f),
//                new PointF(0.7017848f, 0.4091911f), new PointF(0.40621263f, 0.6612872f),
//                new PointF(0.7017848f, 0.6612872f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), 4.049073f);
//        Panel panel6 = new Panel(new PointF(0.6743034f, 0.4081206f),
//                new PointF(0.9698756f, 0.4081206f), new PointF(0.6743034f, 0.6602167f),
//                new PointF(0.9698756f, 0.6602167f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), 4.049073f);
//        Panel panel7 = new Panel(new PointF(0.29906428f, 0.65833914f),
//                new PointF(0.6652556f, 0.65833914f), new PointF(0.29906428f, 0.9706668f),
//                new PointF(0.6652556f, 0.9706668f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), new PointF(0f, 0f), 3.2577322f);
//
//        mSavedPanels.add(panel2);
//        mSavedPanels.add(panel3);
//        mSavedPanels.add(panel4);
//        mSavedPanels.add(panel5);
//        mSavedPanels.add(panel6);
//        mSavedPanels.add(panel7);
//        mSavedPanels.add(panel1);

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
        y = 0;
        PointF topLeftSourcePointF = mImageView.viewToSourceCoord(x, y);
        topLeftSourcePointF.x =
                topLeftSourcePointF.x / (float)mImageView.getSWidth();
        topLeftSourcePointF.y =
                topLeftSourcePointF.y / (float)mImageView.getSHeight();

        x = mImageView.getWidth();
        PointF topRightSourcePointF = mImageView.viewToSourceCoord(x, y);
        topRightSourcePointF.x =
                topRightSourcePointF.x / (float)mImageView.getSWidth();
        topRightSourcePointF.y =
                topRightSourcePointF.y / (float)mImageView.getSHeight();

        x = 0;
        y = mImageView.getHeight();
        PointF bottomLeftSourcePointF = mImageView.viewToSourceCoord(x, y);
        bottomLeftSourcePointF.x =
                bottomLeftSourcePointF.x / (float)mImageView.getSWidth();
        bottomLeftSourcePointF.y =
                bottomLeftSourcePointF.y / (float)mImageView.getSHeight();

        x = mImageView.getWidth();
        PointF bottomRightSourcePointF = mImageView.viewToSourceCoord(x, y);
        bottomRightSourcePointF.x =
                bottomRightSourcePointF.x / (float)mImageView.getSWidth();
        bottomRightSourcePointF.y =
                bottomRightSourcePointF.y / (float)mImageView.getSHeight();

        scale = mImageView.getScale() / mOriginalScale;

        String debugText = "TL: " + topLeftSourcePointF.x + ", " + topLeftSourcePointF.y +
                " TR: " + topRightSourcePointF.x + ", " + topRightSourcePointF.y +
                " BL: " + bottomLeftSourcePointF.x + ", " + bottomLeftSourcePointF.y +
                " BR: " + bottomRightSourcePointF.x + ", " + bottomRightSourcePointF.y;

        mCoordTextView.setText(debugText);

        // get left panel x, and the percentage x point on image
        x = mLeftPanel.getWidth();
        PointF leftPanePointF = mImageView.viewToSourceCoord(x, 0);
        leftPanePointF.x =
                leftPanePointF.x / (float) mImageView.getSWidth();

        // get right panel x, and the percentage x point on image
        x = mImageView.getWidth() - mRightPanel.getWidth();
        PointF rightPanePointF = mImageView.viewToSourceCoord(x, 0);
        rightPanePointF.x =
                rightPanePointF.x / (float) mImageView.getSWidth();

        // get right panel x, and the percentage x point on image
        y = mTopPanel.getHeight();
        PointF topPanePointF = mImageView.viewToSourceCoord(0, y);
        topPanePointF.y =
                topPanePointF.y / (float) mImageView.getSHeight();

        // get right panel x, and the percentage x point on image
        y = mImageView.getHeight() - mBottomPanel.getHeight();
        PointF bottomPanePointF = mImageView.viewToSourceCoord(0, y);
        bottomPanePointF.y =
                bottomPanePointF.y / (float) mImageView.getSHeight();

        Panel panel = new Panel(topLeftSourcePointF, topRightSourcePointF,
                bottomLeftSourcePointF, bottomRightSourcePointF, leftPanePointF, rightPanePointF,
                topPanePointF, bottomPanePointF, scale);

        mSavedPanels.add(panel);

        Log.d("SavedPanelImage",
                panel.getTopLeft() + " : " +
                panel.getTopRight() + " : " +
                panel.getBottomLeft() + " : " +
                panel.getBottomRight() + " : " +
                panel.getScale()
            );
        Log.d("SavedPanelBorders",
                panel.getLeftPane().x + " : " +
                panel.getRightPane().x + " : " +
                panel.getTopPane().y + " : " +
                panel.getBottomPane().y
            );
    }

    private void animateToPanel(final Panel panel) {

        final PointF midPointPercentage = panel.getMidpoint();
        final PointF midPointCoord = new PointF(midPointPercentage.x * mImageView.getSWidth(),
                midPointPercentage.y * mImageView.getSHeight());
        final float scale = panel.getScale() * mOriginalScale;

        PointF midPointView = mImageView.sourceToViewCoord(midPointCoord);

        final float screenRatio = (float)mImageView.getWidth() / (float)mImageView.getHeight();

        mImageView.animateScaleAndCenter(scale, midPointCoord)
                .withDuration(1000)
                .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
                .withInterruptible(false).withOnAnimationEventListener(new SubsamplingScaleImageView.OnAnimationEventListener() {
            @Override
            public void onComplete() {
                PointF midPointView = mImageView.sourceToViewCoord(midPointCoord);
                Log.d("AnimateToPanel", "final mpv:" + midPointView);

                float leftPerc = panel.getLeftPane().x - panel.getTopLeft().x;
                float rightPerc = panel.getTopRight().x - panel.getRightPane().x;
                float topPerc = panel.getTopPane().y - panel.getTopRight().y;
                float bottomPerc = panel.getBottomRight().y - panel.getBottomPane().y;

//        int left = (int) (mImageView.getWidth() *
//                ((panel.getLeftPane().x - panel.getTopLeft().x) * screenRatio));

                int left = (int) mImageView.sourceToViewCoord(
                        panel.getLeftPane().x * mImageView.getSWidth(), 0).x;
                int right = mImageView.getWidth() - (int) mImageView.sourceToViewCoord(
                        panel.getRightPane().x * mImageView.getSWidth(), 0).x;
                int top = (int) mImageView.sourceToViewCoord(
                        0, panel.getTopPane().y * mImageView.getSHeight()).y;
                int bottom = mImageView.getHeight() - (int) mImageView.sourceToViewCoord(
                        0, panel.getBottomPane().y * mImageView.getSHeight()).y;

                animateBorderPanels(left, right, top, bottom);

                Log.d("AnimateToPanel", "mpp:" + midPointPercentage + " mpc:" + midPointCoord + " scale:"
                        + scale + " mpv:" + midPointView + " ratio:" + screenRatio);

                Log.d("AnimateToPanel", "LP:" + leftPerc + " RP:" + rightPerc + " TP:" + topPerc +
                        " BP:" + bottomPerc);

                Log.d("AnimateToPanel", "L:" + left + " R:" + right + " T:" + top + " B:" + bottom);
            }

            @Override
            public void onInterruptedByUser() {

            }

            @Override
            public void onInterruptedByNewAnim() {

            }
        }).start();


//        float leftPerc = panel.getLeftPane().x - panel.getTopLeft().x;
//        float rightPerc = panel.getTopRight().x - panel.getRightPane().x;
//        float topPerc = panel.getTopPane().y - panel.getTopRight().y;
//        float bottomPerc = panel.getBottomRight().y - panel.getBottomPane().y;
//
////        int left = (int) (mImageView.getWidth() *
////                ((panel.getLeftPane().x - panel.getTopLeft().x) * screenRatio));
//
//        int left = (int) mImageView.sourceToViewCoord(
//                panel.getLeftPane().x * mImageView.getSWidth(), 0).x;
//        int right = mImageView.getWidth() - (int) mImageView.sourceToViewCoord(
//                panel.getRightPane().x * mImageView.getSWidth(), 0).x;
//        int top = (int) mImageView.sourceToViewCoord(
//                0, panel.getTopPane().y * mImageView.getSHeight()).y;
//        int bottom = mImageView.getHeight() - (int) mImageView.sourceToViewCoord(
//                0, panel.getBottomPane().y * mImageView.getSHeight()).y;
//
//        animateBorderPanels(left, right, top, bottom);
//
//        Log.d("AnimateToPanel", "mpp:" + midPointPercentage + " mpc:" + midPointCoord + " scale:"
//                + scale + " mpv:" + midPointView + " ratio:" + screenRatio);
//
//        Log.d("AnimateToPanel", "LP:" + leftPerc + " RP:" + rightPerc + " TP:" + topPerc +
//                " BP:" + bottomPerc);
//
//        Log.d("AnimateToPanel", "L:" + left + " R:" + right + " T:" + top + " B:" + bottom);

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

//        mImageView.setImage(ImageSource.resource(R.drawable.sample_image));

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

                animateBorderPanels(0, 0, 200, 200);

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

    private void animateBorderPanels(int left, int right, int top, int bottom) {
        ValueAnimator va1 =
                AnimationUtil.getTopBottomPanelValueAnimator(mTopPanel, top);

        ValueAnimator va2 =
                AnimationUtil.getTopBottomPanelValueAnimator(mBottomPanel, bottom);

        ValueAnimator va3 =
                AnimationUtil.getLeftRightPanelValueAnimator(mLeftPanel, left);

        ValueAnimator va4 =
                AnimationUtil.getLeftRightPanelValueAnimator(mRightPanel, right);

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
