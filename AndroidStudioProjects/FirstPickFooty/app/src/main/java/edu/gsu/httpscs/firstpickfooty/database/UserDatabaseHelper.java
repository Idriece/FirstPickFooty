package edu.gsu.httpscs.firstpickfooty.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class UserDatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = UserDatabaseHelper.class.getSimpleName();
    public static final String DB_NAME = "firstpickfooty.db";
    public static final int DB_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASS = "password";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_POSITION = "position";

    /*
    create table users(
        id integer primary key autoincrement,
        email text,
        password text);
     */
    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASS + " TEXT,"
            + COLUMN_POSITION + " TEXT);";

    public UserDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String username, String password, String position) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASS, password);
        values.put(COLUMN_POSITION, position);

        long id = db.insert(USER_TABLE, null, values);
        db.close();

        Log.d(TAG, "user inserted" + id);
    }

    public boolean getUser(String username, String pass){
        //HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "select * from  " + USER_TABLE + " where " +
                COLUMN_USERNAME + " = " + "'"+username+"'" + " and " + COLUMN_PASS + " = " + "'"+pass+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
//        cursor.close();
//        db.close();

        return false;
    }

    public Cursor getInformation() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String[] columns = {COLUMN_USERNAME};
        cursor = db.query(USER_TABLE, columns, null, null, null, null, null, null);
        return cursor;
    }

}