package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class FlywheelDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry velocity;
    NetworkTableEntry setpoint;

    public FlywheelDisplay() {
        tab = Shuffleboard.getTab("Shooter");

        velocity =
                tab.add("Flywheel Velocity", 0)
                .withPosition(0, 2)
                .getEntry();

//        setpoint =
//                tab.add("Velocity Setpoint", 0)
//                .withPosition(1, 2)
//                .getEntry();
    }

    public void setVelocity(double velocity) {
        this.velocity.setDouble(velocity);
    }

    public void setpoint(double setpoint) {
        this.setpoint.setDouble(setpoint);
    }

}
