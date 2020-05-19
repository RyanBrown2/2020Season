package com.team3250.frc2020.auto.modes;

import com.team3250.frc2020.auto.AutoModeEndedException;

public class DoNothingMode extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("doing nothing");
    }
}
