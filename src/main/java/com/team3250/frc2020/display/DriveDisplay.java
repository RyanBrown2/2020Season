package com.team3250.frc2020.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class DriveDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry encoderPos;
    NetworkTableEntry encoderVel;
    NetworkTableEntry encoderLeft, encoderRight;

    public DriveDisplay() {
        tab = Shuffleboard.getTab("DriveBase");

        encoderPos =
                tab.add("Encoder Position", 0)
                .withPosition(0, 1)
                .getEntry();

        encoderVel =
                tab.add("Encoder Velocity", 0)
                .withPosition(1,1)
                .getEntry();

        encoderLeft =
                tab.add("Left Encoder Pos", 0)
                .withPosition(0, 0)
                .withSize(2, 1)
                .getEntry();

        encoderRight =
                tab.add("Right Encoder Pos", 0)
                .withPosition(2, 0)
                .withSize(2, 1)
                .getEntry();

    }

    public void position(double pos) {
        encoderPos.setDouble(pos);
    }

    public void velocity(double vel) {
        encoderVel.setDouble(vel);
    }

    public void left(double pos) {
        encoderLeft.setDouble(pos);
    }

    public void right(double pos) {
        encoderRight.setDouble(pos);
    }
}
