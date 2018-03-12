package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Goal extends AppCompatActivity {

    public Button btn;
    public FirebaseAuth mAuth;
    public EditText age;
    public EditText weight;
    public EditText height;
    public Spinner activity;
    public Spinner sex;
    public Button sendToDatabaseBtn;
    public Button sendToDatabase;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(btnClick);
        mAuth = FirebaseAuth.getInstance();
        sendToDatabase = (Button) findViewById(R.id.sendToDatabaseBtn);
        sendToDatabase.setOnClickListener(sendToDatabaseOnClick);
        age = (EditText) findViewById(R.id.textEditAge);
        weight = (EditText) findViewById(R.id.textEditWeight);
        height = (EditText) findViewById(R.id.textEditHeight);
        activity = (Spinner) findViewById(R.id.spinnerActivityLevel);
        sex = (Spinner) findViewById(R.id.spinnerSex);

            }








    View.OnClickListener sendToDatabaseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
            Matcher matcher = pattern.matcher("88.8");
            boolean result=matcher.matches();

            String ageString = age.getText().toString();
            Matcher ageMatcher = pattern.matcher(ageString);
            String weightString = weight.getText().toString();
            Matcher weightMatcher = pattern.matcher(weightString);

            String heightString = height.getText().toString();
            Matcher heightMatcher = pattern.matcher(heightString);

            if (ageString.isEmpty() || weightString.isEmpty() || heightString.isEmpty()) {
                Toast.makeText(Goal.this, "Wszystkie pola muszą być uzupełnione", Toast.LENGTH_SHORT).show();
            }else if (!ageMatcher.matches() || !weightMatcher.matches() || !heightMatcher.matches()) {
                Toast.makeText(Goal.this, "Podaj poprawne dane", Toast.LENGTH_SHORT).show();

            }else if(Double.valueOf(ageString) >150){
                Toast.makeText(Goal.this,"Podany wiek jest za wysoki",Toast.LENGTH_SHORT).show();
            }else if(Double.valueOf(weightString) > 300){
                Toast.makeText(Goal.this,"Podana waga jest za duża",Toast.LENGTH_SHORT).show();
            }else if(Double.valueOf(heightString) > 250){
                Toast.makeText(Goal.this,"Podany wzrost jest za duży",Toast.LENGTH_SHORT).show();
           }

            else {
                CurrentUser user = new CurrentUser(age.getText().toString(), weight.getText().toString(),
                        height.getText().toString(), activity.getSelectedItem().toString(), sex.getSelectedItem().toString());
                RealtimeDatabase rd = new RealtimeDatabase();
                rd.setValue(user);
                Toast.makeText(Goal.this, "Wysłano", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Goal.this,EditOrDelete.class);
                startActivity(intent);
            }
        }
    };
    View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            mAuth.signOut();
            updateUI();

        }
    };

    private void updateUI() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);
    }


}
