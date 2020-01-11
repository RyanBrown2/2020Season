package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.drive.Drive;
import frc.drive.PositionTracker;

public class Display {

    public ShuffleboardTab sparks= Shuffleboard.getTab("Sparks");

    public Display() {
    }

    // Run this initially
//    public class init() {}

    // Run this periodically
//    public void periodic() {
//        Drive.getInstance().display();
//        PositionTracker.getInstance().display();
//        driveDisplay();
//    }

    // Run this during auto
//    public class auto() {}

    // Run this during teleop
//    public class tele() {}

    //Displays Data For Drivebase
//    public void driveDisplayInit() {
//        sparks.add("Left Drive", Constants.Drive.left1.get());
//        sparks.add("Right Drive", Constants.Drive.right1.get());
//    }

    public void driveDisplay() {

    }

}
