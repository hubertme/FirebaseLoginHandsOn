package com.example.hubertwangg.firebaselogin_handson;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_signIn;
    EditText editText_email, editText_password;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button_signIn = (Button) findViewById(R.id.button_signIn);
        button_signIn.setOnClickListener(this);

        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user has signed in!
        if (mAuth.getCurrentUser() != null){
            Global.currentUser = mAuth.getCurrentUser();
            Intent navigateToWelcomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
            LoginActivity.this.startActivity(navigateToWelcomeActivity);
            finish();

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_signIn){
            String emailInput = editText_email.getText().toString().trim();
            String passwordInput = editText_password.getText().toString().trim();

            System.out.println("Input: "+emailInput+" "+passwordInput);

            if (emailInput.length()==0){
                editText_email.setError("This field is required!");
                Toast.makeText(this, "Email is empty!", Toast.LENGTH_SHORT).show();
            } else if (passwordInput.length()==0){
                editText_password.setError("This field is required");
                Toast.makeText(this, "Password is empty!", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("sign in");
                handleUserSignIn(emailInput, passwordInput);
            }
        }
    }

    // Methods
    private void handleUserSignIn(String email, String password){
        System.out.println("Email: "+email);
        System.out.println("Password: "+password);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            AlertDialog.Builder alertBuilder;

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Global.currentUser = mAuth.getCurrentUser();

                    alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertBuilder.setTitle("Signed in!")
                            .setMessage("Welcome to the application, "+Global.currentUser.getEmail())
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.out.println("Okay!");
                                    Intent navigateToWelcomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                                    LoginActivity.this.startActivity(navigateToWelcomeActivity);
                                    finish();
                                }
                            })
                            .show();
                } else {
                    alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertBuilder.setTitle("Failed to sign in!")
                            .setMessage("Failed to sign up with entered credential!")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });
    }
}
