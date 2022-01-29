package com.example.malhar_rollcount;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;


public class Storeage extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    public void create(){
        sharedPreferences = this.getSharedPreferences("com.example.malhar_rollcount",
                Context.MODE_PRIVATE);
    }

    public void save() {
        try {
            sharedPreferences.edit().putString("existing_sessions",
                    ObjectSerializer.serialize(MainActivity.existing_sessions)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deserializer() {
        try {
            MainActivity.existing_sessions = (ArrayList<String>) ObjectSerializer.deserialize(
                    sharedPreferences.getString("existing_sessions",
                            ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}