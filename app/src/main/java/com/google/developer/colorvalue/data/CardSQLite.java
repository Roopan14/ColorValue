package com.google.developer.colorvalue.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.developer.colorvalue.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Helper class to manage database
 */
public class CardSQLite extends SQLiteOpenHelper {

    private static final String TAG = CardSQLite.class.getName();
    private static final String DB_NAME = "colorvalue.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "cards";
    public static final String COLUMN_NAME = "answer";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_HEXNAME = "question";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_HEXNAME + " TEXT,"
                    + COLUMN_NAME + " TEXT"
                    + ")";

    private Resources mResources;

    public CardSQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Create and fill the database
        db.execSQL(CREATE_TABLE);
        addDemoCards(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Handle database version upgrades
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public static String getColumnString(Cursor cursor, String name) {
        if (cursor != null) {
            return cursor.getString(cursor.getColumnIndex(name));
        }
        return null;
    }

    public static int getColumnInt(Cursor cursor, String name) {
        if (cursor != null) {
            return cursor.getInt(cursor.getColumnIndex(name));
        }
        return -1;
    }

    /**
     * save demo cards into database
     */
    private void addDemoCards(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            try {
                readCardsFromResources(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Unable to pre-fill database", e);
        }
    }

    /**
     * load demo color cards from {@link raw/colorcards.json}
     */
    private void readCardsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.colorcards);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        JSONObject root = new JSONObject(builder.toString());
        // TODO Parse JSON data and insert into the provided database instance

        if (root != null) {

            try {
                JSONArray jsonArray = root.getJSONArray("cards");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject c = jsonArray.getJSONObject(i);

                    String name = c.getString("name");
                    String hexName = c.getString("hex");

                    ContentValues cv = new ContentValues();
                    cv.put(COLUMN_HEXNAME, hexName);
                    cv.put(COLUMN_NAME, name);

                    db.insert(TABLE_NAME, null, cv);

                }
            }
            catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                db.close();
            }

        }
    }

}
