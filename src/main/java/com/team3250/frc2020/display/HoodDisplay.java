package com.team3250.frc2020.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class HoodDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry hoodSetpoint, hoodAngle;

    public HoodDisplay() {
        tab = Shuffleboard.getTab("Shooter");

        hoodSetpoint =
                tab.add("Hood Setpoint", 0)
                .withPosition(1, 1)
                .getEntry();

        hoodAngle =
                tab.add("Hood Angle", 0)
                .withPosition(0,1)
                .getEntry();
    }

    public void setpoint(double setpoint) {
        hoodSetpoint.setDouble(setpoint);
    }

    public void angle(double angle) {
        hoodAngle.setDouble(angle);
    }
}
