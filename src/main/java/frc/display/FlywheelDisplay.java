package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class FlywheelDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry velocity;
    NetworkTableEntry setpoint;
    NetworkTableEntry current;

    public FlywheelDisplay() {
        tab = Shuffleboard.getTab("Shooter");

        velocity =
                tab.add("Flywheel Velocity", 0)
                .withPosition(0, 2)
                .getEntry();

        setpoint =
                tab.add("Velocity Setpoint", 0)
                .withPosition(1, 2)
                .getEntry();

        current =
                tab.add("Flywheel Current", 0)
                .withPosition(7,0)
                .withSize(2,2)
                .getEntry();
    }

    public void velocity(double velocity) {
        this.velocity.setDouble(velocity);
    }

    public void setpoint(double setpoint) {
        this.setpoint.setDouble(setpoint);
    }

    public void current(double current) {
        this.current.setDouble(current);
    }

}
