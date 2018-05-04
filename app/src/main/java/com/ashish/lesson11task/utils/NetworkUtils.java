package com.ashish.lesson11task.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static URL buildUrl(String uri) throws MalformedURLException {
        return new URL(uri);
    }

    public static Bitmap getBitmapResponseFromHttpUrl(Context context, URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        Bitmap bitmap = null;
        try {
            urlConnection.connect();
            final int length = urlConnection.getContentLength();

            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream(), 8192);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte bytes[] = new byte[8192];
            int count;
            long read = 0;
            while ((count = in.read(bytes)) != -1) {
                read += count;
                out.write(bytes, 0, count);
                NotificationUtils.showProgressBarNotification(context, 100, (int) ((read * 100) / length) ) ;
            }
            bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
        } finally {
            urlConnection.disconnect();
        }
        return bitmap;
    }

}
