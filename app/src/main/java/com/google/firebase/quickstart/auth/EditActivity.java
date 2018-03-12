package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    public Button btn;
    public FirebaseAuth mAuth;
    public EditText age;
    public EditText weight;
    public EditText height;
    public Spinner activity;
    public Spinner sex;

    public Button sendToDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public FirebaseAuth myAtuh = FirebaseAuth.getInstance();
    public String curId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
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

        curId = myAtuh.getUid();
        System.out.println("id zalogowany " + curId);

        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


             // List notes = new ArrayList<>();


                 Map<String, CurrentUser> map = new HashMap<>();
              for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
               // int wiek = noteDataSnapshot.getValue(CurrentUser.class).getWiek();
               // notes.add(wiek);
                    CurrentUser currentUser = new CurrentUser(noteDataSnapshot.getValue(CurrentUser.class).getWiek(),
                            noteDataSnapshot.getValue(CurrentUser.class).getWaga(),
                            noteDataSnapshot.getValue(CurrentUser.class).getWzrost(),
                            noteDataSnapshot.getValue(CurrentUser.class).getActivity(),
                            noteDataSnapshot.getValue(CurrentUser.class).getSex());

                    map.put(noteDataSnapshot.getKey(), currentUser);


                }


               for (Map.Entry<String, CurrentUser> entry : map.entrySet()) {
                   if (entry.getKey().equals(curId)) {

                       age.setText(entry.getValue().getWiek());
                       weight.setText(entry.getValue().getWaga());
                       height.setText(entry.getValue().getWzrost());
                       switch (entry.getValue().getActivity()) {
                           case "niska":
                               activity.setSelection(0);
                               break;
                           case "średnia":
                               activity.setSelection(1);
                               break;
                           case "wysoka":
                               activity.setSelection(2);
                               break;
                           case "bardzo wysoka":
                               activity.setSelection(3);
                               break;
                           default:
                               System.out.println("DUPA");
                               break;
                       }
                       switch (entry.getValue().getSex()) {
                           case "kobieta":
                               sex.setSelection(0);
                               break;
                           case "mężczyzna":
                               sex.setSelection(1);
                               break;

                           default:
                               System.out.println("DUPA");
                               break;
                       }

                   }
               }
          }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DUPA");
                System.out.println("-------------------");

            }

        });


    }

    View.OnClickListener sendToDatabaseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String reg = "([0-9]+)";
            String ageString = age.getText().toString();
            String weightString = weight.getText().toString();
            String heightString = height.getText().toString();
            if (ageString.isEmpty() || weightString.isEmpty() || heightString.isEmpty()) {
                Toast.makeText(EditActivity.this, "Wszystkie pola muszą być uzupełnione", Toast.LENGTH_SHORT).show();
            } else if (!ageString.matches(reg) || !weightString.matches(reg) || !heightString.matches(reg)) {
                Toast.makeText(EditActivity.this, "Podaj poprawne dane", Toast.LENGTH_SHORT).show();

            } else if (Integer.valueOf(ageString) > 150) {
                Toast.makeText(EditActivity.this, "Podany wiek jest za wysoki", Toast.LENGTH_SHORT).show();
            } else if (Integer.valueOf(weightString) > 300) {
                Toast.makeText(EditActivity.this, "Podana waga jest za duża", Toast.LENGTH_SHORT).show();
            } else if (Integer.valueOf(heightString) > 250) {
                Toast.makeText(EditActivity.this, "Podany wzrost jest za duży", Toast.LENGTH_SHORT).show();
            } else {
                CurrentUser user = new CurrentUser(age.getText().toString(), weight.getText().toString(),
                      height.getText().toString(), activity.getSelectedItem().toString(), sex.getSelectedItem().toString());
                RealtimeDatabase rd = new RealtimeDatabase();
                rd.setValue(user);
                Toast.makeText(EditActivity.this, "Wysłano", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditActivity.this, EditOrDelete.class);
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

    ;


}
