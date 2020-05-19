package com.team3250.frc2020.subsystems;

public class Hood {
    private static Hood mInstance;
    public static Hood getInstance() {
        if (mInstance == null) {
            mInstance = new Hood();
        }
        return mInstance;
    }

}
