package frc.subsystems;

import edu.wpi.first.wpilibj.Servo;
import frc.display.HoodDisplay;
import frc.robot.Constants;
import frc.utilPackage.Units;

public class Hood {

    Servo hood;
    HoodDisplay hoodDisplay;

    public double setpoint = 180;

    public Hood() {
        hood = new Servo(Constants.Hood.hoodServo);
        hoodDisplay = new HoodDisplay();
    }

    public void run() {
//        setAngle(150*Units.Angle.degrees);
        hood.setAngle(setpoint);
    }

    // 0 degree min, 150 degree max
    public void setAngle(double angle) {
        double setpoint = angle / Units.Angle.degrees;
        setpoint = setpoint + Constants.Hood.angleOffset;
        this.setpoint = setpoint;
    }

    public void display() {
        hoodDisplay.setpoint(setpoint);
    }

}
