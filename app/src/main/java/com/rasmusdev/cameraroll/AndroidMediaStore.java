package com.rasmusdev.cameraroll;

import android.app.Activity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.ArrayList;


public class AndroidMediaStore {
    private Activity mContext;
    private ArrayList<GalleryItem> imagePaths = new ArrayList<>();
    boolean dataChanged = false;
    private static final String[] projection = new String[]{
            MediaStore.Files.FileColumns.DATA,};

    /**
     * @param context
     */
    AndroidMediaStore(final Activity context) {
        mContext = context;
    }



    void getImages(final OnTaskCompleted listener) {

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(
                mContext,
                queryUri,
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );

        final Cursor mediaCursor = cursorLoader.loadInBackground();
        imagePaths = new ArrayList<>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (mediaCursor.moveToFirst()) {
                    String filePath;
                    GalleryItem item;
                    int pathColumn = mediaCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

                    do {
                        filePath = mediaCursor.getString(pathColumn);
                        item = new GalleryItem(filePath);
                        imagePaths.add(item);
                    } while (mediaCursor.moveToNext());

                    mediaCursor.close();

                    listener.onTaskCompleted(imagePaths);
                }
            }
        });
    }
}