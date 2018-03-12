package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditOrDelete extends AppCompatActivity {
    public Button editBtn;
    public Button deleteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete);
         editBtn = (Button)findViewById(R.id.editBtn);
         deleteBtn = (Button)findViewById(R.id.deleteBtn);
         editBtn.setOnClickListener(editBtnOnClick);
         deleteBtn.setOnClickListener(deleteBtnOnClick);


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
}
