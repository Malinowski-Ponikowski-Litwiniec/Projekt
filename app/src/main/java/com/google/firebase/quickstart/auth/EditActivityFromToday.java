package com.google.firebase.quickstart.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditActivityFromToday extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public TextView activityName;
    public String dateFormat;
    public String name;
    public EditText time;
    public TextView kcal;

    Toolbar myToolbar;
    public Button sendBtn;

    public DatabaseReference kcalRef;
    public DatabaseReference timeRef;
    public Map<String, String> map = new HashMap<>();
    public Double resultKcal;

    public DatabaseReference activitiesRef;


    public Double burnedKcal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_from_today);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);

        //Zmiana koloru paska nawigacji
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.md_black_1000));

        time = (EditText) findViewById(R.id.time);
        kcal = (TextView) findViewById(R.id.kcal);
        activityName = (TextView) findViewById(R.id.activityName);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        activityName.setText(name);
        kcal.setText("0");

        sendBtn = (Button) findViewById(R.id.dodajBtn);
        sendBtn.setOnClickListener(sendBtnListener);

        activitiesRef = myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("aktywnosc");
    }

    View.OnClickListener sendBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myRef.child("aktywnosc").child(name).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (time.getText().toString().isEmpty()) {
                        Toast.makeText(EditActivityFromToday.this, "Musisz ustawić czas ", Toast.LENGTH_SHORT).show();

                    } else {
                        Double userTime = Double.valueOf(String.valueOf(time.getText()));



                        if (userTime <= 60) {
                            double comma = (Double.valueOf(String.valueOf(time.getText())) / 60) * Double.valueOf(String.valueOf(dataSnapshot.getValue()));
                            int result = (int) (comma);
                            kcal.setText(String.valueOf(result));
                            UserActivities userActivities = new UserActivities(Double.valueOf(result),userTime);
                            activitiesRef = myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("aktywnosc").child(name);

                            activitiesRef.setValue(userActivities);

                            setBurnKcal();
                            setCurrentKcal();

                            Toast.makeText(EditActivityFromToday.this, "Dodano aktywność", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditActivityFromToday.this, UserProfile.class);
                            startActivity(intent);
                        } else {
                            double whole = Double.valueOf(Integer.valueOf(String.valueOf(time.getText())) / 60);

                            double comma = Double.valueOf(String.valueOf(time.getText())) - (60 * whole);
                            if (comma <= 0) {

                            } else {
                                comma = (comma / 60) * Double.valueOf(String.valueOf(dataSnapshot.getValue()));
                            }

                            whole = whole * Double.valueOf(String.valueOf(dataSnapshot.getValue()));

                            int result = (int) (comma + whole);
                            UserActivities userActivities = new UserActivities(Double.valueOf(result), userTime);
                            activitiesRef = myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("aktywnosc").child(name);
                            activitiesRef.setValue(userActivities);

                            Toast.makeText(EditActivityFromToday.this, "Dodano aktywność", Toast.LENGTH_SHORT).show();
                            kcal.setText(String.valueOf(result));
                            setBurnKcal();
                            setCurrentKcal();
                            Intent intent = new Intent(EditActivityFromToday.this, UserProfile.class);
                            startActivity(intent);

                        }
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    };
    public void setBurnKcal(){
        burnedKcal =0.0;
        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("aktywnosc").addValueEventListener(new ValueEventListener() {
            Map<String,UserActivities> map = new HashMap<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(),new UserActivities(noteDataSnapshot.getValue(UserActivities.class).getKcal(),
                            noteDataSnapshot.getValue(UserActivities.class).getTime()));
                }
                for(Map.Entry<String, UserActivities> entry : map.entrySet()){

                    burnedKcal+= entry.getValue().getKcal();

                }
                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("burnedAndKcal").child("burnedKcal").setValue(burnedKcal);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setCurrentKcal(){
        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("burnedAndKcal").addValueEventListener(new ValueEventListener() {
            Map<String,Double> map = new HashMap<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){
                    map.put(noteDataSnapshot.getKey(),Double.valueOf(String.valueOf(noteDataSnapshot.getValue())));
                }
                double currentKcal = map.get("kcal") - map.get("burnedKcal");
                myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kcal").setValue(BigDecimal.valueOf(Double.valueOf(currentKcal)).setScale(1, RoundingMode.HALF_UP).doubleValue());
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
            Toast.makeText(EditActivityFromToday.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(EditActivityFromToday.this, EmailPasswordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                                intent = new Intent(EditActivityFromToday.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(EditActivityFromToday.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:

                                intent = new Intent(EditActivityFromToday.this, CurrentList.class);
                                startActivity(intent);
                                break;
                            case 5:

                                intent = new Intent(EditActivityFromToday.this, GraphActivity.class);
                                startActivity(intent);
                                break;
                            case 6:

                                intent = new Intent(EditActivityFromToday.this, SelectDate.class);
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
