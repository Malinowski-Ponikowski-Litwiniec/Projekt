package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteActivity extends AppCompatActivity {
    public EditText emailInput;
   //public EditText passwordInput;
    public CheckBox takCheck;
    public CheckBox nieCheck;
    public Button deleteBtn;
    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        emailInput = (EditText)findViewById(R.id.emailInput);
      // passwordInput = (EditText)findViewById(R.id.passwordInput);
        takCheck = (CheckBox)findViewById(R.id.takCheck);
        nieCheck = (CheckBox)findViewById(R.id.nieCheck);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(deleteBtnOnClick);

        takCheck.setOnClickListener(takCheckListener);
        nieCheck.setOnClickListener(nieCheckListener);


        mAuth = FirebaseAuth.getInstance();
    }
    View.OnClickListener takCheckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(nieCheck.isChecked()){
                nieCheck.toggle();
            }
        }
    };

    View.OnClickListener nieCheckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(takCheck.isChecked()){
                takCheck.toggle();
            }
        }
    };
    View.OnClickListener deleteBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!takCheck.isChecked() && !nieCheck.isChecked()){
                Toast.makeText(DeleteActivity.this,"WYBIERZ OPCJE!!!",Toast.LENGTH_SHORT).show();
            }else{
                if(takCheck.isChecked()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user.getEmail().equals(emailInput.getText().toString())) {
                        user.delete();
                        Toast.makeText(DeleteActivity.this, "Twoje konto zostało usunięte", Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(DeleteActivity.this,EmailPasswordActivity.class);
//                        startActivity(intent);
                        
                        mAuth.signOut();
                        updateUI();
                    }else{
                        Toast.makeText(DeleteActivity.this, "Podałeś nieprawidłowy email", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DeleteActivity.this,"NIE TO NIE!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void updateUI() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);
    }
}
