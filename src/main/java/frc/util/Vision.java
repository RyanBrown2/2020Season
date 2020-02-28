package frc.util;

import com.ctre.phoenix.sensors.PigeonIMU;
import frc.utilPackage.Units;

import java.io.IOException;

public class Vision {
    private static Vision instance = null;
    public static Vision getInstance() {
        if (instance == null) {
            instance = new Vision();
        }
        return instance;
    }

    PigeonIMU pigeon;

    double angleDiff, visionAngle, currentAngle;

    udpServer visionServer;

    private Vision() {
        try {
            visionServer = new udpServer(5100);
            Thread thread = new Thread(visionServer);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /*
    Get data from Coprocessor
    The data will a double array formatted as the following:
        [<distance>,<angle>]
    */
    public double[] getTargetData() {
        try {
            return visionServer.getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new double[]{0, 0};
        }
    }

    // Get angle from vision
    public double getAngle() {
        return getTargetData()[1] * Units.Angle.degrees;
    }

    // Get distance from vision
    public double getDistance() {
        return getTargetData()[0];
    }

    public double offsetAngle(double angle, double offset) {
        return angle - offset;
    }

    // Uses the velocity and angle of the robot to create a 3D velocity vector
    public double[] robotVelocity(double vel, double angle) {
        double xVel = Math.cos(angle) * vel;
        double yVel = Math.sin(angle) * vel;
        return new double[]{xVel, yVel, 0};
    }

    /*
    Transform a point with linear algebra
     */
    public double[] translate(double[] point, double[][] translationMatrix) {
        return new double[]{
                point[0]*translationMatrix[0][0] + point[1]*translationMatrix[1][0],
                point[0]*translationMatrix[0][1] + point[1]*translationMatrix[1][1]
        };
    }

    /*
    This function is used to convert an angle to a transformation matrix
    This is useful for linear algebra
     */
    public double[][] angleToMatrix(double angle) {
        return new double[][]{
                new double[]{Math.cos(angle), Math.sin(angle)},
                new double[]{-Math.sin(angle), Math.cos(angle)}
        };
    }

    // DO NOT RUN THIS IN AN INFINITE LOOP
    public void display() {

    }
}