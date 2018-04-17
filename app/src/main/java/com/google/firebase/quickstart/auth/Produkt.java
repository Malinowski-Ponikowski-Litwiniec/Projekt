package com.google.firebase.quickstart.auth;

/**
 * Created by linux on 14.03.18.
 */

public class Produkt {
    public String name;
    public double amount;
    public double protein;
    public double carbs;
    public double fat;
    public double kcal;




    public Produkt(String name, double amount, double protein, double carbs, double fat, double kcal) {
        this.name = name;
        this.amount = amount;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.kcal = kcal;
    }
public Produkt(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }
}
