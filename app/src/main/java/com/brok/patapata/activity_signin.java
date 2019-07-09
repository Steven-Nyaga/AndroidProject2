package com.brok.patapata;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_signin extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    public String  user_status;
    public String acc_user = "user";
    public String acc_admin = "admin";
    public String acc_driver = "driver";
DatabaseReference Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            redirect();
        }

//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(activity_signin.this, driver.class));
//            finish();
//        }

        // set the view now
        setContentView(R.layout.activity_signin);

/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.sign_up_button);
        btnLogin = (Button) findViewById(R.id.log_button);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_signin.this, activity_signup.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_signin.this, activity_reset.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity_signin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(activity_signin.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
//                                    acc_user = "user";
//                                    acc_admin = "admin";
//                                    acc_driver = "driver";
                                    redirect();
//                                    Users = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.
//                                            getInstance().getCurrentUser().getUid());
//                                    Users.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            user_status = dataSnapshot.child("status").getValue().toString();
//                                            if (user_status.equals(acc_user)) {
//                                                Intent intent = new Intent(activity_signin.this, activity_user.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                            if(user_status.equals(acc_driver)){
//                                                Intent intent = new Intent(activity_signin.this, driver.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                            if(user_status.equals(acc_admin)){
//                                                Toast.makeText(activity_signin.this, "Use Web Admin Interface To Access This Account", Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                            Toast.makeText(activity_signin.this, "Sth Failed", Toast.LENGTH_LONG).show();
//
//                                        }
//                                    });



                                }
                            }
                        });
            }
        });
    }
    public void redirect(){
        Users = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.
                getInstance().getCurrentUser().getUid());
        Users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_status = dataSnapshot.child("status").getValue().toString();
                if (user_status.equals(acc_user)) {
                    Intent intent = new Intent(activity_signin.this, activity_user.class);
                    startActivity(intent);
                    finish();
                }
                if(user_status.equals(acc_driver)){
                    Intent intent = new Intent(activity_signin.this, driver.class);
                    startActivity(intent);
                    finish();
                }
                if(user_status.equals(acc_admin)){
                    Toast.makeText(activity_signin.this, "Use Web Admin Interface To Access This Account", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(activity_signin.this, "Sth Failed", Toast.LENGTH_LONG).show();

            }
        });
    }
}
