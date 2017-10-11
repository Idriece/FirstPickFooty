package edu.gsu.httpscs.firstpickfooty;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import edu.gsu.httpscs.firstpickfooty.adapter.GameListAdapter;
import edu.gsu.httpscs.firstpickfooty.adapter.ListNormalAdapter;
import edu.gsu.httpscs.firstpickfooty.database.GameInfoDatabaseHelper;

public class ScheduleActivity extends AppCompatActivity {

    private GameInfoDatabaseHelper gameInfoDatabaseHelper;
    private UserSession userSession;
    private String user;
    private ArrayList<String> contentList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        gameInfoDatabaseHelper = new GameInfoDatabaseHelper(this);
        userSession = new UserSession(this);
        user = userSession.getUser();
        Cursor cursor = gameInfoDatabaseHelper.getGames(user);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        contentList = new ArrayList<String>();

        while (cursor.moveToNext()) {
            contentList.add("Date: " +cursor.getString(0) + "\n"
                    + "Time: " +cursor.getString(1) + "\n"
                    + "Location: " +cursor.getString(2));

//            if(cursor.getString(0) != (month + "/" + day + "/" + year)) {
//
//                Toast.makeText(this, "dayum", Toast.LENGTH_SHORT).show();
//            }
        }

        listView = (ListView)findViewById(R.id.activity_my_group_lv);
        GameListAdapter adapter = new GameListAdapter(this, contentList);
        listView.setAdapter(adapter);

    }
}
