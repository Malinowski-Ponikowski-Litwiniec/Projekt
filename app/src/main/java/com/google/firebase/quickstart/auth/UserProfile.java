package com.google.firebase.quickstart.auth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.firebase.ui.auth.data.model.User;
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
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public CircularProgressBar circularProgressBar;

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
        addProductToList = (FloatingActionButton)findViewById(R.id.addProductToList);

        addActivityToDatabase.setColorNormal(Color.parseColor("#039BE5"));
        addActivityToDatabase.setColorPressed(Color.parseColor("#0288D1"));

        addActivityToList.setColorNormal(Color.parseColor("#039BE5"));
        addActivityToList.setColorPressed(Color.parseColor("#0288D1"));

        addProductToDatabase.setColorNormal(Color.parseColor("#039BE5"));
        addProductToDatabase.setColorPressed(Color.parseColor("#0288D1"));

        addProductToList.setColorNormal(Color.parseColor("#039BE5"));
        addProductToList.setColorPressed(Color.parseColor("#0288D1"));


        addProductToDatabase.setOnClickListener(addProductToDatabaseOnClick);
        addProductToList.setOnClickListener(addProductToListOnClick);
        addActivityToDatabase.setOnClickListener(addActivityToDatabaseOnClick);
        addActivityToList.setOnClickListener(addActivityToListOnClick);

        //Zmiana koloru paska nawigacji
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.md_black_1000));

        Window window = UserProfile.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(UserProfile.this.getResources().getColor(R.color.md_black_1000));


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

        proteinBar.setProgressColor(ContextCompat.getColor(this, R.color.colorPrimary));
        proteinBar.setProgressBackgroundColor(ContextCompat.getColor(this, R.color.grey_dark));
        proteinBar.setMax(100);


        carbsBar.setProgressColor (ContextCompat.getColor(this, R.color.colorPrimary));
        carbsBar.setProgressBackgroundColor(ContextCompat.getColor(this, R.color.grey_dark));
        carbsBar.setMax(100);


        fatBar.setProgressColor(ContextCompat.getColor(this, R.color.colorPrimary));
        fatBar.setProgressBackgroundColor(ContextCompat.getColor(this, R.color.grey_dark));
        fatBar.setMax(100);

        circularProgressBar = (CircularProgressBar)findViewById(R.id.bar);
        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_dark));
        setGoalMacro();
        setCurrentMacro();
        setCurKcal();

        setBars();
        checkOverstep();
    }


    public void checkOverstep(){
        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("curMacro").addValueEventListener(new ValueEventListener() {
            Map<String,String> map = new HashMap<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(),String.valueOf(noteDataSnapshot.getValue()));
                }
                for(Map.Entry<String, String> entry : map.entrySet()){
                    System.out.println(entry.getKey() + " ++++++ " + entry.getValue());
                }
                myRef.child("users").child(mAuth.getUid()).child("macro").addValueEventListener(new ValueEventListener() {
                    Map<String,String> mapMacro = new HashMap<>();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            mapMacro.put(noteDataSnapshot.getKey(),String.valueOf(noteDataSnapshot.getValue()));
                        }
                        for(Map.Entry<String, String> entry : mapMacro.entrySet()){
                            System.out.println(entry.getKey() + " -------- " + entry.getValue());
                        }
//                        proteinBar.setProgress(100/(Float.valueOf(String.valueOf(mapMacro.get("protein"))) / Float.valueOf(String.valueOf(map.get("protein")))));
//                        carbsBar.setProgress(100/(Float.valueOf(String.valueOf(mapMacro.get("carbs"))) / Float.valueOf(String.valueOf(map.get("carbs")))));
//                        fatBar.setProgress(100/(Float.valueOf(String.valueOf(mapMacro.get("fat"))) / Float.valueOf(String.valueOf(map.get("fat")))));
//                        int progress = (int) (100/(Float.valueOf(String.valueOf(mapMacro.get("fat")))/ Float.valueOf(String.valueOf(map.get("fat")))));
//                        circularProgressBar.setProgressWithAnimation(progress);
                        if(Double.valueOf(map.get("protein")) > Double.valueOf(mapMacro.get("protein"))){
//                            Intent myIntent = new Intent(UserProfile.this,UserProfile.class);
//                            PendingIntent intent = PendingIntent.getActivity(UserProfile.this,0,myIntent,Intent.FLAG_ACTIVITY_NEW_TASK);

                            Notification proteinNotification = new Notification.Builder(UserProfile.this)
                                    .setSmallIcon(R.drawable.andrzej)
                                    .setContentTitle("Przekroczono limit na białko")
                                    //.setContentIntent(intent)
                                    //.setContentText("Przekroczyłeś dzienne zapotrzebowanie na białko o " +String.valueOf(Double.valueOf(String.valueOf(mapMacro.get("protein"))) -
                                     //       Double.valueOf(String.valueOf(map.get("protein")))))

                                    .build();


                            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                            notificationManager.notify(0,proteinNotification);

                        }if(Double.valueOf(map.get("carbs")) > Double.valueOf(mapMacro.get("carbs"))){
                            Notification carbsnotification = new Notification.Builder(UserProfile.this)
                                    .setSmallIcon(R.drawable.andrzej)
                                    .setContentTitle("Przekroczono limit na węglowodany")
                                    //.setContentIntent(intent)
                                    //.setContentText("Przekroczyłeś dzienne zapotrzebowanie na białko o " +String.valueOf(Double.valueOf(String.valueOf(mapMacro.get("protein"))) -
                                    //       Double.valueOf(String.valueOf(map.get("protein")))))

                                    .build();


                            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                            notificationManager.notify(1,carbsnotification);
                        }if(Double.valueOf(map.get("fat")) > Double.valueOf(mapMacro.get("fat"))){
                            Notification fatNotification = new Notification.Builder(UserProfile.this)
                                    .setSmallIcon(R.drawable.andrzej)
                                    .setContentTitle("Przekroczono limit na tłuszcz")
                                    //.setContentIntent(intent)
                                    //.setContentText("Przekroczyłeś dzienne zapotrzebowanie na białko o " +String.valueOf(Double.valueOf(String.valueOf(mapMacro.get("protein"))) -
                                    //       Double.valueOf(String.valueOf(map.get("protein")))))

                                    .build();


                            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                            notificationManager.notify(2,fatNotification);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void setBars(){
        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("curMacro").addValueEventListener(new ValueEventListener() {
            Map<String,String> map = new HashMap<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(),String.valueOf(noteDataSnapshot.getValue()));
                }
                for(Map.Entry<String, String> entry : map.entrySet()){
                    System.out.println(entry.getKey() + " ++++++ " + entry.getValue());
                }
                myRef.child("users").child(mAuth.getUid()).child("macro").addValueEventListener(new ValueEventListener() {
                    Map<String,String> mapMacro = new HashMap<>();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            mapMacro.put(noteDataSnapshot.getKey(),String.valueOf(noteDataSnapshot.getValue()));
                        }
                        for(Map.Entry<String, String> entry : mapMacro.entrySet()){
                            System.out.println(entry.getKey() + " -------- " + entry.getValue());
                        }
                        proteinBar.setProgress(100/(Float.valueOf(String.valueOf(mapMacro.get("protein"))) / Float.valueOf(String.valueOf(map.get("protein")))));
                        carbsBar.setProgress(100/(Float.valueOf(String.valueOf(mapMacro.get("carbs"))) / Float.valueOf(String.valueOf(map.get("carbs")))));
                        fatBar.setProgress(100/(Float.valueOf(String.valueOf(mapMacro.get("fat"))) / Float.valueOf(String.valueOf(map.get("fat")))));
                        int progress = (int) (100/(Float.valueOf(String.valueOf(mapMacro.get("fat")))/ Float.valueOf(String.valueOf(map.get("fat")))));
                        circularProgressBar.setProgressWithAnimation(progress);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                                                    if(pom < 0) {
                                                        carbsPoz.setTextColor(Color.parseColor("#ff0000"));
                                                    }
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
                                                    if(pom < 0) {
                                                        fatPoz.setTextColor(Color.parseColor("#ff0000"));
                                                    }
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
                                                    if(pom < 0) {
                                                        proteinPoz.setTextColor(Color.parseColor("#ff0000"));
                                                    }
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
    }




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
                                if(pom > Double.parseDouble(allKcal.getText().toString())) {
                                    curKcal.setTextColor(Color.parseColor("#ff0000"));
                                }
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
        SecondaryDrawerItem currnetList = new SecondaryDrawerItem().withIdentifier(4).withName("Lista z dzisiejszego dnia");
        SecondaryDrawerItem graph = new SecondaryDrawerItem().withIdentifier(4).withName("Graph");
        SecondaryDrawerItem selectDate = new SecondaryDrawerItem().withIdentifier(4).withName("Wybierz date");



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

                .addDrawerItems(menu, profil, edytujProfil, currnetList,graph,selectDate
                )
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

                                intent = new Intent(UserProfile.this, CurrentList.class);
                                startActivity(intent);
                                break;
                            case 5:

                                intent = new Intent(UserProfile.this, GraphActivity.class);
                                startActivity(intent);
                                break;
                                case 6:

                                intent = new Intent(UserProfile.this, SelectDate.class);
                                startActivity(intent);
                                break;
                           default:

                                break;
                        }
                        return true;
                    }
                }).build();


    }
}