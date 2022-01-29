package com.example.malhar_rollcount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import android.os.Bundle;

public class Dice3d6 extends AppCompatActivity {

    private String table_name;
    private DataBase dataBase;
    private SQLiteDatabase db3;
    private Button button;
    private Stack<String> storing_each_turnOfDice = new Stack<>();


    public void forAll3(View view){
        String id = view.getTag().toString();
        storing_each_turnOfDice.push(id);
        dataBase.addData(table_name, id);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice3d6);
        button = findViewById(R.id.button);
        db3 = this.openOrCreateDatabase("myData", MODE_PRIVATE, null);
        dataBase = new DataBase(db3);
        table_name = getIntent().getStringExtra("keyses");
    }

    public void move2d6(View view){
        Intent intent2 = new Intent(getApplicationContext(), Dice2d6.class);
        intent2.putExtra(MainActivity.key_for_passing_session_name, table_name);
        startActivity(intent2);
    }
    public void move1d6(View view){
        Intent intent1 = new Intent(getApplicationContext(), Dice1d6.class);
        intent1.putExtra(MainActivity.key_for_passing_session_name, table_name);
        startActivity(intent1);
    }

    public void home(View view){
        Intent intent0 = new Intent(getApplicationContext(), MainActivity.class);
        intent0.putExtra(MainActivity.key_for_passing_session_name, table_name);
        startActivity(intent0);
    }

    public void undo(View view){
        if(!storing_each_turnOfDice.empty())
            dataBase.subData(table_name, storing_each_turnOfDice.pop());
        else
            return;
    }

}