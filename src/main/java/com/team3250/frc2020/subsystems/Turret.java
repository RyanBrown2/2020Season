package com.team3250.frc2020.subsystems;

public class Turret {
    private static Turret mInstance;
    public static Turret getInstance() {
        if (mInstance == null) {
            mInstance = new Turret();
        }
        return mInstance;
    }

}
