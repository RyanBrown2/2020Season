package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.drive.PositionTracker;

public class DriveDisplay {
    PositionTracker positionTracker = PositionTracker.getInstance();

    public DriveDisplay() {

    }

    public void PostionTrackerDisplay() {
        NetworkTableEntry postionTracker = Shuffleboard.getTab("PositionTracker")
                .getLayout("List", "Example List")
                .add("Button", false)
                .withWidget("Toggle Button")
                .getEntry();
    }
}
