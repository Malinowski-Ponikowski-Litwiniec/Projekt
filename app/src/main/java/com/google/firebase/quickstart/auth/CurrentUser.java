package com.google.firebase.quickstart.auth;

/**
 * Created by Mateu on 04.03.2018.
 */

public class CurrentUser {


    public int wiek;
    public double waga;
    public double wzrost;
    public String activity;
    public String sex;
    public String uId;
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    CurrentUser(){

}
    CurrentUser(int wiek, double waga, double wzrost, String activity, String sex){
        this.wiek = wiek;
        this.waga = waga;
        this.wzrost =wzrost;
        this.activity = activity;
        this.sex = sex;

    }



    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public int getWiek() {
        return wiek;
    }

    public void setWiek(int wiek) {
        this.wiek = wiek;
    }

    public double getWaga() {
        return waga;
    }

    public void setWaga(double waga) {
        this.waga = waga;
    }

    public double getWzrost() {
        return wzrost;
    }

    public void setWzrost(double wzrost) {
        this.wzrost = wzrost;
    }
}
