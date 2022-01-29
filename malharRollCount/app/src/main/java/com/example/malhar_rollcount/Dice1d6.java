package com.example.malhar_rollcount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import android.os.Bundle;

public class Dice1d6 extends AppCompatActivity {
    private SQLiteDatabase db1;
    private DataBase dataBase;
    private String table_name;
    private Stack<String> storing_each_turnOfDice = new Stack<>();


    public void forAll(View view){
        String id = view.getTag().toString();
        storing_each_turnOfDice.push(id);
        dataBase.addData(table_name, id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice1d6);
        db1 = this.openOrCreateDatabase("myData", MODE_PRIVATE, null);
        dataBase = new DataBase(db1);
        table_name = getIntent().getStringExtra("keyses");
    }

    public void move2d6(View view){
        Intent intent2 = new Intent(getApplicationContext(), Dice2d6.class);
        intent2.putExtra(MainActivity.key_for_passing_session_name, table_name);
        startActivity(intent2);
    }
    public void move3d6(View view){
        Intent intent3 = new Intent(getApplicationContext(), Dice3d6.class);
        intent3.putExtra(MainActivity.key_for_passing_session_name, table_name);
        startActivity(intent3);
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
