package com.team3250.frc2020.subsystems;

import edu.wpi.first.wpilibj.Servo;
import com.team3250.frc2020.display.HoodDisplay;
import com.team3250.frc2020.Constants;

public class Hood {
    private static Hood instance = null;
    public static Hood getInstance() {
        if (instance == null) {
            instance = new Hood();
        }
        return instance;
    }

    Servo hood;
    HoodDisplay hoodDisplay;

    public double setpoint = 180;

    private Hood() {
        hood = new Servo(Constants.Hood.hoodServo);
        hoodDisplay = new HoodDisplay();
    }

    public void run() {
//        setAngle(150*Units.Angle.degrees);
        hood.setAngle(setpoint);
    }

    // 0 degree min, 150 degree max
    public void setAngle(double angle) {
//        double setpoint = angle;
//        setpoint = setpoint + Constants.Hood.angleOffset;
        this.setpoint = angle;
        }

    public void panic() {

    }

    public void display() {
        hoodDisplay.setpoint(setpoint);
        hoodDisplay.angle(hood.getAngle());
    }

}
