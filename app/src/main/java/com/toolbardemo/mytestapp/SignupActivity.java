package com.toolbardemo.mytestapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.toolbardemo.mytestapp.MainActivity.mProfile;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    //SignupDAO mSignup;
    public static Boolean bSignup = true;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.input_email) EditText inputEmail;
    @BindView(R.id.input_password_first) EditText passwordTextFirst;
    @BindView(R.id.input_password_second) EditText passwordTextSecond;
    @BindView(R.id.btn_signup) Button signupButton;
    @BindView(R.id.link_login) Button linkLogin;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        toolbar.setTitle(R.string.signup_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.signup_create_account));
        progressDialog.show();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if(trySignup()) {
                            onSignupSuccess();
                            progressDialog.dismiss();
                        } else {
                            // onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }
                }, 3000);
    }



    private boolean trySignup() {

        //mSignup = new SignupDAO();
        bSignup = true;


        /*if (mSignup.getRegistration(passwordTextFirst.getText().toString(),
                                    passwordTextSecond.getText().toString(),
                                    inputEmail.getText().toString().trim()).equals("Profile not created")){
            return false;
        }else{
            Log.d("tryLogin", "true");
            return true;
        }*/

        mAuth.createUserWithEmailAndPassword(inputEmail.getText().toString().trim(), passwordTextFirst.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            mProfile.setUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            bSignup = false;
                            //updateUI(null);
                        }

                    }
                });

        return bSignup;

    }

    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        setLogin();
        finish();
    }

    public void setLogin(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mProfile.setUser(user);
        }
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getResources().getString(R.string.signup_failed), Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = inputEmail.getText().toString().trim();
        String passwordFirst = passwordTextFirst.getText().toString();
        String passwordSecond = passwordTextSecond.getText().toString();


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError(getResources().getString(R.string.signup_email_wrong));
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (passwordFirst.isEmpty() || passwordFirst.length() < 4 || passwordFirst.length() > 10) {
            passwordTextFirst.setError(getResources().getString(R.string.signup_password_wrong));
            valid = false;
        } else {
            passwordTextFirst.setError(null);
        }

        if (passwordSecond.isEmpty() || passwordSecond.length() < 4 || passwordSecond.length() > 10) {
            passwordTextSecond.setError(getResources().getString(R.string.signup_password_wrong));
            valid = false;
        } else {
            passwordTextSecond.setError(null);
        }

        Log.d("PasswordEqual", Boolean.toString(passwordFirst.equals(passwordSecond)));

        if (!passwordFirst.equals(passwordSecond)){
            passwordTextFirst.setError(getResources().getString(R.string.signup_password_equal));
            passwordTextSecond.setError(getResources().getString(R.string.signup_password_equal));
            valid = false;
        } else {
            passwordTextFirst.setError(null);
            passwordTextSecond.setError(null);
        }

        return valid;
    }

}
