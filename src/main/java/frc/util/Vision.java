package frc.util;

import com.ctre.phoenix.sensors.PigeonIMU;
import frc.utilPackage.Units;

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

    public void loadData() {
        map.put((double) 7, new double[]{3900, 125});
        map.put(7.3, new double[]{3900, 125});
        map.put(7.6, new double[]{3900, 125});
        map.put((double) 8, new double[]{3900, 125});
        map.put(8.3, new double[]{3900, 125});
        map.put(8.6, new double[]{3900, 125});
        map.put((double) 9, new double[]{3900, 120});
        map.put(9.3, new double[]{3900, 108});
        map.put(9.6, new double[]{3900, 98});
        map.put((double) 10, new double[]{3900, 90});
        map.put(10.3, new double[]{3900, 85});
        map.put(10.6, new double[]{3900, 80});
        map.put((double) 11, new double[]{3900, 80});
        map.put(11.3, new double[]{3900, 80});
        map.put(11.6, new double[]{3900, 68});
        map.put((double) 12, new double[]{4500, 30});
        map.put(12.3, new double[]{4500, 30});
        map.put(12.6, new double[]{4500, 30});
        map.put((double) 13, new double[]{4500, 30});
        map.put(13.3, new double[]{4500, 30});
        map.put(13.6, new double[]{4500, 30});
        map.put((double) 14, new double[]{4500, 40});
        map.put(14.3, new double[]{4500, 30});
        map.put(14.6, new double[]{4500, 30});
        map.put((double) 15, new double[]{4500, 30});
        map.put(15.3, new double[]{4000, 30});
        map.put(15.6, new double[]{4000, 30});
        map.put((double) 16, new double[]{4000, 30});
        map.put(16.5, new double[]{4000, 30});
        map.put((double) 17, new double[]{3800, 30});
        map.put(17.5, new double[]{3800, 30});
        map.put((double) 18, new double[]{3800, 30});
        map.put(18.5, new double[]{3800, 30});
        map.put((double) 19, new double[]{3900, 30});
        map.put(19.5, new double[]{4000, 30});
        map.put((double) 20, new double[]{4000, 30});
        map.put(20.5, new double[]{4000, 33});
        map.put((double) 21, new double[]{4100, 33});

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