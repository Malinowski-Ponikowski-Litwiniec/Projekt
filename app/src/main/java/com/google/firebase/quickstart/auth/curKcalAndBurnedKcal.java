package com.google.firebase.quickstart.auth;

public class curKcalAndBurnedKcal {
double kcal;
double burnedKcal;

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getBurnedKcal() {
        return burnedKcal;
    }

    public void setBurnedKcal(double burnedKcal) {
        this.burnedKcal = burnedKcal;
    }

    public curKcalAndBurnedKcal(double kcal, double burnedKcal) {

        this.kcal = kcal;
        this.burnedKcal = burnedKcal;
    }
}
