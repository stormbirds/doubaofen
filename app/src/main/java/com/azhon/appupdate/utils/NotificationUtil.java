package com.azhon.appupdate.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.manager.DownloadManager;
import com.azhon.appupdate.service.DownloadService;
import java.io.File;

public final class NotificationUtil {
    private static NotificationCompat.Builder builderNotification(Context context, int i, String str, String str2) {
        return new NotificationCompat.Builder(context, Build.VERSION.SDK_INT >= 26 ? getNotificationChannelId() : "").setSmallIcon(i).setContentTitle(str).setWhen(System.currentTimeMillis()).setContentText(str2).setAutoCancel(false).setOngoing(true);
    }

    public static void showNotification(Context context, int i, String str, String str2) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            afterO(notificationManager);
        }
        notificationManager.notify(DownloadManager.getInstance().getConfiguration().getNotifyId(), builderNotification(context, i, str, str2).setDefaults(1).build());
    }

    public static void showProgressNotification(Context context, int i, String str, String str2, int i2, int i3) {
        ((NotificationManager) context.getSystemService("notification")).notify(DownloadManager.getInstance().getConfiguration().getNotifyId(), builderNotification(context, i, str, str2).setProgress(i2, i3, false).build());
    }

    public static void showDoneNotification(Context context, int i, String str, String str2, File file) {
        Uri uri;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        notificationManager.cancel(DownloadManager.getInstance().getConfiguration().getNotifyId());
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(268435456);
        intent.addCategory("android.intent.category.DEFAULT");
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, context.getPackageName(), file);
            intent.addFlags(1);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        Notification build = builderNotification(context, i, str, str2).setContentIntent(PendingIntent.getActivity(context, 0, intent, 268435456)).build();
        build.flags |= 16;
        notificationManager.notify(DownloadManager.getInstance().getConfiguration().getNotifyId(), build);
    }

    public static void showErrorNotification(Context context, int i, String str, String str2) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            afterO(notificationManager);
        }
        notificationManager.notify(DownloadManager.getInstance().getConfiguration().getNotifyId(), builderNotification(context, i, str, str2).setAutoCancel(true).setOngoing(false).setContentIntent(PendingIntent.getService(context, 0, new Intent(context, DownloadService.class), 134217728)).setDefaults(1).build());
    }

    private static void afterO(NotificationManager notificationManager) {
        UpdateConfiguration configuration = DownloadManager.getInstance().getConfiguration();
        NotificationChannel notificationChannel = configuration != null ? configuration.getNotificationChannel() : null;
        if (notificationChannel == null) {
            notificationChannel = new NotificationChannel(Constant.DEFAULT_CHANNEL_ID, Constant.DEFAULT_CHANNEL_NAME, 3);
            notificationChannel.enableLights(true);
            notificationChannel.setShowBadge(true);
        }
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private static String getNotificationChannelId() {
        NotificationChannel notificationChannel = DownloadManager.getInstance().getConfiguration().getNotificationChannel();
        if (notificationChannel == null) {
            return Constant.DEFAULT_CHANNEL_ID;
        }
        String id = notificationChannel.getId();
        if (TextUtils.isEmpty(id)) {
            return Constant.DEFAULT_CHANNEL_ID;
        }
        return id;
    }
}
