package edu.gsu.httpscs.firstpickfooty.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.gsu.httpscs.firstpickfooty.ScheduleActivity;
import edu.gsu.httpscs.firstpickfooty.R;
import edu.gsu.httpscs.firstpickfooty.adapter.ListNormalAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MyGamesFragment extends Fragment {

    private final ArrayList<String> contentList;
    private final Context context;
    private ListView listView;
    private String user;


    public MyGamesFragment() {

        context = getContext();

        contentList = new ArrayList<String>();
        contentList.add("See Schedule");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_games, container, false);

        listView = (ListView)view.findViewById(R.id.fragment_my_games_lv);
        ListNormalAdapter adapter = new ListNormalAdapter(this.getContext(), contentList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                        startActivity(intent);
                        break;
                    default:;
                }
            }
        });

        return view;
    }

}
