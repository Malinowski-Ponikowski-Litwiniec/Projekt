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
import android.view.Window;
import android.view.WindowManager;
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);

        //Zmiana koloru paska nawigacji
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.md_black_1000));

        // Zmiana koloru status bara
        Window window = EmailPasswordActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(EmailPasswordActivity.this.getResources().getColor(R.color.grey_dark));

        time = (EditText) findViewById(R.id.time);
        kcal = (EditText) findViewById(R.id.kcal);
        name = (EditText) findViewById(R.id.name);
        dodaj = (Button) findViewById(R.id.dodajBtn);
        dodaj.setOnClickListener(dodajOnClick);
        databaseReference = myRef.child("aktywnosc");
    }







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
                                intent = new Intent(AddActivityToDatabase.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(AddActivityToDatabase.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:

                                intent = new Intent(AddActivityToDatabase.this, CurrentList.class);
                                startActivity(intent);
                                break;
                            case 5:

                                intent = new Intent(AddActivityToDatabase.this, GraphActivity.class);
                                startActivity(intent);
                                break;
                            case 6:

                                intent = new Intent(AddActivityToDatabase.this, SelectDate.class);
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
