package com.example.wither;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    AmplifyCognito amplifyCognito;
    static String username;
    EditText etUsername;
    EditText etPassword;
    Button btnSignup;

    Button btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        amplifyCognito = new AmplifyCognito(getApplicationContext());
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignin = findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{

                    username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    amplifyCognito.signin(username, password);

//                    Intent intent = new Intent(SignInActivity.this,ChattingActivity.class);
//                    intent.putExtra("userId",username);
//                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amplifyCognito.loadSignup();
            }
        });
    }
}