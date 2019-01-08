package com.mdg.finalblog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail;
    private  EditText regPass;
    private  EditText regConfPass;
    private Button regCreateAcc;
    private Button regBacktologin;
    private ProgressBar regProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        Menu menu= navigation.getMenu();
        MenuItem menuItem=menu.getItem(1);
        menuItem.setChecked(true);

        mAuth=FirebaseAuth.getInstance();

        regEmail=(EditText)findViewById(R.id.reg_email);
        regPass=(EditText)findViewById(R.id.reg_password);
        regConfPass=(EditText)findViewById(R.id.reg_conf_pass);
        regCreateAcc=(Button)findViewById(R.id.reg_create_btn);
        regBacktologin=(Button)findViewById(R.id.reg_backto_login);
        regProgress=(ProgressBar)findViewById(R.id.reg_progress);




        regCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =regEmail.getText().toString();
                String password = regPass.getText().toString();
                String confirmpassword = regConfPass.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) & !TextUtils.isEmpty(confirmpassword)) {
                    if(password.equals(confirmpassword)){

                        regProgress.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    sendToMain();
                                }
                                else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error :" + errorMessage, Toast.LENGTH_LONG).show();
                                }
                                regProgress.setVisibility(View.INVISIBLE);


                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();

                    }

                }


            }
        });


        regBacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_to_login_intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(back_to_login_intent);

            }
        });


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.navigation_home:
                        Intent intent=new Intent(RegisterActivity.this,Pew.class);
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
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser !=null){
            sendToMain();




        }



    }

    private void sendToMain() {
        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
