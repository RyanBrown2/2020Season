package frc.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Constants;

public class ShooterControl {
    private static ShooterControl instance = null;
    public static ShooterControl getInstance() {
        if (instance == null) {
            instance = new ShooterControl();
        }
        return instance;
    }

    PigeonIMU pigeon;

    Flywheel flywheel;
    Hood hood;
    Turret turret;

    NetworkTableInstance tableInstance;
    NetworkTable table;
    NetworkTableEntry turretAngle, distance;

    public enum States {
        disbabled,
        enabled,
        tracking,
        shooting
    }

    private ShooterControl() {
        flywheel = new Flywheel();
        hood = new Hood();
        turret = new Turret();

        pigeon = Constants.Drive.pigeon;

        tableInstance = NetworkTableInstance.getDefault();
        table = tableInstance.getTable("ShooterData");
        turretAngle = table.getEntry("turret");
        distance = table.getEntry("distance");

    }

    // Gets Data From Network Tables
    public double[] getData() {
        return new double[]{turretAngle.getDouble(0), distance.getDouble(0)};
    }

    // Uses the velocity and angle of the robot to create a 3D velocity vector
    public double[] robotVelocity(double vel, double angle) {
        double xVel = Math.cos(angle) * vel;
        double yVel = Math.sin(angle) * vel;
        return new double[]{xVel, yVel, 0};
    }

}
