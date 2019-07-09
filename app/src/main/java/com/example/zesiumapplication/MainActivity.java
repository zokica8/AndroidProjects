package com.example.zesiumapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zesiumapplication.retrofit.beans.Login;
import com.example.zesiumapplication.retrofit.beans.User;
import com.example.zesiumapplication.retrofit.clientinterface.ZClientInterface;
import com.example.zesiumapplication.retrofit.instance.RetrofitInstance;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private ZClientInterface zesiumClient;

    private TextView register;
    private TextView passwordRecovery;
    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        register = (TextView) findViewById(R.id.tv_register);
        passwordRecovery = (TextView) findViewById(R.id.tv_passwordRecovery);

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        zesiumClient = retrofit.create(ZClientInterface.class);

        emailText = (EditText) findViewById(R.id.et_email);
        passwordText = (EditText) findViewById(R.id.et_password);
        loginButton =(Button) findViewById(R.id.btn_login);


        passwordRecovery.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, PasswordRecoveryActivity.class);
            startActivity(intent);
        });

        register.setOnClickListener(view -> {
            Intent registerIntent = new Intent();

            registerIntent.setClass(MainActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });

        loginButton.setOnClickListener(view -> {
            login();
        });

    }

    public void login() {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            Login login = new Login();
            login.setEmail(email);
            login.setPassword(password);

            Call<User> loginUser = zesiumClient.validateUser(email, password);

            loginUser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if(!response.isSuccessful()) {
                        try {
                            Toast.makeText(MainActivity.this, "Login Failed! Reason: " + response.errorBody().string(),
                                   Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Response failed with code: " + response.code());
                        } catch (IOException e) {
                            Log.e(TAG, "Error with response error body");
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Login Failed! Reason: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
    }
}
