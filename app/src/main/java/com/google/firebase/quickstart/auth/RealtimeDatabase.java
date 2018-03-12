package com.google.firebase.quickstart.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mateu on 04.03.2018.
 */

public class RealtimeDatabase {
    public DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
    public FirebaseAuth myRef = FirebaseAuth.getInstance();

    public void setValue(CurrentUser value) {
        CurrentUser user = new CurrentUser(value.getWiek(), value.getWaga(),value.getWzrost(), value.getActivity(), value.getSex());
        mDatabase.child(myRef.getUid()).setValue(user);


    }


}
