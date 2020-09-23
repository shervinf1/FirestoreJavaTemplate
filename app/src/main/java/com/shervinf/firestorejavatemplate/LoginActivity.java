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

    //Declaring member variables
    private EditText mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth mAuth;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Hiding the support action bar in order to display custom toolbar.
        Objects.requireNonNull(getSupportActionBar()).hide();
        //Calling ifUserLoggedIn() in order to check if the user is logged in .
        ifUserLoggedIn();
        mAuth = FirebaseAuth.getInstance();
    }


    /*
     *This method is used to check if the SharedPreferences variable 'logged' is true or false to
     * know weather to send the user to the main activity directly or to attempt the log in instead
     * seeing as the launcher activity in this application is set to be the login activity.
     * */
    private void ifUserLoggedIn(){
        sp = getSharedPreferences("logged",MODE_PRIVATE);
        mEmailView = findViewById(R.id.login_email);
        mPasswordView = findViewById(R.id.login_password);
        if(sp.getBoolean("logged",false)){
            //Sending the user to the main activity
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



    // Method that is executed when the sign in button is pressed in this activity.
    public void signInExistingUser(View v)   {
        //Calling the method to attempt to login the user
        attemptLogin();
    }




    //Method that is executed when the register button is pressed in this activity.
    public void registerNewUser(View v) {
        //Code below sends the user to the register activity and finishes this activity.
        Intent intent = new Intent(this, com.shervinf.firestorejavatemplate.RegisterActivity.class);
        finish();
        startActivity(intent);
    }




    /*
     *This method takes the email and password the user has entered, validates if the user has
     * actually entered password and email and if they have the firestore method
     * signInWithEmailAndPassword() will be called with the email and password that the user entered
     *  as arguments. If successful user will be taken to the main activity and if not successful
     * error Toast message will be displayed.
     * */
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
                    //If the log in was successful the SharedPreferences variable 'logged' will be
                    // changed to true to keep the user logged in and then user will be sent to the main activity.
                    sp.edit().putBoolean("logged",true).apply();
                    goToMainActivity();
                }

            }
        });
    }



    /*
     *Method that is used to create intent for main activity and then finished this activity when called.
     * */
    public void goToMainActivity(){
        Intent chatIntent = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        startActivity(chatIntent);
    }



    /*
     *Method used to display an error dialog for any errors.
     * */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /*
     *Method used for when the forget password text view is clicked.
     * */
    public void resetPasswordLogin(View view) {
        goToForgotPasswordActivity();
    }





    /*
     *Method that is used to create new intent for forgot password activity and finishes current activity.
     * */
    private void goToForgotPasswordActivity() {
        Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPassword.class);
        finish();
        startActivity(forgotPasswordIntent);
    }
}
