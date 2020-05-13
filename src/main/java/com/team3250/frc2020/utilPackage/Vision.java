package com.team3250.frc2020.utilPackage;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;
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

    private double[][] teleopOffsets = {
            new double[]{11, 12},
            new double[]{11.7, 13},
            new double[]{12.4, 14},
            new double[]{13.5, 15},
            new double[]{14.5, 16},
            new double[]{15.3, 17},
            new double[]{16.4, 18},
            new double[]{17.2, 19},
            new double[]{17.6, 20},
            new double[]{18.4, 20.3},
            new double[]{18.7, 21},
            new double[]{19.2, 22},
            new double[]{20.1, 23},
            new double[]{21, 24},
            new double[]{21.8, 25}
    };

    public double[] dataLookUp(double distance) {
//        distance = offsetLookUp(distance);
        double minError = 30;
        int selectedIndex = 0;
        for (int i = 0; i < data.length; i++) {
            double error = Math.abs(distance - data[i][0]);
            if (error < minError) {
                minError = error;
                selectedIndex = i;
            }
        }
        return new double[]{data[selectedIndex][1], data[selectedIndex][2]};
    }

//    public double offsetLookUp(double distance) {
//        double minError = 30;
//        int selectedIndex = 0;
//        for (int i = 0; i < teleopOffsets.length; i++) {
//            double error = Math.abs(distance - teleopOffsets[i][0]);
//            if (error < minError) {
//                minError = error;
//                selectedIndex = i;
//            }
//        }
//        return teleopOffsets[selectedIndex][1];
//    }

    public double offsetLookUp(double distance) {
        return (1.186218*distance) - 1.055414;
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

    public boolean cameraTracking() {
        try {
            visionServer.getData();
            return true;
        } catch (InterruptedException e) {
            return false;
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

    public double trackPoint(double[] trackPoint, double[] origin) {
        double x = trackPoint[0] - origin[0];
        double y = trackPoint[1] - origin[1];
        return Math.atan2(y, x);
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

    public void display() {
        SmartDashboard.putNumber("Vision Distance Raw", getDistance());
        SmartDashboard.putNumber("Vision Distance Adjusted",offsetLookUp(getDistance()));
    }
}