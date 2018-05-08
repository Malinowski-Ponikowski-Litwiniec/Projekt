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
import android.widget.Button;
import android.widget.CheckBox;
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

public class EditProductFromToday extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView productName;

    TextView productCarbs;
    TextView productFat;
    TextView productProtein;
    EditText productAmount;
    TextView productKcal;

    Double productCarbsTemp;
    Double productFatTemp;
    Double productProteinTemp;
    Double productKcalTemp;

    Double productAmountTemp;

    Button dodajBtn;

    Double productResultCarbs;
    Double productResultFat;
    Double productResultProtein;
    Double productResultKcal;
    Double productResultAmount;

    CheckBox sniadanie;
    CheckBox obiad;
    CheckBox kolacja;
    CheckBox przekaski;

    String chooser;
    Toolbar myToolbar;
    public String dateFormat;
    public String name;
    public Map<String, String> map = new HashMap<>();
    public DatabaseReference kcalRef;
    public DatabaseReference macroRef;
    public DatabaseReference testRef;

    double curKcal;
    double curProtein;
    double curCarbs;
    double curFat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_from_today);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);

        //ustawie referencji
        kcalRef = myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("burnedAndKcal").child("kcal");
        macroRef = myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("curMacro");
        testRef = myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("sniadanie");
        testRef = myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("obiad");
        testRef = myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("kolacja");
        testRef = myRef.child("lista").child(mAuth.getUid()).child(dateFormat).child("przekaski");


        dodajBtn = (Button) findViewById(R.id.dodajBtn);

        productName = (TextView) findViewById(R.id.productName);

        productCarbs = (TextView) findViewById(R.id.carbs);
        productFat = (TextView) findViewById(R.id.fat);
        productProtein = (TextView) findViewById(R.id.protein);
        productAmount = (EditText) findViewById(R.id.amount);
        productKcal = (TextView) findViewById(R.id.kcal);

        sniadanie = (CheckBox) findViewById(R.id.sniadanie);
        obiad = (CheckBox) findViewById(R.id.obiad);
        kolacja = (CheckBox) findViewById(R.id.kolacja);
        przekaski = (CheckBox) findViewById(R.id.przekaski);

        sniadanie.setOnClickListener(onRadioButtonClick);
        obiad.setOnClickListener(onRadioButtonClick);
        kolacja.setOnClickListener(onRadioButtonClick);
        przekaski.setOnClickListener(onRadioButtonClick);
        productAmount.setText("100");


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        productName.setText(name);


        getFromDatabase();
        dodajBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productAmount.getText().toString().isEmpty()) {
                    Toast.makeText(EditProductFromToday.this, "Ilość nie może być pusta!", Toast.LENGTH_SHORT).show();
                } else {
                    if (sniadanie.isChecked() || obiad.isChecked() || kolacja.isChecked() || przekaski.isChecked()) {
                        productAmountTemp = Double.valueOf(productAmount.getText().toString()) / 100;


                        //ustawienie miejsca po przecinku w wyniku

                        productResultCarbs = BigDecimal.valueOf(Double.valueOf(productCarbs.getText().toString())).setScale(1, RoundingMode.HALF_UP).doubleValue();
                        productResultFat = BigDecimal.valueOf(Double.valueOf(productFat.getText().toString())).setScale(1, RoundingMode.HALF_UP).doubleValue();
                        productResultProtein = BigDecimal.valueOf(Double.valueOf(productProtein.getText().toString())).setScale(1, RoundingMode.HALF_UP).doubleValue();
                        productResultKcal = BigDecimal.valueOf(Double.valueOf(productKcal.getText().toString())).setScale(1, RoundingMode.HALF_UP).doubleValue();

                        if (Integer.parseInt(productAmount.getText().toString()) == 100) {
                            productResultAmount = 100.0;
                        } else {
                            int pom = Integer.parseInt(productAmount.getText().toString()) / 100;
                            productResultAmount = BigDecimal.valueOf(Double.valueOf(pom)).setScale(1, RoundingMode.HALF_UP).doubleValue();
                            productResultCarbs *= pom;
                            productResultFat *= pom;
                            productResultProtein *= pom;
                            productResultKcal *= pom;
                        }
                        //utworzenie obiektu z końcowymi wartościami
                        Produkt produkt = new Produkt("", productResultAmount, productResultProtein, productResultCarbs, productResultFat, productResultKcal);


                        //dodanie wybranego produktu do bazy
                        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).setValue(chooser);
                                //  myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).setValue(name);
                                myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).setValue(produkt);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        setKcal();

                        //powrót do Profil Użytkownika
                        Intent intent = new Intent(EditProductFromToday.this, UserProfile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {

                        Toast.makeText(EditProductFromToday.this, "Zaznacz jakąś opcje ", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });

    }
    public void setKcal() {
        curKcal = 0;
        curProtein = 0;
        curCarbs = 0;
        curFat = 0;
        //obliczenie sumy macro i kcal
        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("sniadanie").addValueEventListener(new ValueEventListener() {
            Map<String, Produkt> map = new HashMap<>();


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(), new Produkt("", noteDataSnapshot.getValue(Produkt.class).getAmount(),
                            noteDataSnapshot.getValue(Produkt.class).getProtein(),
                            noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                            noteDataSnapshot.getValue(Produkt.class).getFat(),
                            noteDataSnapshot.getValue(Produkt.class).getKcal()));
                }
                for (Map.Entry<String, Produkt> entry : map.entrySet()) {

                    curKcal += entry.getValue().getKcal();
                    curProtein += entry.getValue().getProtein();
                    curCarbs += entry.getValue().getCarbs();
                    curFat += entry.getValue().getFat();

                }
                //obliczenie sumy macro i kcal
                myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("obiad").addValueEventListener(new ValueEventListener() {
                    Map<String, Produkt> map = new HashMap<>();

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            map.put(noteDataSnapshot.getKey(), new Produkt("", noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                    noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                    noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                    noteDataSnapshot.getValue(Produkt.class).getFat(),
                                    noteDataSnapshot.getValue(Produkt.class).getKcal()));
                        }
                        for (Map.Entry<String, Produkt> entry : map.entrySet()) {
                            curKcal += entry.getValue().getKcal();
                            curProtein += entry.getValue().getProtein();
                            curCarbs += entry.getValue().getCarbs();
                            curFat += entry.getValue().getFat();

                        }
                        //obliczenie sumy macro i kcal w śniadaniu
                        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("kolacja").addValueEventListener(new ValueEventListener() {
                            Map<String, Produkt> map = new HashMap<>();

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                    map.put(noteDataSnapshot.getKey(), new Produkt("", noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                            noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                            noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                            noteDataSnapshot.getValue(Produkt.class).getFat(),
                                            noteDataSnapshot.getValue(Produkt.class).getKcal()));

                                }
                                for (Map.Entry<String, Produkt> entry : map.entrySet()) {
                                    curKcal += entry.getValue().getKcal();
                                    curProtein += entry.getValue().getProtein();
                                    curCarbs += entry.getValue().getCarbs();
                                    curFat += entry.getValue().getFat();

                                }
                                //obliczenie sumy macro i kcal w śniadaniu
                                myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("przekaski").addValueEventListener(new ValueEventListener() {
                                    Map<String, Produkt> map = new HashMap<>();

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                                            map.put(noteDataSnapshot.getKey(), new Produkt("", noteDataSnapshot.getValue(Produkt.class).getAmount(),
                                                    noteDataSnapshot.getValue(Produkt.class).getProtein(),
                                                    noteDataSnapshot.getValue(Produkt.class).getCarbs(),
                                                    noteDataSnapshot.getValue(Produkt.class).getFat(),
                                                    noteDataSnapshot.getValue(Produkt.class).getKcal()));

                                        }
                                        for (Map.Entry<String, Produkt> entry : map.entrySet()) {
                                            curKcal += entry.getValue().getKcal();
                                            curProtein += entry.getValue().getProtein();
                                            curCarbs += entry.getValue().getCarbs();
                                            curFat += entry.getValue().getFat();

                                        }
                                        kcalRef.setValue(BigDecimal.valueOf(curKcal).setScale(1, RoundingMode.HALF_UP).doubleValue());
                                        curFat = BigDecimal.valueOf(curFat).setScale(1, RoundingMode.HALF_UP).doubleValue();
                                        curCarbs = BigDecimal.valueOf(curCarbs).setScale(1, RoundingMode.HALF_UP).doubleValue();
                                        curProtein = BigDecimal.valueOf(curProtein).setScale(1, RoundingMode.HALF_UP).doubleValue();

                                        Produkt produkt = new Produkt("", 0, curProtein, curCarbs, curFat, curKcal);
                                        macroRef.setValue(produkt);
                                        setCurKcal();
                                        System.out.println(produkt.getProtein() + " ----------- " + produkt.getCarbs() + " _---------- " + produkt.getFat());
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

    public void setCurKcal(){
        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("burnedAndKcal").addValueEventListener(new ValueEventListener() {
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
    //walidacja zaznaczania checkBoxów
    View.OnClickListener onRadioButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sniadanie:
                    obiad.setChecked(false);
                    kolacja.setChecked(false);
                    przekaski.setChecked(false);
                    chooser = "sniadanie";
                    break;
                case R.id.obiad:
                    sniadanie.setChecked(false);
                    kolacja.setChecked(false);
                    przekaski.setChecked(false);
                    chooser = "obiad";
                    break;
                case R.id.kolacja:
                    obiad.setChecked(false);
                    sniadanie.setChecked(false);
                    przekaski.setChecked(false);
                    chooser = "kolacja";
                    break;
                case R.id.przekaski:
                    obiad.setChecked(false);
                    kolacja.setChecked(false);
                    sniadanie.setChecked(false);
                    chooser = "przekaski";
                    break;
                default:
                    break;

            }
        }
    };


    //pobranie z bazy ilosc składników w danym produkcie
    public void getFromDatabase() {
        myRef.child("produkty").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(), String.valueOf(noteDataSnapshot.getValue()));
                }
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals("carbs")) {
                        productCarbsTemp = Double.valueOf(entry.getValue());
                        productCarbs.setText(entry.getValue());
                    }
                    if (entry.getKey().equals("fat")) {
                        productFatTemp = Double.valueOf(entry.getValue());
                        productFat.setText(entry.getValue());

                    }

                    if (entry.getKey().equals("kcal")) {
                        productKcalTemp = Double.valueOf(entry.getValue());
                        productKcal.setText(entry.getValue());

                    }
                    if (entry.getKey().equals("protein")) {
                        productProteinTemp = Double.valueOf(entry.getValue());
                        productProtein.setText(entry.getValue());

                    }
                    System.out.println(entry.getKey() + " ----- " + entry.getValue());
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
            Toast.makeText(EditProductFromToday.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(EditProductFromToday.this, EmailPasswordActivity.class);
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
                                intent = new Intent(EditProductFromToday.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(EditProductFromToday.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:

                                intent = new Intent(EditProductFromToday.this, CurrentList.class);
                                startActivity(intent);
                                break;
                            case 5:

                                intent = new Intent(EditProductFromToday.this, GraphActivity.class);
                                startActivity(intent);
                                break;
                            case 6:

                                intent = new Intent(EditProductFromToday.this, SelectDate.class);
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