package com.google.firebase.quickstart.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProductToDatabase extends AppCompatActivity {
        public EditText name;
        public EditText amount;
        public EditText protein;
        public EditText carbs;
        public EditText fat;
        public EditText kcal;
        public Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_database);
        name = (EditText)findViewById(R.id.name);
        amount = (EditText)findViewById(R.id.amount);
        protein = (EditText)findViewById(R.id.protein);
        carbs = (EditText)findViewById(R.id.carbs);
        fat = (EditText)findViewById(R.id.fat);
        kcal = (EditText)findViewById(R.id.kcal);

        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(sendOnClick);


    }

    View.OnClickListener sendOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Produkt produkt = new Produkt(name.getText().toString(),Double.valueOf(amount.getText().toString()),
                    Double.valueOf(protein.getText().toString()),Double.valueOf(carbs.getText().toString()),
            Double.valueOf(fat.getText().toString()),Double.valueOf(kcal.getText().toString()));
            ProductsDatabase pd = new ProductsDatabase();
            pd.sendProdukt(produkt);
            Toast.makeText(AddProductToDatabase.this,"Wysłano produkt do bazy",Toast.LENGTH_LONG).show();
        }
    };
}
