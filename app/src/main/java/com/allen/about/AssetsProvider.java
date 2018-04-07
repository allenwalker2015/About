package com.allen.about;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by elect on 06/04/2018.
 */


//Many apps require you to provide name and size of the image. So here is an improved code (using Google's FileProvider code as an example):
public class AssetsProvider extends ContentProvider {

    private final static String LOG_TAG = AssetsProvider.class.getName();

    private static final String[] COLUMNS = {
            OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE };

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        /**
         * Source: {@link FileProvider#query(Uri, String[], String, String[], String)} .
         */
        if (projection == null) {
            projection = COLUMNS;
        }

        final AssetManager am = getContext().getAssets();
        final String path = getRelativePath(uri);
        long fileSize = 0;
        try {
            final AssetFileDescriptor afd = am.openFd(path);
            fileSize = afd.getLength();
            afd.close();
        } catch(IOException e) {
            Log.e(LOG_TAG, "Can't open asset file", e);
        }

        final String[] cols = new String[projection.length];
        final Object[] values = new Object[projection.length];
        int i = 0;
        for (String col : projection) {
            if (OpenableColumns.DISPLAY_NAME.equals(col)) {
                cols[i] = OpenableColumns.DISPLAY_NAME;
                values[i++] = uri.getLastPathSegment();
            } else if (OpenableColumns.SIZE.equals(col)) {
                cols[i] = OpenableColumns.SIZE;
                values[i++] = fileSize;
            }
        }

        final MatrixCursor cursor = new MatrixCursor(cols, 1);
        cursor.addRow(values);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        /**
         * Source: {@link FileProvider#getType(Uri)} .
         */
        final String file_name = uri.getLastPathSegment();
        final int lastDot = file_name.lastIndexOf('.');
        if (lastDot >= 0) {
            final String extension = file_name.substring(lastDot + 1);
            final String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mime != null) {
                return mime;
            }
        }

        return "application/octet-stream";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        final AssetManager am = getContext().getAssets();
        final String path = getRelativePath(uri);
        if(path == null) {
            throw new FileNotFoundException();
        }
        AssetFileDescriptor afd = null;
        try {
            afd = am.openFd(path);
        } catch(IOException e) {
            Log.e(LOG_TAG, "Can't open asset file", e);
        }
        return afd;
    }

    private String getRelativePath(Uri uri) {
        String path = uri.getPath();
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }
        return path;
    }
}
