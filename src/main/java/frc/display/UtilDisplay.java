package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import java.util.Map;

public class UtilDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry battery, hoodAngle, flywheelRpm;
    NetworkTableEntry button;

    public UtilDisplay() {
       tab = Shuffleboard.getTab("Robot");

       battery =
               tab.add("Battery Voltage", 0)
               .withPosition(0, 0)
               .getEntry();

       hoodAngle =
               tab.add("Flywheel Target Speed", 0)
               .getEntry();

       flywheelRpm =
               tab.add("Hood Servo Angle", 0)
               .getEntry();

       button =
               tab.add("Update", false)
               .withWidget("Boolean Box")
               .withProperties(Map.of("colorWhenTrue", "green", "colorWhenFalse", "maroon"))
               .getEntry();

    }

    public void battery(double battery) {
        this.battery.setDouble(battery);
    }

    public double getFlywheel() {
        return flywheelRpm.getDouble(0);
    }

    public double getHood() {
        return hoodAngle.getDouble(0);
    }

    public boolean update() {
        return button.getBoolean(false);
    }

}
