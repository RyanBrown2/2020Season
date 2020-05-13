package frc.utilPackage;

public class LinearTransform {

    // Translate a point w/ linear algebra
    public static double[] translate(double[] inputMatrix, double[][] translationMatrix) {
        return new double[]{
                inputMatrix[0]*translationMatrix[0][0] + inputMatrix[1]*translationMatrix[1][0],
                inputMatrix[0]*translationMatrix[0][1] + inputMatrix[1]*translationMatrix[1][1]
        };
    }

    // Create a translation matrix from and angle
    public static double[][] angleToMatrix(double angle) {
        return new double[][]{
                new double[]{Math.cos(angle), Math.sin(angle)},
                new double[]{-Math.sin(angle), Math.cos(angle)}
        };
    }
}
