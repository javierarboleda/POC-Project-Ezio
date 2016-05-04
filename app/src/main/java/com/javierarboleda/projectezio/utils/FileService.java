package com.javierarboleda.projectezio.utils;

import java.io.File;

/**
 * Created by javierarboleda on 4/15/16.
 */
public class FileService {

    static public File[] getFilesInDir(String dirPath) {
        File file = new File(dirPath);
        return file.listFiles();
    }

    static public String getListOfFileNamesInDir(String dirPath) {
        StringBuffer fileNames = new StringBuffer();
        fileNames.append("\n");
        for (File file : getFilesInDir(dirPath)) {
            fileNames.append(file.getName()+"\n");
        }
        return fileNames.toString();
    }

}
