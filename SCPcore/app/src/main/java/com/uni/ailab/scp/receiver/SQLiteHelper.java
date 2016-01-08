package com.uni.ailab.scp.receiver;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import com.uni.ailab.scp.cnf.Formula;
import com.uni.ailab.scp.policy.Permissions;
import com.uni.ailab.scp.policy.Policy;
import com.uni.ailab.scp.policy.Scope;
import com.uni.ailab.scp.runtime.Frame;
import com.uni.ailab.scp.secureManifest.ComponentType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_COMPONENTS = "components";
    public static final String TABLE_PERMISSIONS = "permissions";
    public static final String TABLE_INSTANCES = "instances";
    public static final String TABLE_POLICIES = "policies";
    public static final String TABLE_COMPONENTS_PERMISSIONS = "componentshavepermissions";
    public static final String TABLE_COMPONENTS_POLICIES = "componentshavepolicies";

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

    private static final String CREATE_TABLE_COMPONENTS_PERMISSIONS = "create table "
            + TABLE_COMPONENTS_PERMISSIONS + "(" +
            COLUMN_COMPNAME + " text not null, " +
            COLUMN_PERMNAME + " text not null," +
            "foreign key ("+COLUMN_COMPNAME+") references " + TABLE_COMPONENTS + '(' + COLUMN_COMPNAME + ')' +
            "foreign key ("+COLUMN_PERMNAME+") references " + TABLE_PERMISSIONS + '(' + COLUMN_PERMNAME + ')' +
            "primary key ("+COLUMN_COMPNAME+','+ COLUMN_PERMNAME +"));";

    private static final String CREATE_TABLE_COMPONENTS_POLICIES= "create table "
            + TABLE_COMPONENTS_POLICIES + "(" +
            COLUMN_COMPNAME + " text not null, " +
            COLUMN_POLICY_ID + " integer not null," +
            "foreign key ("+COLUMN_COMPNAME+") references " + TABLE_COMPONENTS + '(' + COLUMN_COMPNAME + ')' +
            "foreign key ("+COLUMN_POLICY_ID+") references " + TABLE_POLICIES + '(' + COLUMN_POLICY_ID + ')' +
            "primary key ("+COLUMN_COMPNAME+','+ COLUMN_POLICY_ID +"));";

    private static final String[] tableCreation = {
            CREATE_TABLE_COMPONENTS,
            CREATE_TABLE_PERMISSIONS,
            CREATE_TABLE_POLICIES,
            CREATE_TABLE_INSTANCES,
            CREATE_TABLE_COMPONENTS_PERMISSIONS,
            CREATE_TABLE_COMPONENTS_POLICIES
    };

    private static final String[] tables = {
            TABLE_COMPONENTS,
            TABLE_PERMISSIONS,
            TABLE_POLICIES,
            TABLE_INSTANCES,
            TABLE_COMPONENTS_PERMISSIONS,
            CREATE_TABLE_COMPONENTS_POLICIES
    };//


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        for (String create : tableCreation)
    	    database.execSQL(create);
    }

    private static void logDbError(String errMsg) {
        Log.e("DBERROR", errMsg);
    }

    public void fullWithTest() {
        Policy[] testPolicies = {
                new Policy(Scope.GLOBAL, Formula.not(Formula.or(Formula.lit(Permissions.MIC), Formula.lit(Permissions.CAM))), false),
                new Policy(Scope.DIRECT, Formula.imply(Formula.not(Formula.lit(Permissions.APP)), Formula.lit(Permissions.UAP)), false),
                new Policy(Scope.LOCAL, Formula.imply(
                        Formula.not(Formula.lit(Permissions.ACP)),
                        Formula.not(Formula.or(Formula.or(Formula.lit(Permissions.NET), Formula.lit(Permissions.WSD)),
                                Formula.lit(Permissions.BTT)))), true),
                new Policy(Scope.DIRECT, Formula.and(Formula.lit(Permissions.MPP), Formula.or(Formula.lit(Permissions.UAP), Formula.lit(Permissions.APP))), false),
                new Policy(Scope.DIRECT, Formula.and(Formula.lit(Permissions.NPP), Formula.lit(Permissions.UAP)), false),

        };

        String[] testPermissions = {
                "android.permission.ACCOUNT_MANAGER",
                "android.permission.SET_ALARM",
                "android.permission.CHANGE_NETWORK_STATE",
                "android.permission.WRITE_CALENDAR",
                "android.permission.WRITE_SMS"
        };

        insertInstance("MainActivity", ComponentType.getEnum("ACTIVITY"), "action1", "data1", Arrays.asList(testPolicies), Arrays.asList(testPermissions));
    }

    public long insertInstance(String componentName, ComponentType componentType, String action, String data, Collection<Policy> policies, Collection<String> permissions) {
        insertComponent(componentName, componentType);
        long instance = insertInstance(componentName, action, data);
        for (Policy p : policies) {
            assignComponentPolicy(componentName, insertPolicy(p));
        }
        for (String s : permissions) {
            insertPermission(s);
            assignComponentPermission(componentName, s);
        }
        return instance;
    }


    public long insertInstance(String componentName, String action, String data) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPNAME, componentName);
        values.put(COLUMN_ACTION, action);
        values.put(COLUMN_DATA, data);
        return database.insert(TABLE_INSTANCES, null, values);
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

    public long assignComponentPolicy(String componentName, long policyid) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPNAME, componentName);
        values.put(COLUMN_POLICY_ID, policyid);
        return database.insert(TABLE_COMPONENTS_POLICIES, null, values);
    }

    public long assignComponentPermission(String componentName, String permissionName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPNAME, componentName);
        values.put(COLUMN_PERMNAME, permissionName);
        return database.insert(TABLE_COMPONENTS_PERMISSIONS, null, values);
    }

    public long insertComponent(String componentName, ComponentType componentType) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPNAME, componentName);
        values.put(COLUMN_TYPE, componentType.toString());
        return database.insert(TABLE_COMPONENTS, null, values);
    }

    public long insertPermission(String permissionName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERMNAME, permissionName);
        return database.insert(TABLE_PERMISSIONS, null, values);
    }

    public long insertPolicy(Policy policy) {
        return insertPolicy(policy.getScope().toString(), policy.isSticky(), policy.getFormula().toString());
    }

    public long insertPolicy(String scope, boolean sticky, String formula) {
        long ret = -1;
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_SCOPE, Scope.getEnum(scope).toString());
            values.put(COLUMN_STICKY, sticky? 1 : 0);
            values.put(COLUMN_FORMULA, formula);
            ret = database.insert(TABLE_POLICIES, null, values);
        } catch (IllegalArgumentException e) {
            logDbError("Wrong scope type: " + scope);
        }
        return ret;
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