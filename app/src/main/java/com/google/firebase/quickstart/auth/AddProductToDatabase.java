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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class AddProductToDatabase extends AppCompatActivity {
    public EditText name;
    public EditText amount;
    public EditText protein;
    public EditText carbs;
    public EditText fat;
    public EditText kcal;
    public Button send;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_database);
        name = (EditText) findViewById(R.id.name);
        amount = (EditText) findViewById(R.id.amount);
        protein = (EditText) findViewById(R.id.protein);
        carbs = (EditText) findViewById(R.id.carbs);
        fat = (EditText) findViewById(R.id.fat);
        kcal = (EditText) findViewById(R.id.kcal);

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(sendOnClick);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();

        // Zmiana koloru status bara
        Window window = EmailPasswordActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(EmailPasswordActivity.this.getResources().getColor(R.color.grey_dark));
    }




    View.OnClickListener addProductToListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddProductToDatabase.this,AddDailyProducts.class);
            startActivity(intent);
        }
    };
    View.OnClickListener addActivityToDatabaseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddProductToDatabase.this,AddActivityToDatabase.class);
            startActivity(intent);
        }
    };
    View.OnClickListener addActivityToListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddProductToDatabase.this,AddDailyActivity.class);
            startActivity(intent);
        }
    };

    //wysłanie produktu do bazy
    View.OnClickListener sendOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Produkt produkt = new Produkt(name.getText().toString(), Double.valueOf(amount.getText().toString()),
                    Double.valueOf(protein.getText().toString()), Double.valueOf(carbs.getText().toString()),
                    Double.valueOf(fat.getText().toString()), Double.valueOf(kcal.getText().toString()));
            ProductsDatabase pd = new ProductsDatabase();
            pd.sendProdukt(produkt);
            Toast.makeText(AddProductToDatabase.this, "Wysłano produkt do bazy", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AddProductToDatabase.this, UserProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        }
    };

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
            Toast.makeText(AddProductToDatabase.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(AddProductToDatabase.this, EmailPasswordActivity.class);
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
                                intent = new Intent(AddProductToDatabase.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(AddProductToDatabase.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:

                                intent = new Intent(AddProductToDatabase.this, CurrentList.class);
                                startActivity(intent);
                                break;
                            case 5:

                                intent = new Intent(AddProductToDatabase.this, GraphActivity.class);
                                startActivity(intent);
                                break;
                            case 6:

                                intent = new Intent(AddProductToDatabase.this, SelectDate.class);
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
