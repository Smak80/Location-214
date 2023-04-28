package ru.smak.location_214

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.util.Log

class DbHelper(context: Context) : SQLiteOpenHelper(
    context, "ru.smak.location_db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.run {
            try {
                beginTransaction()
                execSQL(
                    "create table locations(" +
                            "time text not null primary key, " +
                            "latitude real not null," +
                            "longitude real not null" +
                            ")"
                )
                setTransactionSuccessful()
                endTransaction()
            } catch (e: Throwable){
                Log.e("DB ERROR", e.message?:"")
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }

    fun addLocation(loc: Location){
        writableDatabase.run{
            beginTransaction()
            val values = ContentValues()
            values.put("time", loc.time)
            values.put("latitude", loc.latitude)
            values.put("longitude", loc.longitude)
            insert("locations", null, values)
            setTransactionSuccessful()
            endTransaction()
        }
    }

    fun getLocations(): List<String>{
        val lst = mutableListOf<String>()
        readableDatabase.run{
            beginTransaction()
            query("locations", arrayOf("time", "latitude", "longitude"), null, null, null, null, null).also{
                while (it.moveToNext()){
                    lst.add("${it.getString(0)} ${it.getDouble(1)} ${it.getDouble(2)}")
                }
            }.close()
            setTransactionSuccessful()
            endTransaction()
        }
        return lst
    }
}
