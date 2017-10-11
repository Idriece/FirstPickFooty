package edu.gsu.httpscs.firstpickfooty;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gsu.httpscs.firstpickfooty.adapter.ListNormalAdapter;
import edu.gsu.httpscs.firstpickfooty.broadcast.GameAlarmReceiver;
import edu.gsu.httpscs.firstpickfooty.database.GameInfoDatabaseHelper;
import edu.gsu.httpscs.firstpickfooty.database.UserDatabaseHelper;
import edu.gsu.httpscs.firstpickfooty.dialog.CustomDialog;
import edu.gsu.httpscs.firstpickfooty.service.GameAlarmService;

import static android.R.attr.button;

public class NewGameActivity extends BaseActivity {

    Button button;
    private SQLiteDatabase sqLiteDatabase;
    private UserDatabaseHelper userDatabaseHelper;
    private GameInfoDatabaseHelper gameInfoDatabaseHelper;

    FloatingActionButton fabAdd;
    FloatingActionButton fabAddPlayer;

    TextView fabAddPlayerTV;

    Animation fabOpen;
    Animation fabClose;
    Animation rotateClockwise;
    Animation rotateCounterClockwise;

    private UserSession userSession;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private ListNormalAdapter adapter;
    private Calendar alarmCalendar;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;


    private boolean isOpen = false;
    private String username;
    private String date;
    private String time;
    private String location;
    private String mapLocation;
    private int playerListSize;
    ArrayList<String> players = new ArrayList<>();


    @BindView(R.id.activity_new_game_date_et)
    EditText dateEditText;

    @BindView(R.id.activity_new_game_time_et)
    EditText timeEditText;

    @BindView(R.id.activity_new_game_location_et)
    EditText locationEditText;

    @BindView(R.id.activity_new_game_lv)
    ListView listView;

    @OnClick(R.id.activity_new_game_ib)
    public void imageButton(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        setLocation();
        location = mapLocation;
    }

    @OnClick(R.id.activity_new_game_bt)
    public void button(View view) {
        location = locationEditText.getText().toString();
        if(date == null || time == null || location == "" || players == null) {
            shortToast("Please fill in all criteria");
        }else {
            for (int i = 0; i < playerListSize; i++) {
                gameInfoDatabaseHelper.addGame(players.get(i), date, time, location);
            }
            setAlarm();
            shortToast("Game Created");
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        ButterKnife.bind(this);
        userDatabaseHelper = new UserDatabaseHelper(this);
        gameInfoDatabaseHelper = new GameInfoDatabaseHelper(this);
        userSession = new UserSession(this);
        button = (Button)findViewById(R.id.activity_new_game_bt);
        alarmCalendar = Calendar.getInstance();


        clickFabAdd();
        clickFabAddPlayer();
        setDate();
        setTime();
        setLocation();
    }

    public void clickFabAdd() {

        fabAddPlayerTV = (TextView) findViewById(R.id.fab_add_player_tv);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotateCounterClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_counter_clockwise);

        fabAdd = (FloatingActionButton) findViewById(R.id.new_game_fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOpen) {
                    closeFabAnimation();
                } else {
                    openFabAnimation();
                }
            }
        });

        fabAddPlayer = (FloatingActionButton) findViewById(R.id.fab_add_player);
    }

    public void clickFabAddPlayer() {
        fabAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabAnimation();
                multiChoiceDialog();
            }
        });
    }

    public void closeFabAnimation() {
        fabAddPlayer.startAnimation(fabClose);
        fabAddPlayerTV.startAnimation(fabClose);
        fabAdd.startAnimation(rotateCounterClockwise);
        fabAddPlayer.setClickable(false);
        isOpen = false;
    }

    public void openFabAnimation() {

        fabAddPlayer.startAnimation(fabOpen);
        fabAddPlayerTV.startAnimation(fabOpen);
        fabAdd.startAnimation(rotateClockwise);
        fabAddPlayer.setClickable(true);
        isOpen = true;
    }

    public void setDate() {
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NewGameActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateEditText.setText(month + "/" + dayOfMonth + "/" + year);
                alarmCalendar.set(Calendar.YEAR, year);
                alarmCalendar.set(Calendar.MONTH, month);
                alarmCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date = dateEditText.getText().toString();
                shortToast(date);
            }
        };

    }

    public void setTime() {
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(NewGameActivity.this, timeSetListener, hour, minute, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
                alarmCalendar.set(Calendar.MINUTE, minute - 1);
                String am_pm = "AM";
                if (hour > 12) {
                    am_pm = "PM";
                    hour -= 12;
                } else if (hour == 12) {
                    am_pm = "PM";
                } else {
                    am_pm = "AM";
                    if (hour == 0) {
                        hour += 12;
                    }
                }
                timeEditText.setText(hour + ":" + minute + " " + am_pm);
                time = timeEditText.getText().toString();
                shortToast(time);
            }
        };
    }

    public void setLocation() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            String address = bundle.getString("address");
            String city = bundle.getString("city");
            String state = bundle.getString("state");
            String country = bundle.getString("country");
            String zipCode = bundle.getString("zipCode");
            mapLocation = address + ", " + city + ", " + state + ", " + country + " " + zipCode;
//            locationEditText.setText(mapLocation);
        }
        locationEditText.setText(mapLocation);
    }

    ArrayList<Integer> choices = new ArrayList<>();
    private void multiChoiceDialog() {
        Cursor result = userDatabaseHelper.getInformation();

        ArrayList<String> userList = new ArrayList<>();
        ArrayList<Boolean> checkList = new ArrayList<>();

        while(result.moveToNext()) {
            userList.add(result.getString(0));
            checkList.add(false);
        }
        final String[] items = new String[userList.size()];
        for(int i = 0; i < checkList.size(); i++) {
            items[i] = userList.get(i);
        }
        final boolean initChoiceSets[] = new boolean[checkList.size()];
        for(int i = 0; i < checkList.size(); i++) {
            initChoiceSets[i] = false;
        }
        choices.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Players");
        builder.setMultiChoiceItems(items, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            choices.add(which);
                        } else {
                            choices.remove(which);
                        }
                    }
                });
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int size = choices.size();
                        for(int i = 0; i < size; i++) {
                            players.add(items[choices.get(i)]);
                        }
                        playerListSize = players.size();
                        for(int i = 0; i < players.size(); i++) {
                            shortToast("You added: " + players.get(i));
                        }

                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                shortToast("Cancel was clicked");
                showListView();
            }
        });
        builder.show();
    }

    public void showListView() {
        adapter = new ListNormalAdapter(this, players);
        listView.setAdapter(adapter);


    }


    public void showMessage(String title,String Message){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void setAlarm() {
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(NewGameActivity.this, GameAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
    }

//    public class GameAlarmReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Intent service1 = new Intent(context, GameAlarmService.class);
//            context.startService(service1);
//        }
//    }


}