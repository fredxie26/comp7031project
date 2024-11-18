package com.example.comp7031project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PHOTO_PATH = "photoPath";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_IS_FOCUSED = "is_focused";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_PHOTO_PATH + " TEXT,"
                + COLUMN_STATUS + " TEXT DEFAULT 'complete',"
                + COLUMN_IS_FOCUSED + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_USERS_TABLE);
        initializeData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void initializeData(SQLiteDatabase db) {
        insertUser(db, "John", "Doe", "123 Main St", null, "complete");
        insertUser(db, "Jane", "Smith", "456 Elm St", null, "complete");
        insertUser(db, "Alice", "Johnson", "789 Maple St", null, "complete");
    }

    private void insertUser(SQLiteDatabase db, String firstName, String lastName, String address, String photoPath, String status) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_PHOTO_PATH, photoPath);
        values.put(COLUMN_STATUS, status);
        db.insert(TABLE_USERS, null, values);
    }

    // Method to add a new user (for CreateProfileActivity)
    public void addUser(String firstName, String lastName, String address, String photoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_PHOTO_PATH, photoPath);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FOCUSED)) == 1
                );
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return users;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("users", "id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsDeleted > 0;
    }

    public boolean updateFocusStatus(int userId, boolean isFocused) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_focused", isFocused ? 1 : 0);
        // Log values to verify they are correct
        Log.d("DatabaseHelper", "Updating user focus status: userId = " + userId + ", is_focused = " + (isFocused ? 1 : 0));

        int rowsUpdated = db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});

        // Log the result to confirm if the update was successful
        if (rowsUpdated > 0) {
            Log.d("DatabaseHelper", "Update successful. Rows affected: " + rowsUpdated);
        } else {
            Log.e("DatabaseHelper", "Update failed. No rows affected.");
        }

//        int rowsUpdated = db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsUpdated > 0;
    }

    public ArrayList<User> getFocusedUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE is_focused = 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FOCUSED)) == 1
                );
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        cursor.close();
        return users;
    }

    public void updateUserStatus(int userId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);

        // Update the user status where the ID matches
        db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
}
