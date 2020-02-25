package frc.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.display.ShooterDisplay;
import frc.robot.Constants;
import frc.robot.Robot;

public class ShooterControl {
    private static ShooterControl instance = null;
    public static ShooterControl getInstance() {
        if (instance == null) {
            instance = new ShooterControl();
        }
        return instance;
    }

    PigeonIMU pigeon;

    ShooterDisplay shooterDisplay;

    double angleDiff, visionAngle, currentAngle;
    double flyWheelRpm;

    NetworkTableInstance tableInstance;
    NetworkTable table;
    NetworkTableEntry angle, distance;

    Flywheel flywheel;
    Hood hood;
    Turret turret;

    public enum States {
        tracking,
        spooling,
        shooting
    }

    States state;

    Boolean enabled, panicMode;

    private ShooterControl() {
        pigeon = Constants.Drive.pigeon;

        shooterDisplay = new ShooterDisplay();

        flywheel = Robot.flywheel;
        hood = Robot.hood;
        turret = Robot.turret;

        tableInstance = NetworkTableInstance.getDefault();
        table = tableInstance.getTable("VisionData");
        angle = table.getEntry("angle");
        distance = table.getEntry("distance");

        enabled = false;
        panicMode = false;

        state = States.tracking;

    }

    public void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    public void run() {
        if (enabled && !panicMode) {
            turret.run();
            switch (state) {
                case tracking:
                    trackVision();
                    if(turret.atSetpoint()) {
                        state = States.spooling;
                    }
                    break;
                case spooling:
                    flywheel.setVelocity(flyWheelRpm);

                    if(Math.abs(flywheel.getVelocity() - flyWheelRpm) < 200) {
                        state = States.shooting;
                    }
                    break;
                case shooting:
                    if (!turret.atSetpoint()) {
                        state = States.tracking;
                    }
                    break;
            }

//            turret.run();
//            flywheel.run();
        } else {
            setFlywheel(0);
            flywheel.setVelocity(0);
        }
    }

    public void setFlywheel(double rpm) {
        flyWheelRpm = rpm;
    }

    public void trackVision() {
        visionAngle = getAngle() * Constants.degreesToRadians;
        currentAngle = Robot.turret.getAngle(true);
        angleDiff = currentAngle - visionAngle;
        turret.toSetpoint(angleDiff);
    }

    /*
    Get data from NetworkTables
    The data should be a 3D vector that represents the position of
    the goal, relative to where the turret is facing.
     */
    private double getAngle() {
        return angle.getDouble(0);
    }

    private double getDistance() {
        return distance.getDouble(0);
    }

    private double[] getGoalData() {
        return new double[]{0,0,0};
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
        double angle = Robot.turret.getAngle(false);
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
        double angle = Robot.turret.getAngle(true);
        return new double[][]{
                new double[]{Math.cos(angle), Math.sin(angle)},
                new double[]{-Math.sin(angle), Math.cos(angle)}
        };
    }

    public void display() {
        shooterDisplay.angle(getAngle());
        shooterDisplay.distance(getDistance());
    }
}