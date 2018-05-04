package com.ashish.lesson11task.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.ashish.lesson11task.DownloadImageIntentService;
import com.ashish.lesson11task.ImageDownloadTask;
import com.ashish.lesson11task.R;
import com.ashish.lesson11task.view.FullscreenActivity;

import java.io.File;

public class NotificationUtils {

    private static final int DOWNLOADED_NOTIFICATION_ID = 1138;

    private static final int PROGRESS_BAR_NOTIFICATION_ID = 1132;

    private static final int VIEW_DOWNLOADED_IMAGE_PENDING_INTENT_ID = 3417;

    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 10;

    private static final String DOWNLOADED_NOTIFICAION_CHANNEL_ID = "downloaded_notification_channel";

    private static final String PROGRESS_NOTIFICATION_CHANNEL_ID = "progress_notification_channel";

    private static NotificationCompat.Builder progressNotificationBuilder = null;

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void showProgressBarNotification(Context context, int PROGRESS_MAX, int PROGRESS_CURRENT) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    PROGRESS_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.progress_bar_notification_channel),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        progressNotificationBuilder =
                new NotificationCompat.Builder(context, PROGRESS_NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Picture Download")
                .setContentText("Download in Progress")
                .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        progressNotificationBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager.notify(PROGRESS_BAR_NOTIFICATION_ID, progressNotificationBuilder.build());
    }

    public static void showDownloadedNotification(Context context, File file) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    DOWNLOADED_NOTIFICAION_CHANNEL_ID,
                    context.getString(R.string.download_image_notification_channel),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, DOWNLOADED_NOTIFICAION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Lesson 11 task")
                .setContentText("Image has been downloaded")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        "Image has been downloaded"
                )).setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context, file))
                        .addAction(viewDownloadedImageAction(context, file))
                        .addAction(ignoreDownloadImageAction(context))
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(DOWNLOADED_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static NotificationCompat.Action ignoreDownloadImageAction(Context context) {
        Intent ignoreDownloadImageIntent = new Intent(context, DownloadImageIntentService.class);
        ignoreDownloadImageIntent.setAction(ImageDownloadTask.ACTION_DISMISS_NOTIFICATIONS);
        PendingIntent ignoreDownloadImagePendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreDownloadImageIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Action ignoreDownloadImageAction = new NotificationCompat.Action(R.drawable.ic_cancel_black_24dp,
                "Ignore",
                ignoreDownloadImagePendingIntent);
        return ignoreDownloadImageAction;
    }

    private static NotificationCompat.Action viewDownloadedImageAction(Context context,File file) {
        NotificationCompat.Action viewDownloadedImageAction = new NotificationCompat.Action(R.drawable.ic_open_in_new_black_24dp,
                "Open",
                contentIntent(context, file));
        return viewDownloadedImageAction;
    }

    private static PendingIntent contentIntent(Context context, File imageFile) {
        Intent startActivityIntent = new Intent(context, FullscreenActivity.class);
        startActivityIntent.putExtra("imageFile", imageFile);
        return PendingIntent.getActivity(
                context,
                VIEW_DOWNLOADED_IMAGE_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_file_download_black_24dp);
        return largeIcon;
    }

}
