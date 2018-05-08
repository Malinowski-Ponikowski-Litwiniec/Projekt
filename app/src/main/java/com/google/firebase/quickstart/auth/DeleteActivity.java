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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class DeleteActivity extends AppCompatActivity {
    public EditText emailInput;
    //public EditText passwordInput;
    public CheckBox takCheck;
    public CheckBox nieCheck;
    public Button deleteBtn;


    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        emailInput = (EditText) findViewById(R.id.emailInput);
        // passwordInput = (EditText)findViewById(R.id.passwordInput);
        takCheck = (CheckBox) findViewById(R.id.takCheck);

        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(deleteBtnOnClick);

    //    takCheck.setOnClickListener(takCheckListener);

        //Zmiana koloru paska nawigacji
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.md_black_1000));

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
    }

//    View.OnClickListener takCheckListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (nieCheck.isChecked()) {
//                nieCheck.toggle();
//            }
//        }
//    };

    //zdarzenie wykonywanie podczas usuwania konta
    View.OnClickListener deleteBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!takCheck.isChecked()) {
                Toast.makeText(DeleteActivity.this, "WYBIERZ OPCJE!!!", Toast.LENGTH_SHORT).show();
            } else {
                if (takCheck.isChecked()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user.getEmail().equals((emailInput.getText().toString()).replaceAll("\\s", ""))) {
                        user.delete();
                        Toast.makeText(DeleteActivity.this, "Twoje konto zostało usunięte", Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(DeleteActivity.this,EmailPasswordActivity.class);
//                        startActivity(intent);

                        mAuth.signOut();
                        updateUI();
                    } else {
                        Toast.makeText(DeleteActivity.this, "Podałeś nieprawidłowy email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DeleteActivity.this, "NIE TO NIE!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    //powrót do aktywności EmailPasswordActivity
    private void updateUI() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);

        //wyczyszczenie poprzedniej aktywności
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
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
            Toast.makeText(DeleteActivity.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(DeleteActivity.this, EmailPasswordActivity.class);
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
                                intent = new Intent(DeleteActivity.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 3:

                                intent = new Intent(DeleteActivity.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 4:

                                intent = new Intent(DeleteActivity.this, CurrentList.class);
                                startActivity(intent);
                                break;
                            case 5:

                                intent = new Intent(DeleteActivity.this, GraphActivity.class);
                                startActivity(intent);
                                break;
                            case 6:

                                intent = new Intent(DeleteActivity.this, SelectDate.class);
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
