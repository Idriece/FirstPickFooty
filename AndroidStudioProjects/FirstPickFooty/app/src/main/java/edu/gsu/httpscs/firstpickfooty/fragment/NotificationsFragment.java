package edu.gsu.httpscs.firstpickfooty.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.gsu.httpscs.firstpickfooty.MapsActivity;
import edu.gsu.httpscs.firstpickfooty.R;
import edu.gsu.httpscs.firstpickfooty.ScheduleActivity;
import edu.gsu.httpscs.firstpickfooty.UserSession;
import edu.gsu.httpscs.firstpickfooty.adapter.GameListAdapter;
import edu.gsu.httpscs.firstpickfooty.database.GameInfoDatabaseHelper;
import edu.gsu.httpscs.firstpickfooty.dialog.CustomDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    private GameInfoDatabaseHelper gameInfoDatabaseHelper;
    private UserSession userSession;
    private String user;
    private ArrayList<String> contentList;
    private ListView listView;
    Context context = getContext();

//    private final int DIALOG = 12345;
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch(msg.what) {
//                case DIALOG:
//                    Bundle bundle = msg.getData();
//                    String s = bundle.getString("msg");
//                    Toast.makeText(getActivity(), "Dialog Message: " + s, Toast.LENGTH_SHORT);
//                    break;
//                default:
//            }
//            super.handleMessage(msg);
//        }
//    };

    public NotificationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        gameInfoDatabaseHelper = new GameInfoDatabaseHelper(getContext());
        userSession = new UserSession(getContext());
        user = userSession.getUser();
        Cursor cursor = gameInfoDatabaseHelper.getGames(user);

        contentList = new ArrayList<String>();

        while (cursor.moveToNext()) {
            contentList.add("Date: " +cursor.getString(0) + "\n"
                    + "Time: " +cursor.getString(1) + "\n"
                    + "Location: " +cursor.getString(2));
        }

        listView = (ListView)view.findViewById(R.id.fragment_notifications_lv);
        GameListAdapter adapter = new GameListAdapter(getContext(), contentList);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                for(int i = 0; i < contentList.size(); i++) {
//                    CustomDialog customDialog = new CustomDialog(getContext(), new CustomDialog.ICustomDialogListener() {
//                        @Override
//                        public void onOkCLicked(String msg) {
//                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
//                        }
//                    });
////                    Bundle bundle = new Bundle();
////                    bundle.putString("msg", "Game Added");
////                    Message msg = Message.obtain();
////                    msg.what = DIALOG;
////                    msg.setData(bundle);
////                    mHandler.sendMessage(msg);
//                    customDialog.setCanceledOnTouchOutside(true);
//                    customDialog.show();
//                }
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        CustomDialog customDialog = new CustomDialog(getActivity(), new CustomDialog.ICustomDialogListener() {
                        @Override
                        public void onOkCLicked(String msg) {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
                        }
                    });
//                    Bundle bundle = new Bundle();
//                    bundle.putString("msg", "Game Added");
//                    Message msg = Message.obtain();
//                    msg.what = DIALOG;
//                    msg.setData(bundle);
//                    mHandler.sendMessage(msg);

                    customDialog.setCanceledOnTouchOutside(true);
                    customDialog.show();
                    default:
                }
            }
        });
        return view;
    }

}
