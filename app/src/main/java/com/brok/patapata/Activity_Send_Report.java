package com.brok.patapata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Send_Report extends AppCompatActivity {
    private EditText reportvalue;
    private Button reportB;
    private FirebaseAuth auth;
    private TextView test;
    private String gotid;
    private String mwisho;
    private String input;
    private String currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);

//        Intent intent = getIntent();
//        String gotid = intent.getStringExtra("ident");
        gotid = getIncomingIntent();


       currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reportvalue= (EditText) findViewById(R.id.your_report);


        test = (TextView) findViewById(R.id.test);




        reportB= (Button) findViewById(R.id.reportButton);
        reportB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = reportvalue.getText().toString();
                mwisho = "Reporter ID: " + currentuser + " " + "Reportee ID: " +  gotid  + " " + input;
                test.setText(mwisho);

                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.
                       getInstance().getCurrentUser().getUid()).child("reports").push().setValue(mwisho);
                Intent intent = new Intent(Activity_Send_Report.this, driver.class);
                startActivity(intent);
            }
        });

    }
    private String getIncomingIntent(){
        if(getIntent().hasExtra("Reportee")){
            String userid = getIntent().getStringExtra("User ID");
            return userid;
        }

        return null;
    }

}
