package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.drive.PositionTracker;

public class DriveDisplay {
    PositionTracker positionTracker = PositionTracker.getInstance();

    public ShuffleboardTab driveTab;
    public static NetworkTableEntry resetLocation;
    public static NetworkTableEntry resetHeading;
    public static NetworkTableEntry posX, posY, angle;

    public DriveDisplay() {
        driveTab = Shuffleboard.getTab("Position Tracker");
    }


    public void PositionTrackerDisplay() {
        resetHeading = driveTab
                .add("Reset Heading", false)
                .withWidget("Toggle Button")
                .withPosition(7,1)
                .getEntry();
        resetLocation = driveTab
                .add("Reset Position", false)
                .withWidget("Toggle Button")
                .withPosition(8,1)
                .getEntry();
        posX = driveTab
                .add("Pos X", 0)
                .withPosition(7, 0)
                .getEntry();
        posY = driveTab
                .add("Pos Y", 0)
                .withPosition(8, 0)
                .getEntry();
        angle = driveTab
                .add("Angle", 0)
                .withPosition(6,0)
                .getEntry();
    }
}
