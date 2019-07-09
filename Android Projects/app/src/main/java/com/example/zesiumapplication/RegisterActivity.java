package com.example.zesiumapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zesiumapplication.retrofit.beans.Company;
import com.example.zesiumapplication.retrofit.beans.User;
import com.example.zesiumapplication.retrofit.beans.UserDetails;
import com.example.zesiumapplication.retrofit.clientinterface.ZesiumClientInterface;
import com.example.zesiumapplication.retrofit.instance.RetrofitInstance;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmEmail;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

    private final String TAG = MainActivity.class.getSimpleName();
    // annotations for validating the registration fields
    @NotEmpty
    private EditText nameAndSurname;

    @NotEmpty
    @Email
    private EditText email;

    @ConfirmEmail
    private EditText repeatEmail;

    @NotEmpty
    @Password
    @Length(min = 5, max = 15)
    private EditText password;

    @ConfirmPassword
    private EditText repeatPassword;

    @NotEmpty
    private EditText phoneNumber;


    private EditText companyName;
    private EditText organizationNumber;
    private EditText invoiceAddress;
    private Button registerButton;
    private Validator validator;

    private ZesiumClientInterface zesiumClient;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.register);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006400")));

        nameAndSurname = (EditText) findViewById(R.id.et_nameAndSurname);
        email = (EditText) findViewById(R.id.et_email);
        repeatEmail = (EditText) findViewById(R.id.et_repeatEmail);
        password = (EditText) findViewById(R.id.et_password);
        repeatPassword = (EditText) findViewById(R.id.et_repeatPassword);
        phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
        companyName = (EditText) findViewById(R.id.et_companyName);
        organizationNumber = (EditText) findViewById(R.id.et_organizationNumber);
        invoiceAddress = (EditText) findViewById(R.id.et_invoiceAddress);

        registerButton = (Button) findViewById(R.id.btn_register);

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        zesiumClient = retrofit.create(ZesiumClientInterface.class);

        validator = new Validator(this);
        validator.setValidationListener(this);

        registerButton.setOnClickListener(view -> {
            validator.validate(true);
        });
    }

    public void register() {

        User user = setUserForDetails();
        Company company = setCompanyForDetails();

        UserDetails userDetails = new UserDetails();
        userDetails.setUser(user);
        userDetails.setCompany(company);

        Call<UserDetails> addUser = zesiumClient.addUser(userDetails);

        addUser.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if(!response.isSuccessful()) {
                    try {
                        Toast.makeText(RegisterActivity.this, "Registration Failed! Reason: " + response.errorBody().string(),
                                Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Response failed with code: " + response.code());
                    } catch (IOException e) {
                        Log.e(TAG, "Error with response error body");
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Registration Failed! Reason: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public User setUserForDetails() {
        String nameAndSurnameText = nameAndSurname.getText().toString();
        String emailText = email.getText().toString();
        String repeatEmailText = repeatEmail.getText().toString();
        String passwordText = password.getText().toString();
        String repeatPasswordText = repeatPassword.getText().toString();
        String phoneNumberText = phoneNumber.getText().toString();

        User user = new User();

        user.setName(nameAndSurnameText);
        user.setEmail(emailText);
        user.setPassword(passwordText);
        user.setPhoneNumber(phoneNumberText);

        return user;
    }

    public Company setCompanyForDetails() {
        String companyNameText = companyName.getText().toString();
        String organizationNumberText = organizationNumber.getText().toString();
        String invoiceAddressText = invoiceAddress.getText().toString();

        Company company = new Company();
        company.setCompanyName(companyNameText);
        company.setOrganizationNumber(organizationNumberText);
        company.setInvoiceAddress(invoiceAddressText);

        return company;
    }

    @Override
    public void onValidationSucceeded() {
        register();
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
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
