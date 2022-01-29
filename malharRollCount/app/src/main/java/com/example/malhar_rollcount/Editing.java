package com.example.malhar_rollcount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;

public class Editing extends AppCompatActivity {

    private SharedPreferences    editPref;
    private ArrayList<String> editArrayList = new ArrayList<>();
    private    Button editButton;
    private EditText editText;
    private   String new_name = "new";
    private int index;
    private ArrayAdapter<String> editArrayAdapter;
    private SQLiteDatabase dbE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        dbE = this.openOrCreateDatabase("myData", MODE_PRIVATE, null);

        editButton = findViewById(R.id.editButton);
        editText = findViewById(R.id.editEditText);
        editPref =  this.getSharedPreferences("com.example.rollcount",
                Context.MODE_PRIVATE);

        try {
            editArrayList = (ArrayList<String>) ObjectSerializer.deserialize(
                    editPref.getString("existing_sessions",
                            ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        ListView editListView = findViewById(R.id.editListView);
        editArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, editArrayList);
        editListView.setAdapter(editArrayAdapter);


        editListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editButton.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);

                //we need the i(index of session to be changed) to change session name
                index = i;
            }
        });
    }

    //need to change the SharedPreferences
    public void editSave() {
        try {
            editPref.edit().putString("existing_sessions",
                    ObjectSerializer.serialize(editArrayList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*this is onClick method for editButton. We get the new name of the session we update the
    sessions name, notify adapter and change the SharedPreferences.*/
    public void needString(View view){
        new_name = editText.getText().toString();
        dbE.execSQL("ALTER TABLE "+editArrayList.get(index)+ " RENAME TO ["+new_name+"]");
        editArrayList.set(index, new_name);
        editArrayAdapter.notifyDataSetChanged();
        editSave();
        editText.setText("");
        editButton.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.INVISIBLE);
    }

    //for the home button
    public void goBack(View view){
        Intent in_edit = new Intent(getApplicationContext(), MainActivity.class);
        in_edit.putExtra("keyses",new_name);
        startActivity(in_edit);
    }
}