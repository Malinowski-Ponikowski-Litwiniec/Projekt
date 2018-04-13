package com.google.firebase.quickstart.auth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.usb.UsbRequest;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
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
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();

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


        myRef.child("users").child(mAuth.getUid()).child("macro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = new HashMap<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(), String.valueOf(noteDataSnapshot.getValue()));
                }
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    switch (entry.getKey()) {
                        case "protein":
                            proteinGoal.setText(entry.getValue());
                            break;
                        case "carbs":
                            carbsGoal.setText(entry.getValue());
                            break;
                        case "fat":
                            fatGoal.setText(entry.getValue());
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
        CircularProgressBar circularProgressBar = (CircularProgressBar) findViewById(R.id.bar);
        circularProgressBar.setColor(ContextCompat.getColor(UserProfile.this, R.color.accent));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(UserProfile.this, R.color.cardview_dark_background));
        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.cardview_compat_inset_shadow));
        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.activity_horizontal_margin));
        circularProgressBar.setProgress((float) 0);


        myRef.child("users").child(mAuth.getUid()).child("macro").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = new HashMap<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(), String.valueOf(noteDataSnapshot.getValue()));


                }
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getValue() + " ------ " + entry.getKey());
                    switch (entry.getKey()) {
                        case "kcal":
                            allKcal.setText(entry.getValue());
                            break;
                        case "carbs":
                            carbs.setText(entry.getValue());

                            break;
                        case "protein":
                            protein.setText(entry.getValue());

                            break;
                        case "fat":
                            fat.setText(entry.getValue());

                            break;
                        case "bmi":
                            bmi.setText(entry.getValue());

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


        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("curMacro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = new HashMap<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(), String.valueOf(noteDataSnapshot.getValue()));
                }
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    switch (entry.getKey()) {
                        case "protein":
                            curProteinGoal.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(String.valueOf(entry.getValue()))).setScale(2, RoundingMode.HALF_UP).doubleValue()));
                            double pomPro = (Double.valueOf(entry.getValue()) / Double.valueOf(proteinGoal.getText().toString())) * 100;
                            proteinBar.setProgress((float) pomPro);
                            double proteinPozDouble = Double.valueOf(proteinGoal.getText().toString()) - Double.valueOf(curProteinGoal.getText().toString());
                            proteinPoz.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(String.valueOf(proteinPozDouble))).setScale(2, RoundingMode.HALF_UP).doubleValue()));

                            break;
                        case "carbs":
                            curCarbsGoal.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(String.valueOf(entry.getValue()))).setScale(2, RoundingMode.HALF_UP).doubleValue()));
                            double pomCar = (Double.valueOf(entry.getValue()) / Double.valueOf(carbsGoal.getText().toString())) * 100;
                            carbsBar.setProgress((float) pomCar);
                            double carbsPozDouble = Double.valueOf(carbsGoal.getText().toString()) - Double.valueOf(curCarbsGoal.getText().toString());
                            carbsPoz.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(String.valueOf(carbsPozDouble))).setScale(2, RoundingMode.HALF_UP).doubleValue()));
                            break;
                        case "fat":
                            curFatGoal.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(String.valueOf(entry.getValue()))).setScale(2, RoundingMode.HALF_UP).doubleValue()));
                            double pomFa = (Double.valueOf(entry.getValue()) / Double.valueOf(fatGoal.getText().toString())) * 100;
                            fatBar.setProgress((float) pomFa);
                            double fatPozDouble = Double.valueOf(fatGoal.getText().toString()) - Double.valueOf(curFatGoal.getText().toString());
                            fatPoz.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(String.valueOf(fatPozDouble))).setScale(2, RoundingMode.HALF_UP).doubleValue()));
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

        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curKcal.setText(String.valueOf(dataSnapshot.getValue()));
                double curPom;
                if ((String.valueOf(dataSnapshot.getValue()).equals(null))) {
                    curPom = 0.0;
                } else {
                    curPom = Double.valueOf(String.valueOf(dataSnapshot.getValue()));
                }
                if (curPom == 0.0) {
                    circularProgressBar.setProgress(0);
                } else {
                    circularProgressBar.setProgress((float) ((float) curPom / Double.valueOf(allKcal.getText().toString())) * 100);
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
            Toast.makeText(UserProfile.this, "settings", Toast.LENGTH_SHORT).show();
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
        SecondaryDrawerItem dodajDoDziennejListy = new SecondaryDrawerItem().withIdentifier(5).withName("Dodaj produkt do dziennej listy");
        SecondaryDrawerItem dodajDoDziennejListyAktywnosc = new SecondaryDrawerItem().withIdentifier(6).withName("Dodaj aktywność do dziennej listy");


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

                .addDrawerItems(menu, profil, edytujProfil, dodajDoBazy, dodajDoDziennejListy,dodajDoDziennejListyAktywnosc)
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
                                intent = new Intent(UserProfile.this, AddDailyProducts.class);
                                startActivity(intent);
                                break;
                            case 6:
                                intent = new Intent(UserProfile.this, AddDailyActivity.class);
                                startActivity(intent);
                            default:
                                break;
                        }
                        return true;
                    }
                }).build();


    }
}
