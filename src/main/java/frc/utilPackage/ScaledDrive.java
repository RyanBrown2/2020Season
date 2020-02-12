package frc.utilPackage;

import frc.drive.DriveOutput;
import frc.drive.DriveOutput.Modes;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class ScaledDrive {
    DriveOutput drive;
    double wheelScalar = 0.7;
    double throttleScalar = 0.8;
    double kWheelNonLinearity = 1;

    boolean enabled = true;

    public ScaledDrive() {
        drive = DriveOutput.getInstance();
//        SmartDashboard.putNumber("Wheel Linearity", kWheelNonLinearity);
    }

    public void enabled(boolean enable){
        enabled = enable;
    }

    public void run(){
        double wheel = Robot.getControlBoard().getWheel();
        double x = outputWheel(kWheelNonLinearity, wheel);
        double y = Robot.getControlBoard().getThrottle();
        y*= throttleScalar;
        x *= wheelScalar;
        double rightVal = 12*(y-x);
        double leftVal = 12*(y+x);
        if(enabled)
            drive.set(Modes.Voltage, -rightVal, -leftVal);
    }

    private double outputWheel(double wheelNonLinearity, double wheel){
        final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        return Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
    }
}

