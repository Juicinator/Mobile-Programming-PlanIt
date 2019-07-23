package com.example.planit;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import static com.example.planit.MainActivity.notes;
import static com.example.planit.MainActivity.set;

public class EditNote extends AppCompatActivity implements TextWatcher, TimePickerDialog.OnTimeSetListener{

    int noteId;
    SharedPreferences sharedPreferences;
    private TextView MyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyView = findViewById(R.id.timer);

        Button notifbutton = findViewById(R.id.notif_button);

        notifbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }


        });

        Button cancel = findViewById(R.id.cancelbutton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotif();
            }

        });


        EditText editText = (EditText) findViewById(R.id.editText);

        //************************************************************************************************
        //This section prints "Date Modified" along with the Date
        //************************************************************************************************

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateFull = new Date();
        Calendar calendar = Calendar.getInstance();

        String date = dateFormat.format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.dateText);

        textViewDate.setText("Date Modified: " + date);

        //**************************************************************************************************
        //*************************************************************************************************

        Intent i = getIntent();

        noteId = i.getIntExtra("noteId", -1);

        if (noteId != -1) {
            String fillerText = notes.get(noteId);
            editText.setText(fillerText);
        }

        editText.addTextChangedListener(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        notes.set(noteId, String.valueOf(s));
        //notes.set(noteId, String.valueOf(s));
        MainActivity.arrayAdapter.notifyDataSetChanged();

        sharedPreferences = this.getSharedPreferences("com.example.planit", Context.MODE_PRIVATE);

        if (set == null) {
            set = new HashSet<String>();
        } else {
            set.clear();
        }
        set.clear();

        set.addAll(notes);
        sharedPreferences.edit().remove("notes").apply();
        sharedPreferences.edit().putStringSet("notes", set).apply();


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateFull = new Date();
        Calendar calendar = Calendar.getInstance();

        String date = dateFormat.format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.dateText);

        textViewDate.setText("Date Modified: " + date);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onTimeSet(TimePicker view, int hour, int min)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startNotif(c);
    }

    private void updateTimeText(Calendar c)
    {
        String timeText = "Set notification timer: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        MyView.setText(timeText);
    }

    private void startNotif(Calendar c)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelNotif()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        MyView.setText("Canceled");
    }

}
