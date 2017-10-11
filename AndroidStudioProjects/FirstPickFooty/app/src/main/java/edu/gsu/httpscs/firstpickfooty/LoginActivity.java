package edu.gsu.httpscs.firstpickfooty;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.gsu.httpscs.firstpickfooty.database.UserDatabaseHelper;

public class LoginActivity extends BaseActivity implements View.OnTouchListener {

    private UserDatabaseHelper db;
    private UserSession userSession;
    private GestureDetector gestureDetector;
    private int sumX = 0;
    private int sumY = 0;
    private boolean registerSet = false;

    @BindView(R.id.activity_login_rl)
    RelativeLayout loginLayout;

    @BindView(R.id.login_username_et)
    EditText editTextUsername;

    @BindView(R.id.login_password_et)
    EditText editTextPassword;

    @BindView(R.id.register_username_et)
    EditText editTextRegisterUser;

    @BindView(R.id.register_password_et)
    EditText editTextRegisterPass;

    @BindView(R.id.register_name_et)
    EditText editTextRegisterName;

    @BindView(R.id.register_position_et)
    EditText editTextRegisterPosition;

    @BindView(R.id.register_rl)
    RelativeLayout relativeLayout;

    @OnClick(R.id.register_bt)
    public void register() {
        setRegister();
    }

    @OnClick(R.id.register_bt2)
    public void completeRegistration() {
        registerUser();
    }

    @OnClick(R.id.login_bt)
    public void clickLogin() {
        login();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        db = new UserDatabaseHelper(this);
        userSession = new UserSession(this);
        gestureDetector = new GestureDetector(this, new LoginActivity.simpleGestureListener());
        loginLayout.setOnTouchListener(this);
        loginLayout.setFocusable(true);
        loginLayout.setClickable(true);
        loginLayout.setLongClickable(true);

        if(userSession.loggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

    public void setRegister() {
        editTextUsername.setVisibility(View.INVISIBLE);
        editTextPassword.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);

        Button login = (Button)findViewById(R.id.login_bt);
        login.setVisibility(View.INVISIBLE);
        login.setClickable(false);
        Button register = (Button)findViewById(R.id.register_bt);
        register.setVisibility(View.INVISIBLE);
        register.setClickable(false);
        Button register2 = (Button)findViewById(R.id.register_bt2);
        register2.setVisibility(View.VISIBLE);
        register2.setClickable(true);
    }

    public void registerUser() {
        String username = editTextRegisterUser.getText().toString();
        String password = editTextRegisterPass.getText().toString();
        String name = editTextRegisterName.getText().toString();
        String position = editTextRegisterPosition.getText().toString();
        if(username.isEmpty() && password.isEmpty()) {
            shortToast("Please enter username and password");
        }else {
            db.addUser(name, username, password, position);
            Button login = (Button)findViewById(R.id.login_bt);
            login.setVisibility(View.VISIBLE);
            login.setClickable(true);
            shortToast("User Registered");
        }
    }

    public void login() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if(db.getUser(username, password)) {
            userSession.setLoggedIn(true, username);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            shortToast("Wrong username/password");
        }
    }

    public void unsetRegister() {
        editTextUsername.setVisibility(View.VISIBLE);
        editTextPassword.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);

        Button login = (Button)findViewById(R.id.login_bt);
        login.setVisibility(View.VISIBLE);
        login.setClickable(true);
        Button register = (Button)findViewById(R.id.register_bt);
        register.setVisibility(View.VISIBLE);
        register.setClickable(true);
        Button register2 = (Button)findViewById(R.id.register_bt2);
        register2.setVisibility(View.INVISIBLE);
        register2.setClickable(false);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class simpleGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            sumX += distanceX;
            sumY += distanceY;
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (sumX < 0) {
                if (Math.abs(sumX) > 200 && registerSet == false) {
//                    shortToast("You scroll from left to right");
                    setRegister();
                    registerSet = true;
                }
            }
            if (sumX > 0) {
                if (Math.abs(sumX) > 200 && registerSet == true) {
//                    shortToast("You scroll from right to left");
                    unsetRegister();
                    registerSet = false;
                }
            }
//            if (sumY < 0) {
//                if (Math.abs(sumY) > 200) {
//                    shortToast("You scroll from top to bottom");
//                }
//            }
//            if (sumY > 0) {
//                if (Math.abs(sumY) > 200) {
//                    shortToast("You scroll from bottom to top");
//                }
//            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
