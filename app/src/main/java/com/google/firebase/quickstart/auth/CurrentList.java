package com.google.firebase.quickstart.auth;

import android.app.Activity;
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
import android.widget.Toast;

import com.asksira.dropdownview.DropDownView;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentList extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef = database.getReference();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DropDownView dropDownView;
    DropDownView dropDownViewSniadanie;
    DropDownView dropDownViewObiad;
    DropDownView dropDownViewKolacja;
    DropDownView dropDownViewPrzekaski;

    Toolbar myToolbar;
    public String dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_list);
        createDrawer();
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);
        String curId = mAuth.getUid();
        getAvtivities();
getProductsSniadanie();
getProductsObiad();
getProductsKolacja();
getProductsPrzekaski();
        dropDownView = findViewById(R.id.dropdownview);
        dropDownViewSniadanie = findViewById(R.id.dropdownviewSniadanie);
        dropDownViewObiad = findViewById(R.id.dropdownviewObiad);
        dropDownViewKolacja = findViewById(R.id.dropdownviewKolacja);
        dropDownViewPrzekaski = findViewById(R.id.dropdownviewPrzekaski);



    }


    public void getAvtivities() {

        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("aktywnosc").addValueEventListener(new ValueEventListener() {
            Map<String, UserActivities> map = new HashMap<>();
            List<String> list = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        map.put(noteDataSnapshot.getKey(), new UserActivities(noteDataSnapshot.getValue(UserActivities.class).getKcal(),
                                noteDataSnapshot.getValue(UserActivities.class).getTime()));
                    }


                }

                for (Map.Entry<String, UserActivities> entry : map.entrySet()) {
                    list.add(entry.getKey() + " | kcal = " + entry.getValue().getKcal() + " czas = " + entry.getValue().getTime());
                }

                dropDownView.setDropDownListItem(list);
                dropDownView.setOnSelectionListener(new DropDownView.OnSelectionListener() {
                    @Override
                    public void onItemSelected(DropDownView view, int position) {

                        String pom = list.get(position);


                        Intent intent = new Intent(CurrentList.this, EditActivityFromToday.class);
                        intent.putExtra("name", pom.substring(0, pom.lastIndexOf(" |")));
                        startActivity(intent);


                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getProductsSniadanie() {

        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("sniadanie").addValueEventListener(new ValueEventListener() {
            Map<String, Produkt> map = new HashMap<>();
            List<String> list = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        map.put(noteDataSnapshot.getKey(), new Produkt(noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                noteDataSnapshot.getValue(Produkt.class).getFat(),
                                noteDataSnapshot.getValue(Produkt.class).getKcal()));
                    }


                }

                for (Map.Entry<String, Produkt> entry : map.entrySet()) {
                    list.add(entry.getKey() + " | ilość = " + entry.getValue().getAmount() + " białko = " + entry.getValue().getProtein() +
                    " węglowodany " + entry.getValue().getCarbs() + " tłuszcz = " + entry.getValue().getFat() + " kcal = " + entry.getValue().getKcal());

                }

                dropDownViewSniadanie.setDropDownListItem(list);
                dropDownViewSniadanie.setOnSelectionListener(new DropDownView.OnSelectionListener() {
                    @Override
                    public void onItemSelected(DropDownView view, int position) {

                        String pom = list.get(position);


                        Intent intent = new Intent(CurrentList.this, EditProductFromToday.class);
                        intent.putExtra("name", pom.substring(0, pom.lastIndexOf(" |")));
                        startActivity(intent);


                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getProductsObiad() {

        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("obiad").addValueEventListener(new ValueEventListener() {
            Map<String, Produkt> map = new HashMap<>();
            List<String> list = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        map.put(noteDataSnapshot.getKey(), new Produkt(noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                noteDataSnapshot.getValue(Produkt.class).getFat(),
                                noteDataSnapshot.getValue(Produkt.class).getKcal()));
                    }


                }

                for (Map.Entry<String, Produkt> entry : map.entrySet()) {
                    list.add(entry.getKey() + " | ilość = " + entry.getValue().getAmount() + " białko = " + entry.getValue().getProtein() +
                            " węglowodany " + entry.getValue().getCarbs() + " tłuszcz = " + entry.getValue().getFat() + " kcal = " + entry.getValue().getKcal());

                }

                dropDownViewObiad.setDropDownListItem(list);
                dropDownViewObiad.setOnSelectionListener(new DropDownView.OnSelectionListener() {
                    @Override
                    public void onItemSelected(DropDownView view, int position) {

                        String pom = list.get(position);


                        Intent intent = new Intent(CurrentList.this, EditProductFromToday.class);
                        intent.putExtra("name", pom.substring(0, pom.lastIndexOf(" |")));
                        startActivity(intent);


                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getProductsKolacja() {

        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kolacja").addValueEventListener(new ValueEventListener() {
            Map<String, Produkt> map = new HashMap<>();
            List<String> list = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        map.put(noteDataSnapshot.getKey(), new Produkt(noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                noteDataSnapshot.getValue(Produkt.class).getFat(),
                                noteDataSnapshot.getValue(Produkt.class).getKcal()));
                    }


                }

                for (Map.Entry<String, Produkt> entry : map.entrySet()) {
                    list.add(entry.getKey() + " | ilość = " + entry.getValue().getAmount() + " białko = " + entry.getValue().getProtein() +
                            " węglowodany " + entry.getValue().getCarbs() + " tłuszcz = " + entry.getValue().getFat() + " kcal = " + entry.getValue().getKcal());

                }

                dropDownViewKolacja.setDropDownListItem(list);
                dropDownViewKolacja.setOnSelectionListener(new DropDownView.OnSelectionListener() {
                    @Override
                    public void onItemSelected(DropDownView view, int position) {

                        String pom = list.get(position);


                        Intent intent = new Intent(CurrentList.this, EditProductFromToday.class);
                        intent.putExtra("name", pom.substring(0, pom.lastIndexOf(" |")));
                        startActivity(intent);


                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getProductsPrzekaski()  {

        myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("przekaski").addValueEventListener(new ValueEventListener() {
            Map<String, Produkt> map = new HashMap<>();
            List<String> list = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        map.put(noteDataSnapshot.getKey(), new Produkt(noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                noteDataSnapshot.getValue(Produkt.class).getFat(),
                                noteDataSnapshot.getValue(Produkt.class).getKcal()));
                    }


                }

                for (Map.Entry<String, Produkt> entry : map.entrySet()) {
                    list.add(entry.getKey() + " | ilość = " + entry.getValue().getAmount() + " białko = " + entry.getValue().getProtein() +
                            " węglowodany " + entry.getValue().getCarbs() + " tłuszcz = " + entry.getValue().getFat() + " kcal = " + entry.getValue().getKcal());

                }

                dropDownViewPrzekaski.setDropDownListItem(list);
                dropDownViewPrzekaski.setOnSelectionListener(new DropDownView.OnSelectionListener() {
                    @Override
                    public void onItemSelected(DropDownView view, int position) {

                        String pom = list.get(position);


                        Intent intent = new Intent(CurrentList.this, EditProductFromToday.class);
                        intent.putExtra("name", pom.substring(0, pom.lastIndexOf(" |")));
                        startActivity(intent);


                    }
                });
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
            Intent intent = new Intent(CurrentList.this, Settings.class);
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
                    Intent intent = new Intent(CurrentList.this, EmailPasswordActivity.class);
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
                                intent = new Intent(CurrentList.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(CurrentList.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:

                                intent = new Intent(CurrentList.this, CurrentList.class);
                                startActivity(intent);
                                break;
                            case 5:

                                intent = new Intent(CurrentList.this, GraphActivity.class);
                                startActivity(intent);
                                break;
                            case 6:

                                intent = new Intent(CurrentList.this, SelectDate.class);
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
