package frc.display;

public class ShuffleboardDisplay {
    DriveDisplay driveDisplay;

    public ShuffleboardDisplay() {
        driveDisplay = new DriveDisplay();
    }

    public void run() {
        driveDisplay.PostionTrackerDisplay();
    }



}
