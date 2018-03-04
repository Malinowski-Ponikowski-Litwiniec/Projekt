package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
            String reg = "([0-9]+)";
            String ageString = age.getText().toString();
            String weightString = weight.getText().toString();
            String heightString = height.getText().toString();
            if (ageString.isEmpty() || weightString.isEmpty() || heightString.isEmpty()) {
                Toast.makeText(Goal.this, "Wszystkie pola muszą być uzupełnione", Toast.LENGTH_SHORT).show();
            }else if (!ageString.matches(reg) || !weightString.matches(reg) || !heightString.matches(reg)) {
                Toast.makeText(Goal.this, "Podaj poprawne dane", Toast.LENGTH_SHORT).show();

            }else if(Integer.valueOf(ageString) >150){
                Toast.makeText(Goal.this,"Podany wiek jest za wysoki",Toast.LENGTH_SHORT).show();
            }else if(Integer.valueOf(weightString) > 200){
                Toast.makeText(Goal.this,"Podana waga jest za duża",Toast.LENGTH_SHORT).show();
            }else if(Integer.valueOf(heightString) > 150){
                Toast.makeText(Goal.this,"Podany wzrost jest za duży",Toast.LENGTH_SHORT).show();
            }

            else {
                CurrentUser user = new CurrentUser(Integer.valueOf(age.getText().toString()), Double.valueOf(weight.getText().toString()),
                        Double.valueOf(height.getText().toString()), activity.getSelectedItem().toString(), sex.getSelectedItem().toString());
                RealtimeDatabase rd = new RealtimeDatabase();
                rd.setValue(user);
                Toast.makeText(Goal.this, "Wysłano", Toast.LENGTH_LONG).show();
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
