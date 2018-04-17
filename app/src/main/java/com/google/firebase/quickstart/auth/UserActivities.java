package com.google.firebase.quickstart.auth;

/**
 * Created by linux on 02.04.18.
 */

public class UserActivities {
    public String name;
    public Double kcal;
    public Double time;

    public UserActivities(){}
    public UserActivities(Double kcal, Double time) {
        this.name = name;
        this.kcal = kcal;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
