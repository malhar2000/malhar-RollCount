package com.example.malhar_rollcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String key_for_passing_session_name = "keyses";

    //Database db storing the dice rolls
    private SQLiteDatabase db;
    //Storing the names of the sessions
    private SharedPreferences sharedPreferences;
    private Button add_new_session_button;
    //Stores the existing sessions
    static ArrayList<String> existing_sessions = new ArrayList<>();
    //Stores the date the existing session was created
    private ArrayList<String> dates = new ArrayList<>();
    private EditText add_new_editText;
    //to display the listView
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private int index_used_for_alert;
    private AlertDialog.Builder alertDialog;
    private Date date;
    private SimpleDateFormat simpleDateFormat;
    private ListView listViewDates;
    private ArrayAdapter<String> arrayAdapterDates;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private int index_for_edit_date;
    private EditText diceSelection;


    //onClick method for the ADD button
    public void add_new_sessions(View view) {
        String dice = diceSelection.getText().toString();
        String whereToGo = "Dice".concat(dice);
        String str = add_new_editText.getText().toString();
        //checking if the table already is created or not
        //if already created go add new entries to the game.
        boolean ifAlreadyExists = searchTheExistingSessions(str);
        if (ifAlreadyExists) {
            existing_sessions.add(str);
            add_new_dates();
            save();
            saveDates();
            db.execSQL("CREATE TABLE [" + str + "] (id TEXT PRIMARY KEY, entry INTEGER)");
            //setDefault is for initializing the content in table from 1 to 18.
            setDefault(str);
            add_new_editText.setText("");
        }
        Intent intent = null;
        try {
            //here in the package name there is an extra . in the end to make it complete..
            intent = new Intent(getApplicationContext(), Class.forName("com.example.malhar_rollcount."
                    + whereToGo));
        } catch (ClassNotFoundException e) {
            intent = new Intent(getApplicationContext(), Dice1d6.class);
        }
        //you can use the above lines of code to go to a class using the String
        //or this commented line.
        //intent.setClassName(getApplicationContext(), "com.example.rollcount."+whereToGo);
        intent.putExtra(key_for_passing_session_name, str);
        startActivity(intent);
    }

    public void add_new_dates() {
        date = Calendar.getInstance().getTime();
        dates.add(simpleDateFormat.format(date));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferencesDates;
        add_new_session_button = findViewById(R.id.add_new_button);
        add_new_editText = findViewById(R.id.sessionNameEditText);
        diceSelection = findViewById(R.id.diceSelection);

        sharedPreferences = getSharedPreferences("com.example.malhar_rollcount",
                Context.MODE_PRIVATE);
        sharedPreferencesDates = getSharedPreferences("for_dates",
                Context.MODE_PRIVATE);


        db = this.openOrCreateDatabase("myData", MODE_PRIVATE, null);

        alertDialog = new AlertDialog.Builder(this);

        deserializer();
        deserializerDates();

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, existing_sessions);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String table_requested = listView.getItemAtPosition(i).toString();
                Intent intentH = new Intent(getApplicationContext(), Histogram.class);
                intentH.putExtra("histo", table_requested);
                startActivity(intentH);
                return true;
            }
        });

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        listViewDates = (ListView) findViewById(R.id.listViewDates);

        arrayAdapterDates = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, dates);

        listViewDates.setAdapter(arrayAdapterDates);

        listViewDates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("zDAe", i + "");
                index_for_edit_date = i;
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, onDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String m, d;
                month += 1;
                if (month < 10) {
                    m = "0" + month;
                } else
                    m = "" + month;
                if (day < 10) {
                    d = "0" + day;
                } else
                    d = "" + day;
                String edited_date = year + "-" + m + "-" + d;
                dates.set(index_for_edit_date, edited_date);
                arrayAdapterDates.notifyDataSetChanged();
                saveDates();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.option_for_browsing:
                browsing();
                return true;
            case R.id.option_for_adding:
                adding();
                return true;
            case R.id.option_for_editing:
                Intent editing = new Intent(getApplicationContext(), Editing.class);
                startActivity(editing);
                return true;
            case R.id.option_for_deleting:
                deleting();
                return true;
            case R.id.option_for_saving:
                saving();
                return true;
            default:
                return false;
        }
    }

    public void browsing() {
        add_new_session_button.setVisibility(View.INVISIBLE);
        add_new_editText.setVisibility(View.INVISIBLE);
        diceSelection.setVisibility(View.INVISIBLE);
    }

    public void adding() {
        add_new_session_button.setVisibility(View.VISIBLE);
        add_new_editText.setVisibility(View.VISIBLE);
        diceSelection.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name_t = arrayAdapter.getItem(i);
                Intent j = new Intent(getApplicationContext(), Dice1d6.class);
                j.putExtra("keyses", name_t);
                startActivity(j);
            }
        });

    }

    public void saving() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name_t = arrayAdapter.getItem(i);

            }
        });
    }

    public void deleting() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //need to delete the item from the arrayAdapter itself instead of
                //also can do arrayAdapter.remove(arrayAdapter.getItem(i));
                //passing the index to be deleted
                index_used_for_alert = i;

                alertDialog.setIcon(android.R.drawable.checkbox_on_background);
                alertDialog.setTitle("Would you like to delete session \"" + existing_sessions
                        .get(index_used_for_alert) + "\" ?");
                alertDialog.setMessage("Session \"" + existing_sessions.get(index_used_for_alert) +
                        "\" will be deleted permanently.");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String table_to_remove = arrayAdapter.getItem(index_used_for_alert);
                        existing_sessions.remove(index_used_for_alert);
                        dates.remove(index_used_for_alert);
                        arrayAdapterDates.notifyDataSetChanged();
                        arrayAdapter.notifyDataSetChanged();
                        db.execSQL("DROP TABLE IF EXISTS [" + table_to_remove + "]");
                        save();
                        saveDates();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void save() {
        try {
            sharedPreferences.edit().putString("existing_sessions",
                    ObjectSerializer.serialize(existing_sessions)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> deserializer() {
        try {
            existing_sessions = (ArrayList<String>) ObjectSerializer.deserialize(
                    sharedPreferences.getString("existing_sessions",
                            ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existing_sessions;
    }

    public void saveDates() {
        try {
            sharedPreferences.edit().putString("dates",
                    ObjectSerializer.serialize(dates)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> deserializerDates() {
        try {
            dates = (ArrayList<String>) ObjectSerializer.deserialize(
                    sharedPreferences.getString("dates",
                            ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dates;
    }

    public void setDefault(String table_name) {
        //onCreate(db);
        for (int j = 1; j <= 18; j++) {
            db.execSQL("INSERT INTO [" + table_name + "] (id, entry) VALUES (" + j + ", 0)");
        }
    }

    public boolean searchTheExistingSessions(String str) {
        if (existing_sessions == null) {
            return true;
        }
        for (int i = 0; i < existing_sessions.size(); i++) {
            if (existing_sessions.get(i).equals(str)) {
                return false;
            }
        }
        return true;
    }

}
