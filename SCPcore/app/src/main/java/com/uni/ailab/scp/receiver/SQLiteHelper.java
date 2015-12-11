package com.uni.ailab.scp.receiver;

import java.util.Arrays;
import java.util.Vector;

import com.uni.ailab.scp.cnf.Formula;
import com.uni.ailab.scp.policy.Permissions;
import com.uni.ailab.scp.policy.Policy;
import com.uni.ailab.scp.policy.Scope;
import com.uni.ailab.scp.runtime.Frame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_COMPONENTS = "components";
    public static final String TABLE_PERMISSIONS = "permissions";
    public static final String TABLE_INSTANCES = "instances";
    public static final String TABLE_POLICIES = "policies";
    public static final String TABLE_INSTANCES_PERMISSIONS = "instanceshavepermissions";
    public static final String TABLE_INSTANCES_POLICIES = "instanceshavepolicies";

    public static final String COLUMN_INSTANCE_ID = "instance_id";
    public static final String COLUMN_POLICY_ID = "policy_id";
    public static final String COLUMN_COMPNAME = "compname";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PERMNAME = "permname";
    public static final String COLUMN_ACTION = "action";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_STICKY = "sticky";
    public static final String COLUMN_SCOPE = "scope";
    public static final String COLUMN_FORMULA = "formula";

    private static final String DATABASE_NAME = "instances.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE_COMPONENTS = "create table "
            + TABLE_COMPONENTS + "(" +
            COLUMN_COMPNAME + " text primary key, " +
            COLUMN_TYPE + " text not null);";

    private static final String CREATE_TABLE_PERMISSIONS = "create table "
            + TABLE_PERMISSIONS + "(" +
            COLUMN_PERMNAME + " text primary key);";

    private static final String CREATE_TABLE_POLICIES = "create table "
            + TABLE_POLICIES + "(" +
            COLUMN_POLICY_ID + " integer primary key autoincrement, " +
            COLUMN_SCOPE + " text not null, " +
            COLUMN_STICKY + " integer not null, " +
            COLUMN_FORMULA + " text not null);";


    private static final String CREATE_TABLE_INSTANCES = "create table "
            + TABLE_INSTANCES + "(" +
            COLUMN_INSTANCE_ID + " integer primary key autoincrement, " +
            COLUMN_COMPNAME + " text not null, " +
            COLUMN_ACTION + " text not null," +
            COLUMN_DATA + " text not null," +
            "foreign key ("+COLUMN_COMPNAME+") references " + TABLE_COMPONENTS + '(' + COLUMN_COMPNAME + "));";

    private static final String CREATE_TABLE_INSTANCES_PERMISSIONS = "create table "
            + TABLE_INSTANCES_PERMISSIONS + "(" +
            COLUMN_INSTANCE_ID + " integer not null, " +
            COLUMN_PERMNAME + " text not null," +
            "foreign key ("+COLUMN_INSTANCE_ID+") references " + TABLE_INSTANCES + '(' + COLUMN_INSTANCE_ID + ')' +
            "foreign key ("+COLUMN_PERMNAME+") references " + TABLE_PERMISSIONS + '(' + COLUMN_PERMNAME + ')' +
            "primary key ("+COLUMN_INSTANCE_ID+','+ COLUMN_PERMNAME +"));";

    private static final String CREATE_TABLE_INSTANCES_POLICIES= "create table "
            + TABLE_INSTANCES_POLICIES + "(" +
            COLUMN_INSTANCE_ID + " integer not null, " +
            COLUMN_POLICY_ID + " integer not null," +
            "foreign key ("+COLUMN_INSTANCE_ID+") references " + TABLE_INSTANCES + '(' + COLUMN_INSTANCE_ID + ')' +
            "foreign key ("+COLUMN_POLICY_ID+") references " + TABLE_POLICIES + '(' + COLUMN_POLICY_ID + ')' +
            "primary key ("+COLUMN_INSTANCE_ID+','+ COLUMN_POLICY_ID +"));";

    private static final String[] tableCreation = {
            CREATE_TABLE_COMPONENTS,
            CREATE_TABLE_PERMISSIONS,
            CREATE_TABLE_POLICIES,
            CREATE_TABLE_INSTANCES,
            CREATE_TABLE_INSTANCES_PERMISSIONS,
            CREATE_TABLE_INSTANCES_POLICIES
    };

    private static final String[] tables = {
            TABLE_COMPONENTS,
            TABLE_PERMISSIONS,
            TABLE_POLICIES,
            TABLE_INSTANCES,
            TABLE_INSTANCES_PERMISSIONS,
            TABLE_INSTANCES_POLICIES
    };

    private static final String[] componentTypes = {
            "ACTIVITY",
            "SERVICE",
            "RECEIVER",
            "PROVIDER"
    };

    private static final String[] policyScopeTypes = {
            "DIRECT",
            "LOCAL",
            "GLOBAL"
    };

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {


        for (String create : tableCreation)
    	    database.execSQL(create);

/*
    	// MaplePay
        this.insertInstance("MainActivity", "Activity", new Policy[0], new String[0], database);
        this.insertInstance("LoginActivity", "Activity",
                new Policy[]{new Policy(Scope.GLOBAL,
                        Formula.not(Formula.or(Formula.lit(Permissions.MIC), Formula.lit(Permissions.CAM))), false)},
                new String[0], database);
        this.insertInstance("ContactPayRec.", "Receiver",
                new Policy[]{
                        new Policy(Scope.DIRECT, Formula.imply(Formula.not(Formula.lit(Permissions.APP)), Formula.lit(Permissions.UAP)), false),
                        new Policy(Scope.LOCAL, Formula.and(Formula.lit(Permissions.RCP), Formula.lit(Permissions.GAP)), false)
                },
                new String[0], database);
        this.insertInstance("BalanceActivity", "Activity",
                new Policy[]{
                        new Policy(Scope.LOCAL, Formula.imply(
                                Formula.not(Formula.lit(Permissions.ACP)),
                                Formula.not(Formula.or(Formula.or(Formula.lit(Permissions.NET), Formula.lit(Permissions.WSD)),
                                        Formula.lit(Permissions.BTT)))),
                                true)
                }, new String[0], database);
        this.insertInstance("PaymentActivity", "Activity", new Policy[0], new String[0], database);
        this.insertInstance("NormalPayRec.", "Receiver",
                new Policy[]{
                        new Policy(Scope.DIRECT, Formula.and(Formula.lit(Permissions.NPP), Formula.lit(Permissions.UAP)), false)
                },
                new String[0], database);
        this.insertInstance("MicroPayRec.", "Receiver",
                new Policy[]{
                        new Policy(Scope.DIRECT,
                                Formula.and(Formula.lit(Permissions.MPP),
                                        Formula.or(Formula.lit(Permissions.UAP), Formula.lit(Permissions.APP))),
                                false)
                },
                new String[0], database);
        this.insertInstance("ConnectionSer.", "Service", new Policy[0], new String[]{Permissions.NET, Permissions.ACP}, database);
        this.insertInstance("HistoryProvider", "Provider", new Policy[0], new String[]{Permissions.RSD, Permissions.WSD, Permissions.ACP}, database);
        
        // QRScanner
        this.insertInstance("QRScannerActivity", "Activity", new Policy[0], new String[]{Permissions.CAM, Permissions.MPP, Permissions.UAP}, database);
        
        // FancyEditor
        this.insertInstance("EditorActivity", "Activity", new Policy[0], new String[]{Permissions.RSD}, database);
        this.insertInstance("DocEditorAct.", "Activity", new Policy[0], new String[0], database);
        this.insertInstance("OpenDocRec.", "Receiver", new Policy[0], new String[]{Permissions.RSD}, database);
        this.insertInstance("CloudSer.", "Service", new Policy[0], new String[]{Permissions.NET}, database);
        
        // TamerReader
        this.insertInstance("ReaderActivity", "Activity", new Policy[0], new String[]{Permissions.RSD}, database);
        this.insertInstance("DocViewAct.", "Activity", new Policy[0], new String[0], database);
        this.insertInstance("ViewDocRec.", "Receiver", new Policy[0], new String[]{Permissions.RSD}, database);
        */
    }

    private static void logDbError(String errMsg) {
        Log.e("DBERROR", errMsg);
    }

    public void insertInstance(String componentName, /*Policy[] policies, String[] permissions,*/ String action, String data) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPNAME, componentName);
        values.put(COLUMN_ACTION, action);
        values.put(COLUMN_DATA, data);
        database.insert(TABLE_INSTANCES, null, values);
    	/*
    	String pol = formatPolicies(policies);
    	String per = formatPermissions(permissions);

    	ContentValues values = new ContentValues(); 
    	values.put(COLUMN_NAME, name); 
    	values.put(COLUMN_TYPE, type);  
    	values.put(COLUMN_POLICIES, pol);
    	values.put(COLUMN_PERMISSIONS, per);
    	
    	database.insert(TABLE_COMPONENTS, null, values);
    	*/
    }

    private void assignInstancePolicy(int instanceid, int policyid) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INSTANCE_ID, instanceid);
        values.put(COLUMN_POLICY_ID, policyid);
        database.insert(TABLE_INSTANCES_POLICIES, null, values);
    }

    private void assignInstancePermission(int instanceid, String permissionName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INSTANCE_ID, instanceid);
        values.put(COLUMN_PERMNAME, permissionName);
        database.insert(TABLE_INSTANCES_PERMISSIONS, null, values);
    }

    public void insertComponent(String componentName, String type) {
        type = type.toUpperCase();
        for (String t : componentTypes) {
            if (t.equals(type)) {
                SQLiteDatabase database = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_COMPNAME, componentName);
                values.put(COLUMN_TYPE, type);
                database.insert(TABLE_COMPONENTS, null, values);
                return;
            }
        }
        logDbError("Wrong component type: " + type);
    }

    public void insertPermission(String permissionName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERMNAME, permissionName);
        database.insert(TABLE_PERMISSIONS, null, values);
    }

    public void insertPolicy(String scope, boolean sticky, String formula) {
        scope = scope.toUpperCase();
        for (String pst : policyScopeTypes) {
            if (pst.equals(scope)) {
                SQLiteDatabase database = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_SCOPE, scope);
                values.put(COLUMN_STICKY, sticky? 1 : 0);
                values.put(COLUMN_FORMULA, formula);
                database.insert(TABLE_POLICIES, null, values);
                return;
            }
        }
        logDbError("Wrong scope type: " + scope);
    }

    private String formatPolicies(Policy[] policies) {
		String s = "";
		for(int i = 0; i < policies.length; i++) {
			s += policies[i].toString() + ":";
		}
		return s;
	}
    
    private Policy[] parsePolicies(String s) {
    	
    	if(s.compareTo("") == 0)
    		return new Policy[0];
    	
		String[] str = s.split(":");
    	Policy[] pol = new Policy[str.length];
    	
		for(int i = 0; i < str.length; i++) {
			pol[i] = Policy.parse(str[i]);
		}
		return pol;
	}

	private String formatPermissions(String[] permissions) {
		String s = "";
		for(int i = 0; i < permissions.length; i++) {
			s += permissions[i].toString() + ":";
		}
		return s;
	}
	
	private String[] parsePermissions(String s) {
		
		if(s.compareTo("") == 0)
			return new String[0];
		else 
			return s.split(":");
	}

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.w(SQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");

            for (String t : tables)
                db.execSQL("DROP TABLE IF EXISTS " + t);
            onCreate(db);
        }
    }

    public String getQuery(String type, Uri data, String action) {
        return "SELECT * FROM " + TABLE_COMPONENTS +" WHERE " + COLUMN_TYPE +" = '"+ type + "'";
    }

    public Cursor doCursorQuery(String query) {
        // TODO: should check Uri scheme
        return this.getReadableDatabase().rawQuery(query, null);
    }
    
    public Frame[] doQuery(String query) {
        Cursor c = doCursorQuery(query);
        Frame[] v = new Frame[c.getCount()];
        
        for (int i = 0; i < c.getCount(); i++) {
    		c.moveToPosition(i);
    		v[i] = new Frame();
    		v[i].component = c.getString(1);
    		
    		Policy[] pol = parsePolicies(c.getString(3));
    		Vector<Policy> pVec = new Vector<Policy>();
    		pVec.addAll(Arrays.asList(pol));
    		v[i].policies = pVec;
    		
    		String[] perm = parsePermissions(c.getString(4));
    		v[i].permissions = perm;
		}
        
        return v;
    }

    public Cursor getReceivers(String type, Uri data) {

        // TODO: should check Uri scheme
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_COMPONENTS +" WHERE " + COLUMN_TYPE +" = '"+ type + "'", null);

        return cursor;
    }

} 