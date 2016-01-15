package com.uni.ailab.scp.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


public class ProxyProvider extends ContentProvider {
    private static final String AUTHORITIES = "com.uni.ailab.scp.ProxyProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITIES+"/");

    public ProxyProvider() {}
    @Override
    public boolean onCreate() {
        return false;
    }

    private static class URIquery {
        private String uri;
        private String component;

        public URIquery(String component, String uri) {
            this.component = component;
            this.uri = uri;
        }

        public Uri getUri() {
            return Uri.parse(uri);
        }
    }

    private static URIquery parseRequest(Uri uri) {
        String request = uri.toString();
        request = request.substring(request.indexOf('?'));
        String[] pairs = request.split("&");
        return new URIquery(pairs[0].replace("?component=",""), Uri.decode(pairs[1].replace("uri=","")));
    }

    private static boolean canQuery(URIquery uq) {
        //TODO given component and uri ask if the operation can be performed
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        URIquery uq = parseRequest(uri);
        if (canQuery(uq)) {
            return getContext().getContentResolver().query(uq.getUri(), projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        URIquery uq = parseRequest(uri);
        if (canQuery(uq)) {
            return getContext().getContentResolver().getType(uq.getUri());
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        URIquery uq = parseRequest(uri);
        if (canQuery(uq)) {
            ContentResolver cr = getContext().getContentResolver();
            return cr.insert(uq.getUri(), values);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        URIquery uq = parseRequest(uri);
        if (canQuery(uq)) {
            return getContext().getContentResolver().delete(uq.getUri(), selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        URIquery uq = parseRequest(uri);
        if (canQuery(uq)) {
            return getContext().getContentResolver().update(uq.getUri(), values, selection, selectionArgs);
        }
        return 0;
    }
}
