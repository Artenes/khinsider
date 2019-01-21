package com.artenesnogueira.khinsider.tasks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.api.model.File;
import com.artenesnogueira.khinsider.api.model.Format;
import com.artenesnogueira.khinsider.api.model.Song;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadSongTask extends AsyncTask<Void, Void, Void> {

    private static int NOTIFICATION_ID_COUNTER = 1;

    private final Context mContext;
    private Song mSong;
    NotificationCompat.Builder mBuilder;
    private final static String CHANNEL_ID = "DOWNLOAD_SONG";
    private final int mNotificationID;

    public DownloadSongTask(Context context, Song song) {
        mContext = context;
        mSong = song;
        mNotificationID = ++NOTIFICATION_ID_COUNTER;

        createNotificationChannel();

        mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Downloading " + song.getName())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(0, 0, true)
                .setOngoing(true);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        NotificationManagerCompat.from(mContext).notify(mNotificationID, mBuilder.build());

        int count;

        try {

            URL url = new URL(mSong.getFiles().get(Format.MP3).getUrl());

            URLConnection connection = url.openConnection();

            connection.connect();

            int lengthOfFile = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream(), 8129);

            String path = Environment.getExternalStorageDirectory().toString() + "/khinsider/";

            java.io.File file = new java.io.File(path);

            file.mkdirs();

            OutputStream output = new FileOutputStream(path + mSong.getName() + ".mp3");

            byte data[] = new byte[1024];

            int total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
                mBuilder.setProgress(lengthOfFile, total, false);
                NotificationManagerCompat.from(mContext).notify(mNotificationID, mBuilder.build());
            }

            output.flush();

            output.close();
            input.close();

            mBuilder.setProgress(1, 1, false);
            mBuilder.setContentText("Download complete").setOngoing(false);
            NotificationManagerCompat.from(mContext).notify(mNotificationID, mBuilder.build());

        } catch (Exception exception) {

            mBuilder.setProgress(1, 1, false);
            mBuilder.setContentText("Download failed").setOngoing(false);
            NotificationManagerCompat.from(mContext).notify(mNotificationID, mBuilder.build());
            exception.printStackTrace();

        }

        return null;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Download songs channel";
            String description = "Notification about the song being downloaded";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
