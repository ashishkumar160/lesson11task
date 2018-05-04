package com.ashish.lesson11task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.ashish.lesson11task.utils.NetworkUtils;
import com.ashish.lesson11task.utils.NotificationUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

public class ImageDownloadTask {

    public static final String ACTION_DOWNLOAD_IMAGE = "action_download_image";

    public static final String ACTION_DISMISS_NOTIFICATIONS = "action_dismiss_notifications";

    public static void executeTask(Context context, URL imageUrl, String imageName, String action) {
        if (ACTION_DOWNLOAD_IMAGE.equals(action)) {
            Bitmap bitmap = downloadImage(context, imageUrl);
            if (bitmap!=null) {
                File file = saveImage(bitmap, imageName);
                NotificationUtils.clearAllNotifications(context);
                NotificationUtils.showDownloadedNotification(context, file);
            }
        } else if (ACTION_DISMISS_NOTIFICATIONS.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        }
    }

    private static Bitmap downloadImage(Context context, URL imageUrl) {
        Bitmap bitmap = null;
        try {
            bitmap = NetworkUtils.getBitmapResponseFromHttpUrl(context, imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static File saveImage(Bitmap bitmap, String imageName) {
        final Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
        final File imageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "lesson11taskimages" + File.separator + imageName + "." + mFormat.name().toLowerCase());
        File parent = imageFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            imageFile.createNewFile();
            fos = new FileOutputStream(imageFile);
            bitmap.compress(mFormat, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

}
