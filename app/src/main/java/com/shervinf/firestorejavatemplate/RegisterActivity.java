package com.shervinf.firestorejavatemplate;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    //Member Variables
    private EditText etName;
    private AutoCompleteTextView actvEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Hiding the support action bar to setup our own toolbar.
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Defining memeber variables with their pertaining variable in their layout file.
        etName = findViewById(R.id.editTextPersonName);
        actvEmail = findViewById(R.id.actvEmail);
        etPassword =findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        passwordEditorListener();
        //Calling method to displa toolbar and set icon for returning to previous activity.
        toolbarSetup();
        //Getting firebase authentication instance
        fAuth = FirebaseAuth.getInstance();
    }



    /*
    *
    * */
    private void passwordEditorListener(){
        etConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.integer.register_form_finished || actionId == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });
    }

    /*
     *Setting up the toolbar for the activity to have a back button to finish current activity and send user to previous activity
     * */
    private void toolbarSetup(){
        Toolbar mToolbar = findViewById(R.id.registerToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                finish();
                startActivity(loginIntent);
            }
        });
    }



    /*
     *Method that is called when the register button is clicked.
     * */
    public void register(View v) {
        //Method that is called when the register button is called.
        attemptRegistration();
    }




    /*
     *Method that used for verifying that what the user entered is filled in and correct
     * and attempts actual registration after inputs are invalid.
     * */
    private void attemptRegistration() {
        //Reset any errors cause by the user in the form
        actvEmail.setError(null);
        etPassword.setError(null);
        etName.setError(null);

        String name = etName.getText().toString();
        String email = actvEmail.getText().toString();
        String password = etPassword.getText().toString();
        boolean cancel = false;
        View focusView = null;

        //Verifying if the name edittext is empty or not.
        if (TextUtils.isEmpty(name)) {
            etName.setError(getString(R.string.error_name_field_required));
            focusView = etName;
            cancel = true;
        }
        //Verifting if password is not empty and valid.
        if(TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }
        //Verifying if email is empty.
        if (TextUtils.isEmpty(email)) {
            actvEmail.setError(getString(R.string.error_field_required));
            focusView = actvEmail;
            cancel = true;
        }//Verifying if email entered is valid or not.
        else if(!isEmailValid(email)) {
            actvEmail.setError(getString(R.string.error_invalid_email));
            focusView = actvEmail;
            cancel = true;
        }

        /*If statement to see if there was an error when creating account and if so request focus
        on whichever edit text was invalid or empty.*/
        if(cancel) {
            //There was ana error do not attempt login and focus the first form field with an error
            focusView.requestFocus();
        }
        else {
            //Call the method that creates the firebase user
            createFirebaseUser();
        }
    }



    /*
     *Method that returns true or false depending on if the email contains am '@' symbol.
     * */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }



    /*
     *Method that returns true or false depending on if the password contains more than  6
     * characters and if the main password and confirm password are equal.
     * */
    private boolean isPasswordValid(String password) {
        String confirmPassword = etConfirmPassword.getText().toString();
        return confirmPassword.equals(password) && password.length() > 6;
    }



    /*
     *Method that uses the email and password that was validated from the user
     * and calls the createUserWithEmailAndPassword from firebase auth and when completed it will
     * send the user to the MainActivity.
     * */
    private void createFirebaseUser() {
        String email = actvEmail.getText().toString();
        String password = etConfirmPassword.getText().toString();

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FirestoreJavaTemplate", "CreateUser on Complete: " + task.isSuccessful());

                if(!task.isSuccessful())
                {
                    Log.d("FirestoreJavaTemplate", "User creation has failed");
                    showErrorDialog("Registration attempt failed");
                }
                else {
                    //Calling createdFirebaseUser() in order to add the name, timestamp and userID
                    // to the pertaining user document.
                    createdFirebaseUser();
                    finish();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }
        });
    }




    //Create an alert dialog to show in case registration failed
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    /*
     *After the user has been created and added in firestore this method will be called which
     * basically user a UserPOJO that I have created to add the user name, UserID and time stamp
     * for when user was created to the user document.
     * */
    private void createdFirebaseUser(){
        Log.d("FirestoreJavaTemplate","User Created");
        Toast.makeText(RegisterActivity.this,R.string.success_register, Toast.LENGTH_SHORT).show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference newUserDocumentRef = db.collection("users").document(userID);
        UserPOJO newUser = new UserPOJO();
        newUser.setName(etName.getText().toString());
        newUser.setUserID(userID);
        newUserDocumentRef.set(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("FirestoreJavaTemplate","createdFirebaseUser Success");
                }
                else{
                    Log.d("FirestoreJavaTemplate","createdFirebaseUser Failed");
                }
            }
        });
    }
}