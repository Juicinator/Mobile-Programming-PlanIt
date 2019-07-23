package com.example.planit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = (ListView)findViewById(R.id.listView);

        sharedPreferences = this.getSharedPreferences("com.example.planit", Context.MODE_PRIVATE);

        set = sharedPreferences.getStringSet("notes", null);

        notes.clear();

        if(set != null) {
            notes.addAll(set);
        }
        else{
            notes.add("Example Note");
            set = new HashSet<String>();
            set.addAll(notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes", set).apply();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), EditNote.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });

        //DELETE NOTE SECTION
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)  //The long button hold will bring this up on any created memo
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this memo?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notes.remove(position);

                                sharedPreferences = getApplicationContext().getSharedPreferences("com.example.planit", Context.MODE_PRIVATE);

                                if(set == null){
                                    set = new HashSet<String>();
                                }
                                else{
                                    set.clear();
                                }

                                set.addAll(notes);

                                sharedPreferences.edit().remove("notes").apply();
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            return true;}
        });


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        //FLOATING ACTION BUTTON SECTION
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notes.add("");

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.planit", Context.MODE_PRIVATE);

                if(set == null){
                    set = new HashSet<String>();
                }
                else{
                    set.clear();
                }

                set.addAll(notes);

                sharedPreferences.edit().remove("notes").apply();
                sharedPreferences.edit().putStringSet("notes", set).apply();
                arrayAdapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), EditNote.class);

                intent.putExtra("noteId", notes.size()-1);

                startActivity(intent);

            }
        });


        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WeatherActivity.class));

            }
        });

        FloatingActionButton fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
