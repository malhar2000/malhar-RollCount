package com.example.malhar_rollcount;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBase {
    private SQLiteDatabase db;

    public DataBase(SQLiteDatabase db) {
        this.db = db;
    }

    //Updating the table after adding 1.
    public void updateValue(String table_name, String id, int new_entry){
        String query = "UPDATE ["+table_name+ "] SET entry = " +new_entry+ " WHERE id = " +id;
        db.execSQL(query);
    }

    //Add the 1 and send data to update and also get the entry we want to change
    public boolean addData(String table_name, String id){
        Cursor data = getEntry(table_name, id);
        int old_entry = -1;
        int entry = data.getColumnIndex("entry");
        while(data.moveToNext()){
            old_entry = data.getInt(entry);
        }
        if(old_entry > -1) {
            int new_entry = old_entry + 1;
            updateValue(table_name, id, new_entry);
            return true;
        }
        else{
            return false;
        }
    }

    //for getting entry for the addData
    public Cursor getEntry(String table_name, String id){
        String query = "SELECT entry FROM ["+table_name+"] WHERE id = " +id+"";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean subData(String table_name, String id){
        Cursor data = getEntry(table_name, id);
        int old_entry = -1;
        int entry = data.getColumnIndex("entry");
        while(data.moveToNext()){
            old_entry = data.getInt(entry);
        }
        if(old_entry > -1) {
            int new_entry = old_entry - 1;
            updateValue(table_name, id, new_entry);
            return true;
        }
        else{
            return false;
        }
    }

}

