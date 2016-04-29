package com.javierarboleda.projectezio;

import android.graphics.PointF;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.javierarboleda.projectezio.comic.FileService;

public class ComicActivity extends AppCompatActivity {

    SubsamplingScaleImageView mImageView;
    float mX;
    float mY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);

        testLoadImage();
    }

    private void testLoadImage() {

        mImageView =
                (SubsamplingScaleImageView) findViewById(R.id.comicImageView);

        mImageView.setImage(ImageSource.uri(
                "/storage/emulated/0/comics/Batman Cacophony 01 (of 03) (2009) (3 covers)" +
                        " (digital) (Minutemen-PhD)/Batman- Cacophony 001-000.jpg"));

        // imageView.startAnimation(someAnimation);

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mX = event.getX();
                mY = event.getY();

                // todo: add an overlaying TextView showing coordinates

                return false;
            }
        });

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mImageView.animateScaleAndCenter(1.5f, new PointF(mX + mX/2, mY + mY/2))
                        .withDuration(2000)
                        .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
                        .withInterruptible(false)
                        .start();

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
