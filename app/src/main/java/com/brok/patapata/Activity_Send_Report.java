package com.brok.patapata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Send_Report extends AppCompatActivity {
    private EditText reportvalue;
    private Button reportB;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);


        reportvalue= (EditText) findViewById(R.id.report);
        reportB= (Button) findViewById(R.id.reportButton);
        reportB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String report = reportvalue.getText().toString();
                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.
                        getInstance().getCurrentUser().getUid()).child("reports").push().setValue(report);
            }
        });

    }
}
