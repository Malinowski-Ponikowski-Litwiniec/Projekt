package com.google.firebase.quickstart.auth;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linux on 14.03.18.
 */

public class ProductsDatabase {
    public DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("produkty");
    public FirebaseAuth myRef = FirebaseAuth.getInstance();

    public void sendProdukt(Produkt value){
        Produkt produkt = new Produkt(value.getAmount(),value.getProtein(),value.getCarbs(),value.getFat(),value.getKcal());
        mDatabase.child(value.getName()).setValue(produkt);
    }
}
