package frc.util;

public class util {
    public static double getMagnitude(double[] coord) {
        double mag = Math.sqrt(Math.pow(coord[0], 2) + Math.pow(coord[1], 2));
        return mag;
    }

    public static double map(double x, double x1, double x2, double y1, double y2) {
        double slope = (y2 - y1) / (x2 - x1);

        return slope * (x - x1) + y1;
    }

    public static double limit(double v, double maxMagnitude) {
        return limit(v, -maxMagnitude, maxMagnitude);
    }

    public static double limit(double v, double min, double max) {
        return Math.min(max, Math.max(min, v));
    }

    public static double interpolate(double a, double b, double x) {
        x = limit(x, 0.0, 1.0);
        return a + (b - a) * x;
    }
}