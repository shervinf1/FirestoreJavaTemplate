package com.shervinf.firestorejavatemplate;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth mAuth;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
       ifUserLoggedIn();
        mAuth = FirebaseAuth.getInstance();
    }



    private void ifUserLoggedIn(){
        sp = getSharedPreferences("logged",MODE_PRIVATE);
        mEmailView = findViewById(R.id.login_email);
        mPasswordView = findViewById(R.id.login_password);
        if(sp.getBoolean("logged",false)){
            goToMainActivity();
        }
        else {
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });
        }
    }



    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        attemptLogin();

    }




    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.shervinf.firestorejavatemplate.RegisterActivity.class);
        finish();
        startActivity(intent);
    }





    private void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.equals("") || password.equals(""))
            return;
        else
            Toast.makeText(LoginActivity.this, "Login in Progress...", Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("ApplicationTracker", "signInWithEmail() onComplete" + task.isSuccessful());

                if(!task.isSuccessful()) {
                    Log.d("ApplicationTracker", "Problem Logging in: " + task.getException());
                    showErrorDialog("There was a problem signing in...");
                }
                else {
                    sp.edit().putBoolean("logged",true).apply();
                    goToMainActivity();
                }

            }
        });
    }




    public void goToMainActivity(){
        Intent chatIntent = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        startActivity(chatIntent);
    }




    private void showErrorDialog(String message) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public void resetPasswordLogin(View view) {
        goToForgotPasswordActivity();
    }

    private void goToForgotPasswordActivity() {
        Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPassword.class);
        finish();
        startActivity(forgotPasswordIntent);
    }


}
