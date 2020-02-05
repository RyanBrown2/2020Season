package frc.display;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class HoodDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry hoodSetpoint;

    public HoodDisplay() {
        tab = Shuffleboard.getTab("Shooter");

        hoodSetpoint =
                tab.add("Hood Setpoint", 0)
                .withPosition(1, 1)
                .getEntry();
    }

    public void setpoint(double setpoint) {
        hoodSetpoint.setDouble(setpoint);
    }
}
