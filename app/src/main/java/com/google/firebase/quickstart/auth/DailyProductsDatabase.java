package com.google.firebase.quickstart.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linux on 14.03.18.
 */

public class DailyProductsDatabase {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Date data;
    public String name;
    public double amount;
    public double protein;
    public double carbs;
    public double fat;
    public double kcal;


    Date date = new Date();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String dateFormat = simpleDateFormat.format(date);


    public DailyProductsDatabase() {

    }

    public DailyProductsDatabase(Date data, String name, double amount, double protein, double carbs, double fat, double kcal) {
        this.data = data;
        this.name = name;
        this.amount = amount;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.kcal = kcal;
    }

    public void calculateCurrentDailyKcal() {


//        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("sniadanie").addValueEventListener(new ValueEventListener() {
//
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//
//                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
//
//
//                    }
//                }
//
//                for (Map.Entry<String, Double> entry : map.entrySet()) {
//                    curKcal += entry.getValue();
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("obiad").addValueEventListener(new ValueEventListener() {
//
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//
//                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
//
//
//                    }
//                }
//                for (Map.Entry<String, Double> entry : map.entrySet()) {
//                    curKcal += entry.getValue();
//                }
//                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        curKcalFromDatabase = Double.valueOf(String.valueOf(dataSnapshot.getValue()));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kolacja").addValueEventListener(new ValueEventListener() {
//
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//
//                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
//
//
//                    }
//                }
//                for (Map.Entry<String, Double> entry : map.entrySet()) {
//                    curKcal += entry.getValue();
//                }
//                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        curKcalFromDatabase = Double.valueOf(String.valueOf(dataSnapshot.getValue()));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("przekaski").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//
//                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
//
//
//                    }
//                }
//                for (Map.Entry<String, Double> entry : map.entrySet()) {
//                    curKcal += entry.getValue();
//                }
//                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        curKcalFromDatabase = Double.valueOf(String.valueOf(dataSnapshot.getValue()));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//


    }

//    public void calculateCurrentDailyKcalSniadanie() {
//
//        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").addListenerForSingleValueEvent(new ValueEventListener() {
//            Map<String, Double> map = new HashMap<>();
//            double curKcal = 0;
//            double curKcalFromDatabase;
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println(dataSnapshot.getKey() + " " + dataSnapshot.getValue());
//                curKcalFromDatabase = Double.valueOf(String.valueOf(dataSnapshot.getValue()));
//                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("sniadanie").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                            map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
//
//
//
//                    }
//                for (Map.Entry<String, Double> entry : map.entrySet()) {
//                        curKcal += entry.getValue();
//                    }
//                        System.out.println("curKcal " + curKcal + " _----------------------- ");
//                        System.out.println("curKcalformfatabse " + curKcalFromDatabase+ " _----------------------- ");
////                        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").setValue(curKcal + curKcalFromDatabase);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//
//
//
//    public void calculateCurrentDailyKcalObiad() {
//        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("obiad").addValueEventListener(new ValueEventListener() {
//            Map<String, Double> map = new HashMap<>();
//            double curKcal;
//            double curKcalFromDatabase;
//
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//
//                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
//
//
//                    }
//                }
//                for (Map.Entry<String, Double> entry : map.entrySet()) {
//                    curKcal += entry.getValue();
//                }
//                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        curKcalFromDatabase = Double.valueOf(String.valueOf(dataSnapshot.getValue()));
//                        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").setValue(curKcal + curKcalFromDatabase);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    public void calculateCurrentDailyKcalKolacja() {
//        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kolacja").addValueEventListener(new ValueEventListener() {
//
//            Map<String, Double> map = new HashMap<>();
//            double curKcal;
//            double curKcalFromDatabase;
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//
//                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
//
//
//                    }
//                }
//                for (Map.Entry<String, Double> entry : map.entrySet()) {
//                    curKcal += entry.getValue();
//                }
//                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        curKcalFromDatabase = Double.valueOf(String.valueOf(dataSnapshot.getValue()));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        curKcalFromDatabase = Double.valueOf(String.valueOf(dataSnapshot.getValue()));
//                        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").setValue(curKcal + curKcalFromDatabase);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    public void calculateCurrentDailyKcalPrzekaski() {
//        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("przekaski").addValueEventListener(new ValueEventListener() {
//            Map<String, Double> map = new HashMap<>();
//            double curKcal;
//            double curKcalFromDatabase;
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//
//                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
//
//
//                    }
//                }
//                for (Map.Entry<String, Double> entry : map.entrySet()) {
//                    curKcal += entry.getValue();
//                }
//
//                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        curKcalFromDatabase = Double.valueOf(String.valueOf(dataSnapshot.getValue()));
//                        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").setValue(curKcal + curKcalFromDatabase);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }
}
