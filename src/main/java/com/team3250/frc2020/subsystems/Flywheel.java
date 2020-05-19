package com.team3250.frc2020.subsystems;

public class Flywheel  {
    private static Flywheel mInstance;
    public static Flywheel getInstance() {
        if (mInstance == null) {
            mInstance = new Flywheel();
        }
        return mInstance;
    }

}
