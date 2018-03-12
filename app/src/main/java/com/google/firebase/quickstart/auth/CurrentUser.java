package com.google.firebase.quickstart.auth;

/**
 * Created by Mateu on 04.03.2018.
 */

public class CurrentUser {


    public String wiek;
    public String waga;
    public String wzrost;
    public String activity;
    public String sex;
    public String uId;




    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    CurrentUser(){

}
    CurrentUser(String wiek, String waga, String wzrost, String activity, String sex){
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


    public String getWiek() {
        return wiek;
    }

    public void setWiek(String wiek) {
        this.wiek = wiek;
    }

    public String getWaga() {
        return waga;
    }

    public void setWaga(String waga) {
        this.waga = waga;
    }

    public String getWzrost() {
        return wzrost;
    }

    public void setWzrost(String  wzrost) {
        this.wzrost = wzrost;
    }
}
