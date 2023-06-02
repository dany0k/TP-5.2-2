package ru.vsu.cs.tp.richfamily.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE IF NOT EXISTS" + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                FLAG + " INTEGER" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun setFlag(flag : Int){
        val values = ContentValues()
        values.put(FLAG, 1)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun resetFlag(flag : Int){
        val values = ContentValues()
        values.put(FLAG, 0)
        val db = this.writableDatabase
        db.update(TABLE_NAME, values, "id=?", arrayOf("1"))
        db.close()
    }

    fun getFlag(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT $FLAG FROM $TABLE_NAME", null)

    }

    companion object{
        private const val DATABASE_NAME = "SPLASH"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "splash_table"
        const val ID_COL = "id"
        const val FLAG = "flag"
    }
}