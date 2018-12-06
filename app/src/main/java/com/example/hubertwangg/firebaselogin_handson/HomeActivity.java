package com.example.hubertwangg.firebaselogin_handson;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView_email;
    Button button_signOut, button_delete;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textView_email = (TextView) findViewById(R.id.textView_emailDisplay);
        button_signOut = (Button) findViewById(R.id.button_signOut);
        button_delete = (Button) findViewById(R.id.button_delete);

        textView_email.setText(Global.currentUser.getEmail());
        button_signOut.setOnClickListener(this);
        button_delete.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_signOut){
            handleUserSignOut();
        } else if (v.getId() == R.id.button_delete){
            deleteThisUser();
        }
    }

    private void handleUserSignOut(){
        Global.currentUser = null;
        FirebaseAuth.getInstance().signOut();

        Intent navigateToLoginActivity = new Intent(HomeActivity.this, LoginActivity.class);
        HomeActivity.this.startActivity(navigateToLoginActivity);
        finish();
    }

    private void deleteThisUser(){
        Global.currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    System.out.println("Successfully deleted this account!");
                    Intent navigateToLoginActivity = new Intent(HomeActivity.this, LoginActivity.class);
                    HomeActivity.this.startActivity(navigateToLoginActivity);
                    finish();
                } else {
                    AlertDialog.Builder alertBuilder;

                    alertBuilder = new AlertDialog.Builder(HomeActivity.this);
                    alertBuilder.setTitle("Failed delete this account!")
                            .setMessage("Please retry deleting!")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                    System.out.println("Failed to delete account!");
                }
            }
        });
    }
}
