package com.google.firebase.quickstart.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef = database.getReference();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Toolbar myToolbar;
    CurrentUser user;

    TextView bmi;
    TextView carbs;
    TextView fat;
    TextView allKcal;
    TextView curKcal;
    TextView protein;
    public String dateFormat;

    public RoundCornerProgressBar proteinBar;
    public RoundCornerProgressBar carbsBar;
    public RoundCornerProgressBar fatBar;

    public TextView proteinGoal;
    public TextView carbsGoal;
    public TextView fatGoal;

    public TextView curProteinGoal;
    public TextView curCarbsGoal;
    public TextView curFatGoal;

    public TextView proteinPoz;
    public TextView carbsPoz;
    public TextView fatPoz;
    public NumberFormat nf = DecimalFormat.getInstance();

    public FloatingActionButton addProductToDatabase;
    public FloatingActionButton addProductToList;
    public FloatingActionButton addActivityToDatabase;
    public FloatingActionButton addActivityToList;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
        nf.setMaximumFractionDigits(0);


        addActivityToDatabase = (FloatingActionButton) findViewById(R.id.addActivityToDatabase);
        addActivityToList = (FloatingActionButton)findViewById(R.id.addActivityToList);
        addProductToDatabase = (FloatingActionButton)findViewById(R.id.addProductToDatabase);
        addProductToList= (FloatingActionButton)findViewById(R.id.addProductToList);


        addProductToDatabase.setOnClickListener(addProductToDatabaseOnClick);
        addProductToList.setOnClickListener(addProductToListOnClick);
        addActivityToDatabase.setOnClickListener(addActivityToDatabaseOnClick);
        addActivityToList.setOnClickListener(addActivityToListOnClick);


        bmi = (TextView) findViewById(R.id.bmi);
        carbs = (TextView) findViewById(R.id.carbs);
        fat = (TextView) findViewById(R.id.fat);
        protein = (TextView) findViewById(R.id.protein);
        allKcal = (TextView) findViewById(R.id.allKcal);
        curKcal = (TextView) findViewById(R.id.curKcal);
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);
        String curId = mAuth.getUid();

        proteinGoal = (TextView) findViewById(R.id.proteinGoal);
        carbsGoal = (TextView) findViewById(R.id.carbsGoal);
        fatGoal = (TextView) findViewById(R.id.fatGoal);

        curProteinGoal = (TextView) findViewById(R.id.curProteinGoal);
        curCarbsGoal = (TextView) findViewById(R.id.curCarbsGoal);
        curFatGoal = (TextView) findViewById(R.id.curFatGoal);

        proteinBar = (RoundCornerProgressBar) findViewById(R.id.proteinBar);
        carbsBar = (RoundCornerProgressBar) findViewById(R.id.carbsBar);
        fatBar = (RoundCornerProgressBar) findViewById(R.id.fatBar);

        proteinPoz = (TextView) findViewById(R.id.proteinPoz);
        carbsPoz = (TextView) findViewById(R.id.carbsPoz);
        fatPoz = (TextView) findViewById(R.id.fatPoz);

        proteinBar.setProgressColor(Color.parseColor("#ed3b27"));
        proteinBar.setProgressBackgroundColor(Color.parseColor("#808080"));
        proteinBar.setMax(100);


        carbsBar.setProgressColor(Color.parseColor("#ed3b27"));
        carbsBar.setProgressBackgroundColor(Color.parseColor("#808080"));
        carbsBar.setMax(100);


        fatBar.setProgressColor(Color.parseColor("#ed3b27"));
        fatBar.setProgressBackgroundColor(Color.parseColor("#808080"));
        fatBar.setMax(100);

        setGoalMacro();
        setCurrentMacro();
        setCurKcal();
    }

    View.OnClickListener addProductToDatabaseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserProfile.this,AddProductToDatabase.class);
            startActivity(intent);
        }
    };
    View.OnClickListener addProductToListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserProfile.this,AddDailyProducts.class);
            startActivity(intent);
        }
    };
    View.OnClickListener addActivityToDatabaseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserProfile.this,AddActivityToDatabase.class);
            startActivity(intent);
        }
    };
    View.OnClickListener addActivityToListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserProfile.this,AddDailyActivity.class);
            startActivity(intent);
        }
    };

    public void setGoalMacro() {
        myRef.child("users").child(mAuth.getUid()).child("macro").addValueEventListener(new ValueEventListener() {
            Map<String, String> map = new HashMap<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        map.put(noteDataSnapshot.getKey(), String.valueOf(noteDataSnapshot.getValue()));
                    }
                }
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    switch (entry.getKey()) {
                        case "kcal":

                            allKcal.setText(entry.getValue());
                            break;
                        case "carbs":
                            carbs.setText(nf.format(Double.valueOf(entry.getValue())));
                            carbsGoal.setText(nf.format(Double.valueOf(entry.getValue())));

                            break;
                        case "protein":
                            protein.setText(nf.format(Double.valueOf(entry.getValue())));
                            proteinGoal.setText(nf.format(Double.valueOf(entry.getValue())));

                            break;
                        case "fat":
                            fat.setText(nf.format(Double.valueOf(entry.getValue())));
                            fatGoal.setText(nf.format(Double.valueOf(entry.getValue())));

                            break;
                        case "bmi":
                            bmi.setText(nf.format(Double.valueOf(entry.getValue())));

                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setCurrentMacro() {
        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("curMacro").addValueEventListener(new ValueEventListener() {
            Map<String, String> map = new HashMap<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        map.put(noteDataSnapshot.getKey(), String.valueOf(noteDataSnapshot.getValue()));
                    }
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        switch (entry.getKey()) {
                            case "carbs":
                                curCarbsGoal.setText(entry.getValue());
                                myRef.child("users").child(mAuth.getUid()).child("macro").addValueEventListener(new ValueEventListener() {
                                    Map<String, String> map = new HashMap<>();

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot noteDataSnapshotSecond : dataSnapshot.getChildren()) {
                                                map.put(noteDataSnapshotSecond.getKey(), String.valueOf(noteDataSnapshotSecond.getValue()));
                                            }
                                            for (Map.Entry<String, String> entrySecond : map.entrySet()) {
                                                if (entrySecond.getKey().equals("carbs")) {
                                                    Double pom = Double.valueOf(entrySecond.getValue()) - Double.valueOf(entry.getValue());
                                                    carbsPoz.setText(nf.format(pom));

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                break;


                            case "fat":
                                curFatGoal.setText(entry.getValue());
                                myRef.child("users").child(mAuth.getUid()).child("macro").addValueEventListener(new ValueEventListener() {
                                    Map<String, String> map = new HashMap<>();

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot noteDataSnapshotSecond : dataSnapshot.getChildren()) {
                                                map.put(noteDataSnapshotSecond.getKey(), String.valueOf(noteDataSnapshotSecond.getValue()));
                                            }
                                            for (Map.Entry<String, String> entrySecond : map.entrySet()) {
                                                if (entrySecond.getKey().equals("fat")) {
                                                    Double pom = Double.valueOf(entrySecond.getValue()) - Double.valueOf(entry.getValue());
                                                    fatPoz.setText(nf.format(pom));

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                break;

                            case "protein":
                                curProteinGoal.setText(entry.getValue());
                                myRef.child("users").child(mAuth.getUid()).child("macro").addValueEventListener(new ValueEventListener() {
                                    Map<String, String> map = new HashMap<>();

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot noteDataSnapshotSecond : dataSnapshot.getChildren()) {
                                                map.put(noteDataSnapshotSecond.getKey(), String.valueOf(noteDataSnapshotSecond.getValue()));
                                            }
                                            for (Map.Entry<String, String> entrySecond : map.entrySet()) {
                                                if (entrySecond.getKey().equals("protein")) {
                                                    Double pom = Double.valueOf(entrySecond.getValue()) - Double.valueOf(entry.getValue());
                                                    proteinPoz.setText(nf.format(pom));

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("curMacro").setValue(new curMacro(0, 0, 0, 0, 0));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    };

    public void setCurKcal() {
        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).addValueEventListener(new ValueEventListener() {
            Map<String, String> map = new HashMap<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        map.put(noteDataSnapshot.getKey(), String.valueOf(noteDataSnapshot.getValue()));
                    }
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        switch (entry.getKey()) {
                            case "kcal":
                                Double pom = Double.valueOf(entry.getValue());
                                curKcal.setText(nf.format(pom));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //ustawienie trzech kropeczek
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //ustawienie zdarzenia po wybraniu opcji w trzech kropeczkach
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(UserProfile.this, Settings.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(UserProfile.this, EmailPasswordActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }

        return false;
    }

    //menu z lewej strony
    public void createDrawer() {

        PrimaryDrawerItem menu = new PrimaryDrawerItem().withIdentifier(1).withName("Menu").withSelectable(false);
        SecondaryDrawerItem profil = new SecondaryDrawerItem().withIdentifier(2).withName("Profil");
        SecondaryDrawerItem edytujProfil = new SecondaryDrawerItem().withIdentifier(3).withName("Edytuj Profil");
        SecondaryDrawerItem dodajDoBazy = new SecondaryDrawerItem().withIdentifier(4).withName("Dodaj produkt do bazy");
        SecondaryDrawerItem dodajAktywnoscDoBazy = new SecondaryDrawerItem().withIdentifier(5).withName("Dodaj aktywność do bazy");
        SecondaryDrawerItem dodajDoDziennejListy = new SecondaryDrawerItem().withIdentifier(6).withName("Dodaj produkt do dziennej listy");
        SecondaryDrawerItem dodajDoDziennejListyAktywnosc = new SecondaryDrawerItem().withIdentifier(7).withName("Dodaj aktywność do dziennej listy");
        SecondaryDrawerItem edytujAktywnosc = new SecondaryDrawerItem().withIdentifier(8).withName("Edytuj dodaną aktywność ");


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(android.R.drawable.dark_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(mAuth.getCurrentUser().getEmail()).withIcon(getResources().getDrawable(R.drawable.andrzej))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(myToolbar)
                .withDrawerLayout(R.layout.drawer_layout)

                .addDrawerItems(menu, profil, edytujProfil, dodajDoBazy, dodajAktywnoscDoBazy, dodajDoDziennejListy, dodajDoDziennejListyAktywnosc, edytujAktywnosc)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        System.out.println("position +++++++++++++++++++++ " + position);

                        Intent intent;
                        switch (position) {
                            case 1:
                                break;
                            case 2:
                                intent = new Intent(UserProfile.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(UserProfile.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(UserProfile.this, AddProductToDatabase.class);
                                startActivity(intent);
                                break;

                            case 5:
                                intent = new Intent(UserProfile.this, AddActivityToDatabase.class);
                                startActivity(intent);
                                break;

                            case 6:
                                intent = new Intent(UserProfile.this, AddDailyProducts.class);
                                startActivity(intent);
                                break;
                            case 7:
                                intent = new Intent(UserProfile.this, AddDailyActivity.class);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(UserProfile.this, AddProductWithFloatingButton.class);
                                startActivity(intent);
                            default:
                                break;
                        }
                        return true;
                    }
                }).build();
    }
}