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
import android.widget.ListView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddActivityToDatabase extends AppCompatActivity {
    public ListView listView;
    Toolbar myToolbar;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public String dateFormat;

    public EditText time;
    public EditText kcal;
    public EditText name;
    public Button dodaj;
    public DatabaseReference databaseReference;

    public FloatingActionButton addProductToDatabase;
    public FloatingActionButton addProductToList;

    public FloatingActionButton addActivityToList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity_to_database);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
        Date date = new Date();



        addActivityToList = (FloatingActionButton)findViewById(R.id.addActivityToList);
        addProductToDatabase = (FloatingActionButton)findViewById(R.id.addProductToDatabase);
        addProductToList= (FloatingActionButton)findViewById(R.id.addProductToList);


        addProductToDatabase.setOnClickListener(addProductToDatabaseOnClick);
        addProductToList.setOnClickListener(addProductToListOnClick);

        addActivityToList.setOnClickListener(addActivityToListOnClick);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);

        time = (EditText) findViewById(R.id.time);
        kcal = (EditText) findViewById(R.id.kcal);
        name = (EditText) findViewById(R.id.name);
        dodaj = (Button) findViewById(R.id.dodajBtn);
        dodaj.setOnClickListener(dodajOnClick);
        databaseReference = myRef.child("aktywnosc");
    }





    View.OnClickListener addProductToDatabaseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddActivityToDatabase.this,AddProductToDatabase.class);
            startActivity(intent);
        }
    };
    View.OnClickListener addProductToListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddActivityToDatabase.this,AddDailyProducts.class);
            startActivity(intent);
        }
    };

    View.OnClickListener addActivityToListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddActivityToDatabase.this,AddDailyActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener dodajOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setNewActivity();
        }
    };

    public void setNewActivity() {


//    myRef.child("aktywnosc").addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
//            ArrayList<String> list = new ArrayList<>();
//            ArrayAdapter<String> arrayAdapter;
//            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                String name = noteDataSnapshot.getKey();
//
//                list.add(name);
//
//            }
//
//            if (list.contains(name.getText().toString())) {
//                Toast.makeText(AddActivityToDatabase.this, "Ćwiczenie o takiej nazwie już istnieje", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        myRef.child("aktywnosc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> list = new ArrayList<>();
                ArrayAdapter<String> arrayAdapter;
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String name = noteDataSnapshot.getKey();

                    list.add(name);

                }


//                if (list.contains(name.getText().toString())) {
//                    Toast.makeText(AddActivityToDatabase.this, "Ćwiczenie o takiej nazwie już istnieje", Toast.LENGTH_SHORT).show();
//                } else {
                    if (kcal.getText().toString().isEmpty()) {
                        Toast.makeText(AddActivityToDatabase.this, "Ilość kcal nie może być pusta", Toast.LENGTH_SHORT).show();
                    } else {

                        if (Double.valueOf(time.getText().toString()) <= 60) {
                            int pomTime = Integer.valueOf(time.getText().toString()) / 60;
                            double pomComma = Double.valueOf(time.getText().toString()) - (Double.valueOf(pomTime) * 60);
                            int result = Integer.valueOf((60 * Integer.valueOf(kcal.getText().toString()))) / Integer.valueOf(time.getText().toString());

                            databaseReference.child(name.getText().toString()).setValue(result);

                            Toast.makeText(AddActivityToDatabase.this, "Dodano aktywność do bazy", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddActivityToDatabase.this,UserProfile.class);
                            startActivity(intent);

                        } else {
                            int pomTime = Integer.valueOf(time.getText().toString()) / 60;
                            double result = Double.valueOf(Integer.valueOf(kcal.getText().toString()) / pomTime);
                            databaseReference.child(name.getText().toString()).setValue(result);
                            Toast.makeText(AddActivityToDatabase.this, "Dodano aktywność do bazy", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AddActivityToDatabase.this,UserProfile.class);
                            startActivity(intent);
                        }


                    }

                }


//            }


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
            Toast.makeText(AddActivityToDatabase.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(AddActivityToDatabase.this, EmailPasswordActivity.class);
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
                                intent = new Intent(AddActivityToDatabase.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(AddActivityToDatabase.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(AddActivityToDatabase.this, AddProductToDatabase.class);
                                startActivity(intent);
                                break;

                            case 5:
                                intent = new Intent(AddActivityToDatabase.this, AddActivityToDatabase.class);
                                startActivity(intent);
                                break;

                            case 6:
                                intent = new Intent(AddActivityToDatabase.this, AddDailyProducts.class);
                                startActivity(intent);
                                break;
                            case 7:
                                intent = new Intent(AddActivityToDatabase.this, AddDailyActivity.class);
                                startActivity(intent);
                                break;
                            case 8:
                                intent = new Intent(AddActivityToDatabase.this, EditAddedActivity.class);
                                startActivity(intent);
                            default:
                                break;
                        }
                        return true;
                    }
                }).build();


    }

}
