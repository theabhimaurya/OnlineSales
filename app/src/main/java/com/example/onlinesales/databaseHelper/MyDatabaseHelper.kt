package com.example.onlinesales.databaseHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.onlinesales.model.ListData

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "string_lists.db"
        private const val TABLE_NAME = "string_lists_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_LIST1 = "list1"
        private const val COLUMN_LIST2 = "list2"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_LIST1 TEXT, $COLUMN_LIST2 TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade if needed
    }

    fun insertStringLists(stringLists: ListData): Long {
        val values = ContentValues()
        values.put(COLUMN_LIST1, stringLists.list1.joinToString(","))
        values.put(COLUMN_LIST2, stringLists.list2.joinToString(","))

        val db = this.writableDatabase
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun getStringLists(): List<ListData> {
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        val stringLists = mutableListOf<ListData>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val list1 = cursor.getString(cursor.getColumnIndex(COLUMN_LIST1)).split(",")
            val list2 = cursor.getString(cursor.getColumnIndex(COLUMN_LIST2)).split(",")
            stringLists.add(ListData(id, list1, list2))
        }

        cursor.close()
        db.close()
        return stringLists
    }
}