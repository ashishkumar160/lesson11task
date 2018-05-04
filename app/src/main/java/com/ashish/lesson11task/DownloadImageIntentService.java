package com.ashish.lesson11task;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.net.URL;

public class DownloadImageIntentService extends IntentService {

    public DownloadImageIntentService() {
        super("DownloadImageIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        ImageDownloadTask.executeTask(this,
                (URL) intent.getSerializableExtra("imageUrl"),
                intent.getStringExtra("imageName"), action);
    }
}
