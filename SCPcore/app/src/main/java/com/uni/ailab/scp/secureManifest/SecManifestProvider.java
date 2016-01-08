package com.uni.ailab.scp.secureManifest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.uni.ailab.scp.receiver.SQLiteHelper;

import java.util.Collection;

public class SecManifestProvider extends ContentProvider {
    private static final String AUTHORITIES = "com.uni.ailab.scp.secureManifest";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITIES+"/components");

    private SQLiteHelper sqLiteHelper;

    public SecManifestProvider() {
    }

    @Override
    public boolean onCreate() {
        sqLiteHelper = new SQLiteHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String xmlSecureManifest = values.getAsString("securemanifest");
        SecureManifestReader smr = new SecureManifestReader(xmlSecureManifest);
        Collection<Component> components = smr.getComponents();
        for (Component component : components)
            component.storeIntoDB(sqLiteHelper);
        return CONTENT_URI;
    }




    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
