package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EditOrDelete extends AppCompatActivity {
    public Button editBtn;
    public Button deleteBtn;
    public Button signOutBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete);
         editBtn = (Button)findViewById(R.id.editBtn);
         deleteBtn = (Button)findViewById(R.id.deleteBtn);
         signOutBtn = (Button)findViewById(R.id.signOutBtn);

         editBtn.setOnClickListener(editBtnOnClick);
         deleteBtn.setOnClickListener(deleteBtnOnClick);
         signOutBtn.setOnClickListener(singOutBtnClick);

        mAuth = FirebaseAuth.getInstance();

    }
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
    View.OnClickListener singOutBtnClick = new View.OnClickListener () {
        @Override
        public void onClick(View v) {
            Toast.makeText(EditOrDelete.this, "Wylogowano", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            updateUI();
        }
    };

    private void updateUI() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);
    }
}
