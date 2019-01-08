package com.mdg.finalblog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private Button loginRegBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        Menu menu= navigation.getMenu();
        MenuItem menuItem=menu.getItem(1);
        menuItem.setChecked(true);

        loginEmailText = (EditText) findViewById(R.id.login_email);
        loginPassText = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginRegBtn = (Button) findViewById(R.id.login_reg_btn);
        mAuth = FirebaseAuth.getInstance();
        loginProgress = (ProgressBar) findViewById(R.id.progressBar);

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regIntent );
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();

                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)) {

                    loginProgress.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                sendToMain();


                            } else {

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error :" + errorMessage, Toast.LENGTH_LONG).show();

                            }

                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });

                }


            }
        });


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.navigation_home:
                        Intent intent=new Intent(LoginActivity.this,Pew.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_notifications:

                        break;

                }


                return false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            sendToMain();
        }
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
