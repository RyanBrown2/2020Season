package frc.util;

import com.ctre.phoenix.sensors.PigeonIMU;
import frc.utilPackage.Units;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    Map<Double, double[]> map;

    private Vision() {
        map = new HashMap<>();
        try {
            visionServer = new udpServer(5100);
            Thread thread = new Thread(visionServer);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[][] data = {
            new double[]{7, 3900, 125},
            new double[]{7.3, 3900, 125},
            new double[]{7.6, 3900, 125},
            new double[]{8, 3900, 125},
            new double[]{8.3, 3900, 125},
            new double[]{8.6, 3900, 125},
            new double[]{9, 3900, 120},
            new double[]{9.3, 3900, 108},
            new double[]{9.6, 3900, 98},
            new double[]{10, 3900, 90},
            new double[]{10.3, 3900, 85},
            new double[]{10.6, 3900, 80},
            new double[]{11, 3900, 80},
            new double[]{11.3, 3900, 80},
            new double[]{11.6, 3900, 68},
            new double[]{12, 4500, 30},
            new double[]{12.3, 4500, 30},
            new double[]{12.6, 4500, 30},
            new double[]{13, 4500, 30},
            new double[]{13.3, 4500, 30},
            new double[]{12.6, 4500, 30},
            new double[]{13, 4500, 30},
            new double[]{13.3, 4500, 30},
            new double[]{13.6, 4500, 30},
            new double[]{14, 4500, 40},
            new double[]{14.5, 4500, 30},
            new double[]{15, 4500, 30},
            new double[]{15.5, 4000, 30},
            new double[]{16, 4000, 30},
            new double[]{16.5, 4000, 30},
            new double[]{17, 3800, 30},
            new double[]{17.5, 3800, 30},
            new double[]{18, 3800, 30},
            new double[]{18.5, 3800, 30},
            new double[]{19, 3900, 30},
            new double[]{19.5, 4000, 30},
            new double[]{20, 4000, 30},
            new double[]{20.5, 4000, 33},
            new double[]{21, 4100, 33}
    };

    public double[] dataLookUp(double distance) {
        double[][] distanceData = new double[0][];
        for (int i = 0; i < data.length; i++) {
            double error = Math.abs(distance - data[i][0]);
            distanceData[i][0] = error;
            distanceData[i][1] = i;
        }

        double minValue = distanceData[0][0];
        for (int j = 0; j < distanceData.length; j++) {
                if (distanceData[j][0] < minValue ) {
                    minValue = distanceData[j][1];
                }
        }

        double hoodAngle = data[(int)minValue][2];
        double rpm = data[(int)minValue][1];

        return new double[]{rpm, hoodAngle};
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