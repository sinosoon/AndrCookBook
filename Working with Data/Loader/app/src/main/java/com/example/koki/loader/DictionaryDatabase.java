package com.example.koki.loader;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DictionaryDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dictionary";
    private static final String TABLE_DICTIONARY = "dictionary";
    private static final String FIELD_WORD = "word";
    private static final String FIELD_DEFINITION = "definition";
    private static final int DATABASE_VERSION = 1;

    DictionaryDatabase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_DICTIONARY +
                "(_id integer PRIMARY KEY," +
                FIELD_WORD + " TEXT, " +
                FIELD_DEFINITION + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Dandle database as needed
    }

    public int deleteRecord(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_DICTIONARY, "_id = ?", new String[]{String.valueOf(id)});
    }

    public void saveRecord(String word, String definition) {
        long id = findWordID(word);
        if (id>0) {
            updateRecord(id,word,definition);
        } else {
            addRecord(word,definition);
        }
    }

    public long addRecord(String word, String definition) {
        SQLiteDatabase sqLiteDatabase =getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_WORD,word);
        values.put(FIELD_DEFINITION,definition);
        return sqLiteDatabase.insert(TABLE_DICTIONARY, null,values);
    }

    public int updateRecord(long id, String word, String definition) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id",id);
        values.put(FIELD_WORD,word);
        values.put(FIELD_DEFINITION,definition);
        return sqLiteDatabase.update(TABLE_DICTIONARY,values,"_id = ?",
                new String[]{String.valueOf(id)});
    }
    public long findWordID(String word) {
        long returnVal = -1;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT _id FROM " + TABLE_DICTIONARY +
                        " WHERE " + FIELD_WORD + " = ?", new String[]{word});
        Log.i("findWordID","getCount()="+cursor.getCount());
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal= cursor.getInt(0);
        }
        return returnVal;
    }
    public String getDefinition(long id) {
        String returnVal = "";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT definition FROM " + TABLE_DICTIONARY +
                        " WHERE _id = ?", new String[]{String.valueOf(id)});

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getString(0);
        }
        return returnVal;
    }
    public Cursor getWordList() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT _id, " + FIELD_WORD +
                " FROM " + TABLE_DICTIONARY + " ORDER BY " + FIELD_WORD +
                " ASC";
        return sqLiteDatabase.rawQuery(query,null);
    }
}
