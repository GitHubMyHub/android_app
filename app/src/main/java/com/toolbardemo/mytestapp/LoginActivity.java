package com.toolbardemo.mytestapp;

import android.app.ProgressDialog;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.content.Intent;

import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.toolbardemo.mytestapp.widgets.LeftNavDrawer;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.toolbardemo.mytestapp.MainActivity.mProfile;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static Boolean bSignup = false;
    static final int LOGIN_CONTACT_REQUEST = 1;  // The request code for login
    LeftNavDrawer leftNav;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.input_username) EditText usernameText;
    @BindView(R.id.input_password) EditText passwordText;
    @BindView(R.id.btn_login) Button loginButton;
    @BindView(R.id.link_signup) Button signupLink;
    @BindView(R.id.button_twitter_login) Button loginTwitter;
    @BindView(R.id.button_google_login) SignInButton loginGoogle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        MainActivity.mAuth = FirebaseAuth.getInstance();

        /*leftNav = new LeftNavDrawer(this);
        navigationView = leftNav.setLeftNavDrawer(false);
        navigationView.setNavigationItemSelectedListener(this);*/

        toolbar.setTitle(R.string.login_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        loginTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.context, TwitterLoginActivity.class);
                startActivity(intent);
            }
        });

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GoogleSignInActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, LOGIN_CONTACT_REQUEST);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.login_authentification));
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        tryLogin(progressDialog);
                    }
                }, 3000);
    }

    private boolean tryLogin(ProgressDialog externProgressDialog) {

        final ProgressDialog progressDialog = externProgressDialog;

        MainActivity.mAuth.signInWithEmailAndPassword(usernameText.getText().toString(), passwordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, String.valueOf(task.isSuccessful()));
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = MainActivity.mAuth.getCurrentUser();
                            //updateUI(user);
                            mProfile.setUser(user);
                            onLoginSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplication(), task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            bSignup = false;
                        }
                        progressDialog.dismiss();
                    }
                });

        return bSignup;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG,"onActivityResult");
        Log.d(TAG, "" + requestCode);
        Log.d(TAG, "" + resultCode);
        if (requestCode == REQUEST_SIGNUP || requestCode == LOGIN_CONTACT_REQUEST) {
            //if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                finish();
            //}
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        leftNav.setPage(id);
        return true;
    }

    //@Override
    //public void onBackPressed() {
        // disable going back to the MainActivity
        //moveTaskToBack(true);
    //}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess");

        mProfile.savePrefs(mProfile.PROFILE_SESSION, mProfile.getUid());
        Toast.makeText(getBaseContext(), getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();
        MainActivity.mNav.getNewLeftNav(true);
        this.finish();
    }

    public void onLoginFailed() {
        //Toast.makeText(getBaseContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
        //MainActivity.mProfile.setLoginData(null, null);
        //_loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameText.setError(getResources().getString(R.string.login_email_wrong));
            valid = false;
        } else {
            usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError(getResources().getString(R.string.login_password_wrong));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }


}
