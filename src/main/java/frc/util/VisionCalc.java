package frc.util;

import com.ctre.phoenix.sensors.PigeonIMU;
import frc.robot.Constants;

import java.io.IOException;

public class VisionCalc {
    private static VisionCalc instance = null;
    public static VisionCalc getInstance() {
        if (instance == null) {
            instance = new VisionCalc();
        }
        return instance;
    }

    PigeonIMU pigeon;

    double angleDiff, visionAngle, currentAngle;

    udpServer visionServer;

    private VisionCalc() {
        try {
            visionServer = new udpServer(5100);
            Thread thread = new Thread(visionServer);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void trackVision() {
        visionAngle = 0 * Constants.degreesToRadians /*TODO*/;
    }

    /*
    Get data from Coprocessor
    The data will a double array formatted as the following:
        [<distance>,<angle>]
    */
    private double[] getGoalData() {
        try {
            return visionServer.getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new double[]{0, 0};
        }
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
//                getGoalData()[0]*getTurretAngleRobot()[0][0] + getGoalData()[1]*getTurretAngleRobot()[1][0],
//                getGoalData()[0]*getTurretAngleRobot()[0][1] + getGoalData()[1]*getTurretAngleRobot()[1][1]
        };
    }

    /*
    Take the data from Network Tables and make it relative to the robot
     */
    public double[] goalPosField() {
        return new double[]{
//                getGoalData()[0]*getTurretAngleField()[0][0] + getGoalData()[1]*getTurretAngleField()[1][0],
//                getGoalData()[0]*getTurretAngleField()[0][1] + getGoalData()[1]*getTurretAngleField()[1][1]
        };
    }

    /*
    Returns turret angle relative to robot
    The angle is represented as a matrix for linear algebra
     */
    public double[][] getTurretAngle(double angle) {
        return new double[][]{
                new double[]{Math.cos(angle), Math.sin(angle)},
                new double[]{-Math.sin(angle), Math.cos(angle)}
        };
    }
}