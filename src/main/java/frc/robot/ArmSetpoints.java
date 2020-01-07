package frc.robot;

import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.utilPackage.Units;

public class ArmSetpoints {
    private static IControlBoard cb = new ControlBoard();
    public static IControlBoard getControlBoard(){
        return cb;
    }

    private static boolean ballMode() {
        return cb.ballMode();
    }

    public static class hatch {
        public static double[] highHatch = { 23.6, 20.1, 0.65 };
        public static double[] midHatch = { 26, 2.8, 0 };
        public static double[] lowHatch = { 10.7, -26, 0 };
    }

    public static class ball {
        public static double[] highBall = { 23.3, 27.3, 0.846};
        public static double[] midBall = { 22.3, 11.5, 0};
        public static double[] lowBall = { 20, -15, 0};
        public static double[] reset = { 1, -26.1, 0};
        public static double[] cargoship = { 26.1, 3.6, -0.4};
        public static double[] ballPickup = { 18, -24.4, -1.124 };
    }

    public static class setpoints {
        public static double[] climbClear = { 27, 3, 0};
        public static double[] climbDown = {21.18, -14, 0};

        public static double[] high() {
            double[] setpoint;
            if (ballMode()) {
                setpoint = ball.highBall;
            } else {
                setpoint = hatch.highHatch;
            }
            return setpoint;
        }

        public static double[] mid() {
            double [] setpoint;
            if (ballMode()) {
                setpoint = ball.midBall;
            } else {
                setpoint = hatch.midHatch;
            }
            return setpoint;
        }

        public static double[] low() {
            double[] setpoint;
            if (ballMode()) {
                setpoint = ball.lowBall;
            } else {
                setpoint = hatch.lowHatch;
            }
            return setpoint;
        }

        public static double[] reset() {
            double[] setpoint;
            if (ballMode()) {
                setpoint = ball.reset;
            } else {
                setpoint = ball.reset;
            }
            return setpoint;
        }
    }
}