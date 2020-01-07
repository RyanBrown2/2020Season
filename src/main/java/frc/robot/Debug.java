package frc.robot;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Debug {


    public Debug() {
    }

    public void display() {
        SmartDashboard.putBoolean("Brown Out", RobotController.isBrownedOut());
        SmartDashboard.putNumber("RoboRio", RobotController.getInputVoltage());
    }
}
