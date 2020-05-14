package com.team3250.frc2020.utilPackage;

import com.team3250.frc2020.Constants;

public class SerialVision {

    SerialReader serialReader;

    double radPerPixel;

    public SerialVision(int com) {
        serialReader = new SerialReader(com);
        radPerPixel = Constants.Camera.horizontalFov / Constants.Image.imageWidth;
    }

    public boolean targetFound() {
        if (!(serialReader.readLine() == null)) {
            return true;
        } else {
            return false;
        }
    }

    public double[] getSerialData() {
        String serialData = serialReader.readLine();
        String[] stringData = serialData.split(", ");
        Double x = Double.parseDouble(stringData[0]);
        Double y = Double.parseDouble(stringData[1]);
        return new double[] {x,y};
    }

    public double getTargetAngle() {
        return getSerialData()[0]*radPerPixel;
    }

    public double getTargetDistance() {
        double verticalTargetAngle = getSerialData()[1]*radPerPixel + Constants.Camera.cameraMountAngle;
        return Constants.Camera.heightDiff / Math.tan(verticalTargetAngle);
    }

    public double[] targetCoord(double turretAngle) {
        double dist = getTargetDistance();
        double[][] transMatrix = angleToMatrix(turretAngle);
        double[] coord = translate(new double[]{0,dist}, transMatrix);
        /* todo
            point before transformation is (0, <distance to target>)
            might have to change it to (<distance to target>, 0)
        */
        return coord;
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

}
