package com.google.firebase.quickstart.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditAddedActivityForm extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar myToolbar;
    public Button sendBtn;
    public String dateFormat;

    public TextView activityName;
    public EditText time;
    public TextView kcal;
    public String name;
    public DatabaseReference activitiesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_added_form);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        time = (EditText) findViewById(R.id.time);
        kcal = (TextView) findViewById(R.id.kcal);
        activityName = (TextView) findViewById(R.id.activityName);

        activityName.setText(name);
        setKcal();
        sendBtn = (Button) findViewById(R.id.dodajBtn);
        sendBtn.setOnClickListener(sendBtnOnClick);
    }

    View.OnClickListener sendBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("aktywnosc").child(name).child("time").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (time.getText().toString().isEmpty()) {
                        Toast.makeText(EditAddedActivityForm.this, "Musisz ustawić czas ", Toast.LENGTH_SHORT).show();

                    } else {
                        Double userTime = Double.valueOf(String.valueOf(time.getText()));
                        if (userTime <= 60) {

                            double comma = (Double.valueOf(String.valueOf(time.getText())) / 60) * Double.valueOf(String.valueOf(dataSnapshot.getValue()));
                            int result = (int) (comma);
                            kcal.setText(String.valueOf(result));
                            UserActivities userActivities = new UserActivities(Double.valueOf(result), userTime);
                            activitiesRef = myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("aktywnosc").child(name);

                            activitiesRef.setValue(userActivities);
                            Toast.makeText(EditAddedActivityForm.this, "wyslano", Toast.LENGTH_SHORT).show();
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

                            kcal.setText(String.valueOf(result));
                            Intent intent = new Intent(EditAddedActivityForm.this, UserProfile.class);
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

    public void setKcal() {
        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("aktywnosc").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map = new HashMap<>();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String name = noteDataSnapshot.getKey();

                        map.put(noteDataSnapshot.getKey(),String.valueOf(noteDataSnapshot.getValue()));

                }
                kcal.setText(map.get("kcal"));
                time.setText(map.get("time"));

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
            Toast.makeText(EditAddedActivityForm.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(EditAddedActivityForm.this, EmailPasswordActivity.class);
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

                .addDrawerItems(menu, profil, edytujProfil, dodajDoBazy,dodajAktywnoscDoBazy, dodajDoDziennejListy,dodajDoDziennejListyAktywnosc,edytujAktywnosc)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        System.out.println("position +++++++++++++++++++++ " + position);

                        Intent intent;
                        switch (position) {
                            case 1:
                                break;
                            case 2:
                                intent = new Intent(EditAddedActivityForm.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(EditAddedActivityForm.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(EditAddedActivityForm.this, AddProductToDatabase.class);
                                startActivity(intent);
                                break;

                            case 5:
                                intent = new Intent(EditAddedActivityForm.this, AddActivityToDatabase.class);
                                startActivity(intent);
                                break;

                            case 6:
                                intent = new Intent(EditAddedActivityForm.this, AddDailyProducts.class);
                                startActivity(intent);
                                break;
                            case 7:
                                intent = new Intent(EditAddedActivityForm.this, AddDailyActivity.class);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(EditAddedActivityForm.this, EditAddedActivity.class);
                                startActivity(intent);
                            default:
                                break;
                        }
                        return true;
                    }
                }).build();


    }
}
