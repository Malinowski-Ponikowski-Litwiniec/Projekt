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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EditOrDelete extends AppCompatActivity {
    public Button editBtn;
    public Button deleteBtn;
    public Button signOutBtn;
    public Button addBtn;
    private FirebaseAuth mAuth;
    public Button addToDalyBtn;
    public Button showDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
         editBtn = (Button)findViewById(R.id.editBtn);
         deleteBtn = (Button)findViewById(R.id.deleteBtn);

        addBtn = (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(addBtnOnClick);
         editBtn.setOnClickListener(editBtnOnClick);
         deleteBtn.setOnClickListener(deleteBtnOnClick);
         
        addToDalyBtn = (Button)findViewById(R.id.addToDalyBtn);
        addToDalyBtn.setOnClickListener(addToDalyBtnOnCLick);
        mAuth = FirebaseAuth.getInstance();
        showDates = (Button)findViewById(R.id.showDates);
        showDates.setOnClickListener(showDatesOnClick);

        // Zmiana koloru status bara
        Window window = EmailPasswordActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(EmailPasswordActivity.this.getResources().getColor(R.color.grey_dark));

        //Zmiana koloru paska nawigacji
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.md_black_1000));

    }

    View.OnClickListener showDatesOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EditOrDelete.this,SelectDate.class);
            startActivity(intent);
        }
    };
    View.OnClickListener addToDalyBtnOnCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EditOrDelete.this,AddDailyProducts.class);
            startActivity(intent);
        }
    };
    View.OnClickListener addBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EditOrDelete.this,AddProductToDatabase.class);
            startActivity(intent);
        }
    };
    View.OnClickListener editBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EditOrDelete.this,EditActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener deleteBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EditOrDelete.this,DeleteActivity.class);
            startActivity(intent);
        }
    };


    private void updateUI() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.settings) {
            Toast.makeText(EditOrDelete.this,"settings",Toast.LENGTH_SHORT).show();
            return true;
        }if(item.getItemId() == R.id.logOut){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(EditOrDelete.this,EmailPasswordActivity.class);
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
}
