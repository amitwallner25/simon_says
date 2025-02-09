package com.example.saimonsays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class GameDatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "game_db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_NAME = "player_scores";
    private static final String COLUMN_CURRENT_LOGGED_IN_USER = "current_logged_in_user";
    private static final String COLUMN_ID = "unique_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_HIGHEST_SCORE = "highest_score";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_HS_DATE = "hsdate";
    private static final String COLUMN_SCORE = "score";

    // SQL query to create the table
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " TEXT PRIMARY KEY, "
            + COLUMN_CURRENT_LOGGED_IN_USER + " TEXT, "
            + COLUMN_USERNAME + " TEXT, "
            + COLUMN_HIGHEST_SCORE + " INTEGER, "
            + COLUMN_PASSWORD + " TEXT, "
            + COLUMN_HS_DATE + " TEXT,"
            + COLUMN_SCORE + " INTEGER)";

    // Constructor
    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table when the database is created
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If the database version is upgraded, drop the old table and create a new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    // Method to add a new player record to the database
    public void addPlayer(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String uniqueId;
        int highestScore = 0;
        String hsDate = "";
        int score = 0;
        do {
            uniqueId = generateUniqueId();


        } while (isUniqueIdExists(db, uniqueId) || isUsernameExists(db, username));

        values.put(COLUMN_ID, uniqueId);
        values.put(COLUMN_CURRENT_LOGGED_IN_USER,"");
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_HIGHEST_SCORE, highestScore);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_HS_DATE, hsDate);
        values.put(COLUMN_SCORE, score);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    private String generateUniqueId() {
        Random random = new Random();
        int id = 10000 + random.nextInt(90000); // Generates 5-digit number
        return String.valueOf(id);
    }


    private boolean isUniqueIdExists(SQLiteDatabase db, String uniqueId) {
        String[] selectionArgs = {uniqueId};
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME, COLUMN_ID + " = ?", selectionArgs) > 0;
    }

    private boolean isUsernameExists(SQLiteDatabase db, String uniqueId) {
        String[] selectionArgs = {uniqueId};
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME, COLUMN_USERNAME + " = ?", selectionArgs) > 0;
    }


    // Method to get a player's information by unique ID
    public Cursor getPlayerById(String uniqueId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, COLUMN_ID + "=?", new String[]{uniqueId}, null, null, null);
    }

    // Method to update a player's highest score
    public void updatePlayerHighScore(String uniqueId, int newScore, String newDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("GameDB", "Attempting to update high score for user ID: " + uniqueId);

        // Check the current highest score
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_HIGHEST_SCORE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=?", new String[]{uniqueId});

        if (cursor.moveToFirst()) {
            int currentHighScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HIGHEST_SCORE));
            Log.d("GameDB", "Current High Score: " + currentHighScore + ", New Score: " + newScore);
            cursor.close();

            // Update only if the new score is higher
            if (newScore > currentHighScore) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_HIGHEST_SCORE, newScore);
                values.put(COLUMN_HS_DATE, newDate);

                int rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{uniqueId});
                Log.d("GameDB", "High score updated successfully! Rows affected: " + rowsAffected);
            } else {
                Log.d("GameDB", "New score is not higher. No update performed.");
            }
        } else {
            Log.d("GameDB", "User ID not found: " + uniqueId);
            cursor.close();
        }

        db.close();
    }



    // Method to delete a player's record
    public void deletePlayer(String uniqueId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{uniqueId});
        db.close();
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE USERNAME=? AND PASSWORD=?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public int getScore(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SCORE FROM " + TABLE_NAME + " WHERE USERNAME=?", new String[]{username});
        if (cursor.moveToFirst()) {
            int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
            cursor.close();
            return score;
        }
        cursor.close();
        return 0; // Default score if user doesn't exist
    }

    public int getHighScore(String username) {
        Log.d("GameDB", "GetHighScore(" + username + ")");
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("GameDB", "GetHighScore: got readable db");
        Cursor cursor = db.rawQuery("SELECT HIGHEST_SCORE FROM " + TABLE_NAME + " WHERE USERNAME=?", new String[]{username});
        if (cursor.moveToFirst()) {
            int highScore = cursor.getInt(cursor.getColumnIndexOrThrow("highest_score"));
            cursor.close();
            return highScore;
        }
        cursor.close();
        return 0; // Default score if user doesn't exist
    }

    public boolean doesUserExist(GameDatabaseHelper db, String username) {
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String[] selectionArgs = {username};
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ? LIMIT 1";
        Cursor cursor = readableDatabase.rawQuery(query, selectionArgs);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public String getCurrentLoggedIn() {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CURRENT_LOGGED_IN_USER + " FROM " + TABLE_NAME;
        Cursor cursor = readableDatabase.rawQuery(query, null);

        String currentUser = null;
        if (cursor.moveToFirst()) {
            currentUser = cursor.getString(0); // Assuming the column is of type TEXT
        }
        cursor.close();
        readableDatabase.close();
        return currentUser;
    }

    public void setCurrentLoggedInUser(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CURRENT_LOGGED_IN_USER, name);
        db.update(TABLE_NAME, values, null, null);
    }



    public void updateScore(String username, int score) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SCORE, score);
            db.update(TABLE_NAME, contentValues, "USERNAME = ?", new String[]{username});
        }

    }

    public String getIdByUserName(String uName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{uName});
        String id = null;
        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    public ArrayList<CardModel> getAllPlayers() {
        ArrayList<CardModel> playerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username, highest_score, hsdate FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow("highest_score"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("hsdate"));

                playerList.add(new CardModel(name, score, date));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return playerList;
    }


}


