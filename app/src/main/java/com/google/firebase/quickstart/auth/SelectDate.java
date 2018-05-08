package com.google.firebase.quickstart.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectDate extends AppCompatActivity {
    public ListView listView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        listView = (ListView) findViewById(R.id.listView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        myRef.child("lista").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    list.add(noteDataSnapshot.getKey());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        ArrayList arrayList = new ArrayList();
                        Map<String,UserActivities> mapActivity = new HashMap<>();

                        myRef.child("lista").child(mAuth.getUid()).child(list.get(position)).child("aktywnosc").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                                            mapActivity.put(noteDataSnapshot.getKey(), new UserActivities(noteDataSnapshot.getValue(UserActivities.class).getKcal(),
                                                    noteDataSnapshot.getValue(UserActivities.class).getTime()));


                                    }
//                                    arrayList.add(dataSnapshot.getValue());
//                                    ArrayAdapter<String> secondArrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, arrayList);
//                                    listView.setAdapter(secondArrayAdapter);
                                } else {
                                    //Toast.makeText(SelectDate.this, "Brak danych", Toast.LENGTH_SHORT).show();
                                }

                                if(!mapActivity.isEmpty()){
                                    for (Map.Entry<String, UserActivities> entry : mapActivity.entrySet()) {
                                        System.out.println("++++++++++++++++++ " +entry.getKey() + " ------ " + entry.getValue().getTime() + entry.getValue().getKcal() );
                                        arrayList.add(entry.getKey() + " czas = " + entry.getValue().getTime() +" kcal = " +  entry.getValue().getKcal()  );
                                    }
                                }
                                Map<String,Produkt> mapProduct = new HashMap<>();
                                myRef.child("lista").child(mAuth.getUid()).child(list.get(position)).child("sniadanie").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {


                                                mapProduct.put(noteDataSnapshot.getKey(), new Produkt(
                                                        noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                                        noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                                        noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                                        noteDataSnapshot.getValue(Produkt.class).getFat(),
                                                        noteDataSnapshot.getValue(Produkt.class).getKcal()));

                                            }
//                                    arrayList.add(dataSnapshot.getValue());
//                                    ArrayAdapter<String> secondArrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, arrayList);
//                                    listView.setAdapter(secondArrayAdapter);
                                        } else {
                                            //Toast.makeText(SelectDate.this, "Brak danych", Toast.LENGTH_SHORT).show();
                                        }



                                        if(!mapProduct.isEmpty()){
                                            for (Map.Entry<String, Produkt> entry : mapProduct.entrySet()) {
                                                System.out.println("++++++++++++++++++ " +entry.getKey() + " ------ " + entry.getValue().getAmount() + entry.getValue().getProtein() +
                                                        entry.getValue().getCarbs() + entry.getValue().getFat() + entry.getValue().getKcal()) ;
                                                arrayList.add("sniadanie | " + entry.getKey() + " ilosc = " + entry.getValue().getAmount() + " białko = " +  entry.getValue().getProtein() +
                                                        " węglowodany =" + entry.getValue().getCarbs() + " tłuszcz = " +entry.getValue().getFat() +" kcal = "+ entry.getValue().getKcal() );
                                            }
                                        }

                                        Map<String,Produkt> mapProduct = new HashMap<>();
                                        myRef.child("lista").child(mAuth.getUid()).child(list.get(position)).child("obiad").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {


                                                        mapProduct.put(noteDataSnapshot.getKey(), new Produkt(
                                                                noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                                                noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                                                noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                                                noteDataSnapshot.getValue(Produkt.class).getFat(),
                                                                noteDataSnapshot.getValue(Produkt.class).getKcal()));

                                                    }
