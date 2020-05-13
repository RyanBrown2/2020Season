package com.team3250.frc2020.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class PositionTrackerDisplay {

    ShuffleboardTab tab;

    NetworkTableEntry xPos, yPos;
    NetworkTableEntry angle;
    NetworkTableEntry xResetPos, yResetPos;

    // Buttons
    NetworkTableEntry resetLocation;
    NetworkTableEntry resetHeading;

    public PositionTrackerDisplay() {
        tab = Shuffleboard.getTab("DriveBase");

        xPos =
                tab.add("X Position", 0)
                .withPosition(7,0)
                .getEntry();

        yPos =
                tab.add("Y Position", 0)
                .withPosition(8, 0)
                .getEntry();

        angle =
                tab.add("Angle", 0)
                .withPosition(6,0)
                .getEntry();

        xResetPos =
                tab.add("X Reset Position", 0)
                .withPosition(5, 1)
                .withSize(2,1)
                .getEntry();

        yResetPos =
                tab.add("Y Reset Posistion", 0)
                .withPosition(7, 1)
                .withSize(2,1)
                .getEntry();

        resetLocation =
                tab.add("Reset Position", false)
                .withPosition(4, 0)
                .getEntry();

        resetHeading =
                tab.add("Reset Heading", false)
                .withPosition(4, 1)
                .getEntry();
    }

    public void xPosition(double x) {
        xPos.setDouble(x);
    }

    public void yPosition(double y) {
        yPos.setDouble(y);
    }

    public void setAngle(double angle) {
        this.angle.setDouble(angle);
    }

    public void xReset(double x) {
        xResetPos.setDouble(x);
    }

    public void yReset(double y) {
        yResetPos.setDouble(y);
    }

    public double getXReset() {
        return xResetPos.getDouble(0);
    }

    public double getYReset() {
        return yResetPos.getDouble(0);
    }

    public boolean locationReset() {
        return resetLocation.getBoolean(false);
    }

    public boolean headingReset() {
        return resetHeading.getBoolean(false);
    }

    public void untoggleButtons() {
        if (locationReset()) {
            resetLocation.setBoolean(false);
        }

        if (headingReset()) {
            resetHeading.setBoolean(false);
        }
    }
}
