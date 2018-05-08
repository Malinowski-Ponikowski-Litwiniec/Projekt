package com.google.firebase.quickstart.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by linux on 24.03.18.
 */

public class UserBmi {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String curId = mAuth.getUid();
    CurrentUser user;
    public double bmi;
    public CurrentUser calculateBmi(CurrentUser user) {
        bmi = (Double.valueOf(user.getWaga()) / (Math.pow(Double.valueOf(user.getWzrost())/100, 2)));
        System.out.println(" +++++++++++++++++++++++++++++++++ " + bmi);
        CurrentUser resUser = new CurrentUser(user.getWiek(), user.getWaga(), user.getWzrost(), user.getActivity(), user.getSex(), user.getGoal(), curId);

        return resUser;
    }

    public UserMacro calculateMacro(CurrentUser user) {
        double kcal = 0.0;
        if (user.getSex().equals("mężczyzna")) {
            kcal = 66.47 + (13.7 * Double.valueOf(user.getWaga())) + (5 * Double.valueOf(user.getWzrost())) - (6.76 * Double.valueOf(user.getWiek()));
        }
        if (user.getSex().equals("kobieta")) {
            kcal = 655.1 + (9.567 * Double.valueOf(user.getWaga())) + (1.85 * Double.valueOf(user.getWzrost())) - (4.68 * Double.valueOf(user.getWiek()));
        }
        switch (user.getActivity()) {
            case "niska":
                kcal *= 1.2;
                break;
            case "średnia":
                kcal *= 1.375;
                break;
            case "wysoka":
                kcal *= 1.55;
                break;
            case "bardzo wysoka":
                kcal *= 1.725;
                break;
            default:
                break;
        }
        kcal = Math.round(kcal);
        double protein =Math.round((kcal * 0.3) / 4);
        double carbs = Math.round((kcal * 0.4) / 4);
        double fat = Math.round((kcal * 0.3) / 9);
       UserMacro macro = new UserMacro(String.valueOf(kcal),String.valueOf(protein),String.valueOf(carbs),String.valueOf(fat),String.valueOf(Math.round(bmi)));
        return macro;
    }
}