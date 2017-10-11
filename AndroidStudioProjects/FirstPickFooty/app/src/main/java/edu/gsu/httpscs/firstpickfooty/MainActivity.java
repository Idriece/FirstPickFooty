package edu.gsu.httpscs.firstpickfooty;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gsu.httpscs.firstpickfooty.adapter.ViewFragmentStateAdapter;
import edu.gsu.httpscs.firstpickfooty.fragment.MyGroupsFragment;
import edu.gsu.httpscs.firstpickfooty.fragment.MyGamesFragment;
import edu.gsu.httpscs.firstpickfooty.fragment.NotificationsFragment;

public class MainActivity extends BaseActivity {

    private ArrayList<Pair<String, Fragment>> list = new ArrayList<Pair<String, Fragment>>();
    private UserSession userSession;

    FloatingActionButton fabAdd;
    FloatingActionButton fabNewGame;
    FloatingActionButton fabNewGroup;

    TextView fabNewGameTV;
    TextView fabNewGroupTV;

    Animation fabOpen;
    Animation fabClose;
    Animation rotateClockwise;
    Animation rotateCounterClockwise;

    private boolean isOpen = false;

    @BindView(R.id.activity_main_tablayout)
    TabLayout tabLayout;

    @BindView(R.id.activity_main_viewpager)
    ViewPager viewPager;


//    @OnClick(R.id.menu_logout)
//    public void clickLogout() {
//        logout();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setLogout();

        list.add(new Pair<String, Fragment>("My Groups", new MyGroupsFragment()));
        list.add(new Pair<String, Fragment>("", new NotificationsFragment()));
        list.add(new Pair<String, Fragment>("My Games", new MyGamesFragment()));
        ViewFragmentStateAdapter adapter =
                new ViewFragmentStateAdapter(this.getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_notifications_white);
        clickFabAdd();
        clickFabNewGame();
        clickFabNewGroup();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
//        logout();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        logout();
        return true;
    }


    public void clickFabAdd() {

        fabNewGameTV = (TextView) findViewById(R.id.fab_new_game_tv);
        fabNewGroupTV = (TextView) findViewById(R.id.fab_new_group_tv);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotateCounterClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_counter_clockwise);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
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

        fabNewGame = (FloatingActionButton) findViewById(R.id.fab_new_game);
        fabNewGroup = (FloatingActionButton) findViewById(R.id.fab_new_group);
    }

    public void clickFabNewGame() {
        fabNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                startActivity(intent);
                closeFabAnimation();
            }
        });
    }

    public void clickFabNewGroup() {
        fabNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewGroupActivity.class);
                startActivity(intent);
                fabNewGame.startAnimation(fabClose);
                fabNewGroup.startAnimation(fabClose);
                fabNewGameTV.startAnimation(fabClose);
                fabNewGroupTV.startAnimation(fabClose);
                fabAdd.startAnimation(rotateCounterClockwise);
                fabNewGame.setClickable(false);
                fabNewGroup.setClickable(false);
                isOpen = false;
            }
        });
    }

    public void closeFabAnimation() {
        fabNewGame.startAnimation(fabClose);
        fabNewGroup.startAnimation(fabClose);
        fabNewGameTV.startAnimation(fabClose);
        fabNewGroupTV.startAnimation(fabClose);
        fabAdd.startAnimation(rotateCounterClockwise);
        fabNewGame.setClickable(false);
        fabNewGroup.setClickable(false);
        isOpen = false;
    }

    public void openFabAnimation() {

        fabNewGame.startAnimation(fabOpen);
        fabNewGroup.startAnimation(fabOpen);
        fabNewGameTV.startAnimation(fabOpen);
        fabNewGroupTV.startAnimation(fabOpen);
        fabAdd.startAnimation(rotateClockwise);
        fabNewGame.setClickable(true);
        fabNewGroup.setClickable(true);
        isOpen = true;
    }

    public void setLogout() {
        userSession = new UserSession(this);
        if (!userSession.loggedIn()) {
            logout();
        }
    }

    public void logout() {
        userSession.setLoggedIn(false, "");
        finish();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}