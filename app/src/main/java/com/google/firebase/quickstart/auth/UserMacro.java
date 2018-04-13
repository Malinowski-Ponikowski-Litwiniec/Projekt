package com.google.firebase.quickstart.auth;

/**
 * Created by linux on 24.03.18.
 */

public class UserMacro {
    public String kcal;
    public String protein;
    public String carbs;
    public String fat;
    public String bmi;

    public double doublePprotein;
    public double  doublearbs;
    public double doubleFat;
public UserMacro(){}
    public UserMacro(String kcal, String protein, String carbs, String fat, String bmi) {
        this.kcal = kcal;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.bmi = bmi;
    }
    public UserMacro(double protein, double carbs, double fat) {
        this.doublePprotein = protein;
        this.doublearbs= carbs;
        this.doubleFat = fat;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }
}