//                                    arrayList.add(dataSnapshot.getValue());
//                                    ArrayAdapter<String> secondArrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, arrayList);
//                                    listView.setAdapter(secondArrayAdapter);
                                                } else {
                                                    //Toast.makeText(SelectDate.this, "Brak danych", Toast.LENGTH_SHORT).show();
                                                }



                                                if(!mapProduct.isEmpty()){
                                                    for (Map.Entry<String, Produkt> entry : mapProduct.entrySet()) {
                                                        System.out.println("++++++++++++++++++ " +entry.getKey() + " ------ " + entry.getValue().getAmount() + entry.getValue().getProtein() +
                                                                entry.getValue().getCarbs() + entry.getValue().getFat() + entry.getValue().getKcal()) ;
                                                        arrayList.add("obiad | " + entry.getKey() + " ilosc = " + entry.getValue().getAmount() + " białko = " +  entry.getValue().getProtein() +
                                                                " węglowodany =" + entry.getValue().getCarbs() + " tłuszcz = " +entry.getValue().getFat() +" kcal = "+ entry.getValue().getKcal() );
                                                    }
                                                }
                                                Map<String,Produkt> mapProduct = new HashMap<>();
                                                myRef.child("lista").child(mAuth.getUid()).child(list.get(position)).child("kolacja").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if (dataSnapshot.exists()) {
                                                            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {


                                                                mapProduct.put(noteDataSnapshot.getKey(), new Produkt(
                                                                        noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                                                        noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                                                        noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                                                        noteDataSnapshot.getValue(Produkt.class).getFat(),
                                                                        noteDataSnapshot.getValue(Produkt.class).getKcal()));

                                                            }
//                                    arrayList.add(dataSnapshot.getValue());
//                                    ArrayAdapter<String> secondArrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, arrayList);
//                                    listView.setAdapter(secondArrayAdapter);
                                                        } else {
                                                            //Toast.makeText(SelectDate.this, "Brak danych", Toast.LENGTH_SHORT).show();
                                                        }



                                                        if(!mapProduct.isEmpty()){
                                                            for (Map.Entry<String, Produkt> entry : mapProduct.entrySet()) {
                                                                System.out.println("++++++++++++++++++ " +entry.getKey() + " ------ " + entry.getValue().getAmount() + entry.getValue().getProtein() +
                                                                        entry.getValue().getCarbs() + entry.getValue().getFat() + entry.getValue().getKcal()) ;
                                                                arrayList.add("kolacja | " + entry.getKey() + " ilosc = " + entry.getValue().getAmount() + " białko = " +  entry.getValue().getProtein() +
                                                                        " węglowodany =" + entry.getValue().getCarbs() + " tłuszcz = " +entry.getValue().getFat() +" kcal = "+ entry.getValue().getKcal() );
                                                            }
                                                        }
                                                        Map<String,Produkt> mapProduct = new HashMap<>();
                                                        myRef.child("lista").child(mAuth.getUid()).child(list.get(position)).child("przekaski").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                if (dataSnapshot.exists()) {
                                                                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {


                                                                        mapProduct.put(noteDataSnapshot.getKey(), new Produkt(
                                                                                noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                                                                noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                                                                noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                                                                noteDataSnapshot.getValue(Produkt.class).getFat(),
                                                                                noteDataSnapshot.getValue(Produkt.class).getKcal()));

                                                                    }
//                                    arrayList.add(dataSnapshot.getValue());
//                                    ArrayAdapter<String> secondArrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, arrayList);
//                                    listView.setAdapter(secondArrayAdapter);
                                                                } else {
                                                                    //Toast.makeText(SelectDate.this, "Brak danych", Toast.LENGTH_SHORT).show();
                                                                }



                                                                if(!mapProduct.isEmpty()){
                                                                    for (Map.Entry<String, Produkt> entry : mapProduct.entrySet()) {
                                                                        System.out.println("++++++++++++++++++ " +entry.getKey() + " ------ " + entry.getValue().getAmount() + entry.getValue().getProtein() +
                                                                                entry.getValue().getCarbs() + entry.getValue().getFat() + entry.getValue().getKcal()) ;
                                                                        arrayList.add("przekaski | " + entry.getKey() + " ilosc = " + entry.getValue().getAmount() + " białko = " +  entry.getValue().getProtein() +
                                                                                " węglowodany =" + entry.getValue().getCarbs() + " tłuszcz = " +entry.getValue().getFat() +" kcal = "+ entry.getValue().getKcal() );
                                                                    }
                                                                }
                                                                ArrayAdapter<String> secondArrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, arrayList);

                                                                listView.setAdapter(secondArrayAdapter);

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


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }


                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();

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
            Toast.makeText(SelectDate.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(SelectDate.this, EmailPasswordActivity.class);
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
                                intent = new Intent(SelectDate.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(SelectDate.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:

                                intent = new Intent(SelectDate.this, CurrentList.class);
                                startActivity(intent);
                                break;
                            case 5:

                                intent = new Intent(SelectDate.this, GraphActivity.class);
                                startActivity(intent);
                                break;
                            case 6:

                                intent = new Intent(SelectDate.this, SelectDate.class);
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
