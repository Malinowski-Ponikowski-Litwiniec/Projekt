package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectDate extends AppCompatActivity {
    public ListView listView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        listView = (ListView)findViewById(R.id.listView);

        myRef.child("lista").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    list.add(noteDataSnapshot.getKey());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        ArrayList arrayList = new ArrayList();
                       myRef.child("lista").child(list.get(position)).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                              arrayList.add(dataSnapshot.getValue());
                               ArrayAdapter<String> secondArrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, arrayList);
                               listView.setAdapter(secondArrayAdapter);
                           }



                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
