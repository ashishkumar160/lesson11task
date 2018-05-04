package com.ashish.lesson11task.utils;

import android.os.Environment;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ashish.lesson11task.view.ImageAdapter;

import java.io.File;

public class DirectoryFileObserver extends FileObserver {

    private String folderPath;

    private ImageAdapter.ListItemUpdateListener itemUpdateListener;

    public DirectoryFileObserver(String path, ImageAdapter.ListItemUpdateListener listener) {
        super(path, FileObserver.CREATE);
        this.itemUpdateListener = listener;
        this.folderPath = path;
    }

    @Override
    public void onEvent(int event, @Nullable String path) {
        if (path != null) {
            itemUpdateListener.onListUpdate(new File(folderPath + "/" + path));
        }
    }
}
