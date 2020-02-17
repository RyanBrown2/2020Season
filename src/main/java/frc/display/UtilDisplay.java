package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class UtilDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry battery;

    public UtilDisplay() {
       tab = Shuffleboard.getTab("Robot");

       battery =
               tab.add("Battery Voltage", 0)
               .withPosition(0, 0)
               .getEntry();

    }

    public void battery(double battery) {
        this.battery.setDouble(battery);
    }

}
