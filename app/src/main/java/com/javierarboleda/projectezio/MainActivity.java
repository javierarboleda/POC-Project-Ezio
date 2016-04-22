package com.javierarboleda.projectezio;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.javierarboleda.projectezio.comic.FileService;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ask for permission

        String path = Environment.getExternalStorageDirectory().toString();
        Log.d("Files", "Path: " + path);
        String rootPath = Environment.getRootDirectory().getPath();
        Log.d("Files", "RootPath: " + rootPath);
        String dataPath = Environment.getDataDirectory().getPath();
        Log.d("Files", "DataPath: " + dataPath);
        File f = new File(path + "/comics");
        File[] files = FileService.getFilesInDir(path);
        for (File file : files) {
            Log.d("Files", file.getName());
        }
        Log.d("Files", "num of files = " + files.length);

    }


}
