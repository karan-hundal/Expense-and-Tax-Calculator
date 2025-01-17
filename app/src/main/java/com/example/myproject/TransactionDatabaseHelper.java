package com.example.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TransactionDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "transactions.db";
    private static final String TABLE_NAME = "transactions";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "TYPE"; // Expense or Earning
    private static final String COL_4 = "CATEGORY";
    private static final String COL_5 = "AMOUNT";

    public TransactionDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERNAME TEXT, TYPE TEXT, CATEGORY TEXT, AMOUNT REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTransaction(String username, String type, String category, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, type);
        contentValues.put(COL_4, category);
        contentValues.put(COL_5, amount);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public ArrayList<String> getTransactionsByUser(String username) {
        ArrayList<String> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE USERNAME=?", new String[]{username});
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow(COL_3));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_4));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_5));
            transactions.add(type + ": " + category + " - $" + String.format("%.2f", amount));
        }
        cursor.close();
        return transactions;
    }
}


