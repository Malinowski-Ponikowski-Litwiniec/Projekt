package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddDailyProducts extends AppCompatActivity {
    public ListView listView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_products);
        listView = (ListView) findViewById(R.id.listView);
        myRef.child("produkty").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> list = new ArrayList<>();
                ArrayAdapter<String> arrayAdapter;
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String name = noteDataSnapshot.getKey();

                    list.add(name);

                }
                arrayAdapter = new ArrayAdapter<String>(AddDailyProducts.this, android.R.layout.simple_list_item_1, list);


                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String item = ((TextView)view).getText().toString();

                       Intent intent = new Intent(AddDailyProducts.this,AddDailyProductsFrom.class);
                        intent.putExtra("name",item);
                       startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("-------------------");
                System.out.println("DATABASE ERROR");
                System.out.println("-------------------");

            }

        });





}
}
