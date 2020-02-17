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
    NetworkTableEntry goalPosition;

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
        goalPosition = table.getEntry("goalPosition");

    }

    /*
    Get data from NetworkTables
    The data should be a 3D vector that represents the position of
    the goal, relative to where the turret is facing.
     */
    private double[] getGoalData() {
        return goalPosition.getDoubleArray(new double[]{0,0,0});
    }

    // Uses the velocity and angle of the robot to create a 3D velocity vector
    public double[] robotVelocity(double vel, double angle) {
        double xVel = Math.cos(angle) * vel;
        double yVel = Math.sin(angle) * vel;
        return new double[]{xVel, yVel, 0};
    }

    /*
    Take the data from Network Tables and make it relative to the robot
     */
    public double[] goalPosRobot() {
        return new double[]{
                getGoalData()[0]*getTurretAngleRobot()[0][0] + getGoalData()[1]*getTurretAngleRobot()[1][0],
                getGoalData()[0]*getTurretAngleRobot()[0][1] + getGoalData()[1]*getTurretAngleRobot()[1][1]
        };
    }

    /*
    Take the data from Network Tables and make it relative to the robot
     */
    public double[] goalPosField() {
        return new double[]{
                getGoalData()[0]*getTurretAngleField()[0][0] + getGoalData()[1]*getTurretAngleField()[1][0],
                getGoalData()[0]*getTurretAngleField()[0][1] + getGoalData()[1]*getTurretAngleField()[1][1]
        };
    }

    /*
    Returns turret angle relative to robot
    The angle is represented as a matrix for linear algebra
     */
    public double[][] getTurretAngleRobot() {
        double angle = turret.getAngle(false);
        return new double[][]{
                new double[]{Math.cos(angle), Math.sin(angle)},
                new double[]{-Math.sin(angle), Math.cos(angle)}
        };
    }

    /*
    Returns turret angle relative to field
    The angle is represented as a matrix for linear algebra
     */
    public double[][] getTurretAngleField() {
         double angle = turret.getAngle(true);
         return new double[][]{
                 new double[]{Math.cos(angle), Math.sin(angle)},
                 new double[]{-Math.sin(angle), Math.cos(angle)}
         };
    }

}
