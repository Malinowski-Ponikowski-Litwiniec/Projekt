package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddDailyProductsFrom extends AppCompatActivity {
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

    Double productAmountTemp ;

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

    public String dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_products_from);

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);

        dodajBtn = (Button)findViewById(R.id.dodajBtn);

        productName = (TextView) findViewById(R.id.productName);

        productCarbs = (TextView) findViewById(R.id.carbs);
        productFat = (TextView) findViewById(R.id.fat);
        productProtein = (TextView) findViewById(R.id.protein);
        productAmount = (EditText) findViewById(R.id.amount);
        productKcal = (TextView)findViewById(R.id.kcal);

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
        String name = intent.getStringExtra("name");
        productName.setText(name);

        Map<String, String> map = new HashMap<>();
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
        productAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    productAmount.setText("");
                }else{
                    if(productAmount.getText().toString().isEmpty()){
                        productAmount.setText("100");
                    }
                   productAmountTemp = Double.valueOf(productAmount.getText().toString())/100;

                    DecimalFormat decimalFormat = new DecimalFormat(".##");

                    productResultAmount = Double.valueOf(decimalFormat.format(productAmountTemp));
                    productResultCarbs = Double.valueOf(decimalFormat.format(productCarbsTemp*productAmountTemp));
                    productResultFat = Double.valueOf(decimalFormat.format(productFatTemp*productAmountTemp));
                    productResultProtein = Double.valueOf(decimalFormat.format(productProteinTemp*productAmountTemp));
                    productResultKcal = Double.valueOf(decimalFormat.format(productKcalTemp*productAmountTemp));

                    productCarbs.setText(String.valueOf(productResultCarbs));
                    productFat.setText(String.valueOf(productResultFat));
                    productProtein.setText(String.valueOf(productResultProtein));
                    productKcal.setText(String.valueOf(productResultKcal));


                }
            }
        });





        dodajBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productAmount.getText().toString().isEmpty()) {
                    Toast.makeText(AddDailyProductsFrom.this,"Ilość nie może być pusta!",Toast.LENGTH_SHORT).show();
                } else {
                    if (sniadanie.isChecked() || obiad.isChecked() || kolacja.isChecked() || przekaski.isChecked()) {
                        productAmountTemp = Double.valueOf(productAmount.getText().toString())/100;

                        DecimalFormat decimalFormat = new DecimalFormat(".##");

                        productResultAmount = Double.valueOf(decimalFormat.format(productAmountTemp));
                        productResultCarbs = Double.valueOf(decimalFormat.format(productCarbsTemp * productAmountTemp));
                        productResultFat = Double.valueOf(decimalFormat.format(productFatTemp * productAmountTemp));
                        productResultProtein = Double.valueOf(decimalFormat.format(productProteinTemp * productAmountTemp));
                        productResultKcal = Double.valueOf(decimalFormat.format(productKcalTemp * productAmountTemp));


                        Produkt produkt = new Produkt(productResultAmount, productResultProtein, productResultCarbs, productResultFat, productResultKcal);



                        if(myRef.child("lista").child(mAuth.getCurrentUser().getUid()) == null){
                            myRef.child("lista").setValue(mAuth.getCurrentUser().getUid());
                        }

                        if (myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat) == null) {
                            myRef.child("lista").setValue(dateFormat);

                        }

                        if (myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser) != null) {
                            if (myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name) != null) {
                                myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).setValue(produkt);
                                Toast.makeText(AddDailyProductsFrom.this, "Dodano", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddDailyProductsFrom.this, EditOrDelete.class);
                                startActivity(intent);
                            }
                        } else {
                            myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).setValue(chooser);
                            myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).setValue(name);
                            myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).setValue(produkt);
                            Toast.makeText(AddDailyProductsFrom.this, "Dodano", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddDailyProductsFrom.this, EditOrDelete.class);
                            startActivity(intent);

                        }
                    } else {

                        Toast.makeText(AddDailyProductsFrom.this, "Zaznacz jakąś opcje ", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });

    }
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

}
