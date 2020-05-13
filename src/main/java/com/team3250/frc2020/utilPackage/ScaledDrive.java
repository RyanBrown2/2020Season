package com.team3250.frc2020.utilPackage;

import com.team3250.frc2020.drive.DriveOutput;
import com.team3250.frc2020.drive.DriveOutput.Modes;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.team3250.frc2020.Robot;

public class ScaledDrive {
    DriveOutput drive;
    // Increase to make the wheel more aggressive
    double wheelScalar = 0.7 * 2;
    // Increase to make the throttle more aggressive
    double throttleScalar = -0.8;
    // Adjusts how nonlinear the wheel is
    double kWheelNonLinearity = 1;

    boolean enabled = true;

    public ScaledDrive() {
        drive = DriveOutput.getInstance();
    }

    public void enabled(boolean enable){
        enabled = enable;
    }

    public void run(){
        // Get the raw value of the wheel
        double wheel = Robot.getControlBoard().getWheel();
        double x = outputWheel(kWheelNonLinearity, wheel);
        double y = Robot.getControlBoard().getThrottle();
        // Multiply the wheel and throttle values by scalars
        y*= throttleScalar;
        x *= wheelScalar;
        // Convert from percentage to voltage
        double rightVal = 12*(y-x);
        double leftVal = 12*(y+x);
        if(enabled)
            // Set the drivebase voltages
            drive.set(Modes.Voltage, -rightVal, -leftVal);
    }

    // Uses a sine function to scale the wheel such that it stays between -1 and 1 but is no longer linear
    private double outputWheel(double wheelNonLinearity, double wheel) {
        final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        return Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
    }
}

