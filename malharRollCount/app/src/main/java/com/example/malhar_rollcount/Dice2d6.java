package com.example.malhar_rollcount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.Stack;

public class Dice2d6 extends AppCompatActivity {

    private SQLiteDatabase db2;
    private DataBase dataBase;
    private String table_name;
    private Stack<String> storing_each_turnOfDice = new Stack<>();


    public void forAll2(View view){
        String id = view.getTag().toString();
        storing_each_turnOfDice.push(id);
        dataBase.addData(table_name, id);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice2d6);
        //dice1d6 = new Dice1d6();
        db2 = this.openOrCreateDatabase("myData", MODE_PRIVATE, null);
        dataBase = new DataBase(db2);
        table_name = getIntent().getStringExtra("keyses");
    }

    public void move3d6(View view){
        Intent intent3 = new Intent(getApplicationContext(), Dice3d6.class);
        intent3.putExtra(MainActivity.key_for_passing_session_name, table_name);
        startActivity(intent3);
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