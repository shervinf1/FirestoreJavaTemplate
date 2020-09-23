package com.shervinf.firestorejavatemplate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //Declaring member variables
    private FirebaseAuth mAuth;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Getting the SharedPreferences value named logged and setting it to a sp variable.
        sp = getSharedPreferences("logged", Context.MODE_PRIVATE);
        //Getting current instance of Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }





    /*Overriding the OnCreateOptionsMenu to inflate the menu layout called main_activity_menu in
    order to add icon to the toolbar for loggin out. By going into the menu folder in the res
    directory you will see the layout file and are able to add or remove icons for your preference.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }


    /*Overriding the onOptionsItemSelected in order to listen and see if the icon in the toolbar
    that we inflated in the onCreateOptionsMenu was selected and if selected what action will
    take place.*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*If statement to see which icon was selected. If using multiple icons using switch statement
         would be more useful and clean.*/
        if (item.getItemId() == R.id.logout) {
            //Calling the alert sign out method when the icon with ID logout is clicked.
            alertSignout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*Method that builds an alert dialog that is displayed to the user if they clicked the logout
    icon on the top right hand corner of the main activity and confirms if the user wants to logout.*/
    private void alertSignout() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
        // Setting Dialog Title
        alertDialog2.setTitle("Confirm Sign Out");
        // Setting Dialog Message
        alertDialog2.setMessage("Are you sure you want to Sign out?");
        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Calling the signOut() method from FirebaseAuth to remove user logged in.
                        FirebaseAuth.getInstance().signOut();
                        //Setting the SharedPreference value used to keep user logged in as false because user logged out.
                        sp.edit().putBoolean("logged",false).apply();
                        //Creating intent to go back to the Login activity.
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });
        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // Showing Alert Dialog
        alertDialog2.show();
    }


}