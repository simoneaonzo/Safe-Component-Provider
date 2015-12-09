package com.uni.ailab.scp.provider;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.uni.ailab.scp.MainActivity;

public class AuthProvider extends ContentProvider {
    static final String URL = "content://com.uni.ailab.scp.provider.AuthProvider/askPerm";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static Context context;

    @Override
    public boolean onCreate() {
        context = getContext();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String[] columns = new String[] { "_id", "item", "description" };
        MatrixCursor matrixCursor= new MatrixCursor(columns);
        matrixCursor.addRow(new Object[] { 1, "item1", "description1" });
        matrixCursor.addRow(new Object[] { 1, "item2", "description2" });
        //Context c = getContext();
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return matrixCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
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
}
