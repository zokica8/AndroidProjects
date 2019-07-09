package com.example.zesiumapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zesiumapplication.retrofit.beans.User;
import com.example.zesiumapplication.retrofit.clientinterface.ZClientInterface;
import com.example.zesiumapplication.retrofit.instance.RetrofitInstance;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PasswordRecoveryActivity extends AppCompatActivity implements Validator.ValidationListener {

    private final String TAG = PasswordRecoveryActivity.class.getSimpleName();

    @NotEmpty
    @Email
    private EditText emailText;

    @NotEmpty
    @Password
    @Length(min = 8)
    private EditText newPasswordText;

    @NotEmpty
    @ConfirmPassword
    private EditText confirmPasswordText;

    private Button passwordButton;

    private ZClientInterface zesiumClient;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006400")));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        emailText = (EditText) findViewById(R.id.et_email);
        newPasswordText = (EditText) findViewById(R.id.et_newPassword);
        confirmPasswordText = (EditText) findViewById(R.id.et_confirmPassword);

        passwordButton = (Button) findViewById(R.id.btn_done);

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        zesiumClient = retrofit.create(ZClientInterface.class);

        validator = new Validator(this);
        validator.setValidationListener(this);

        passwordButton.setOnClickListener((view -> validator.validate(true)));
    }

    public void findUser() {
        Call<List<User>> findUser = zesiumClient.findByEmail(emailText.getText().toString());

        findUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if(!response.isSuccessful()) {
                    try {
                        Toast.makeText(PasswordRecoveryActivity.this, "User Not Found!!! Reason: " + response.errorBody().string(),
                                Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Response failed with code: " + response.code());
                    } catch (IOException e) {
                        Log.e(TAG, "Error with response error body");
                        e.printStackTrace();
                    }
                }
                else {
                    List<User> users = response.body();
                    for(User user : users) {
                        if(user.getEmail().equals(emailText.getText().toString())) {
                            recoverPassword(user.getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(PasswordRecoveryActivity.this, "User Not Found!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "User Not Found! Reason: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void recoverPassword(Long id) {

        String email = emailText.getText().toString();
        String newPassword = newPasswordText.getText().toString();

        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(newPassword);

        Call<ResponseBody> updateUser = zesiumClient.updateUser(user, user.getId());

        updateUser.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    try {
                        Toast.makeText(PasswordRecoveryActivity.this, "Password Recovery Failed! Reason: " + response.errorBody().string(),
                                Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Response failed with code: " + response.code());
                    } catch (IOException e) {
                        Log.e(TAG, "Error with response error body");
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(PasswordRecoveryActivity.this, "Password Recovery Successful!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PasswordRecoveryActivity.this, "Password Recovery Failed!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Password Recovery Failed! Reason: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        findUser();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if(view instanceof EditText) {
                ((EditText) view).setError(message);
            }
            else {
                Toast.makeText(PasswordRecoveryActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
