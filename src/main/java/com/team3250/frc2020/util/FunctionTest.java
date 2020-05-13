package com.team3250.frc2020.util;

/*

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import com.team3250.frc2020.drive.Drive;
import com.team3250.frc2020.Constants;
import com.team3250.frc2020.subsystems.Hood;

//runs drivetrain, flywheel, turret (tested)
// maybe feeder and transport, and hood (not tested yet)

public class FunctionTest {

//    Hood hood = new Hood();
//    Timer timer = new Timer();
    //time is in seconds
    int state = 1;

    public FunctionTest() {
//        timer.reset();
//        timer.start();
    }

    public void reset() {
        state = 1;
    }

    public void run() {

        /*
        Display Following variables:
            Drive.getInstance().getLeftPosition()
            Drive.getInstance().getRightPosition()
            Constants.Flywheel.flywheelMotor.getEncoder().getPosition()
            Constants.Flywheel.flywheelMotor.getEncoder().getVelocity()
            Constants.Turret.turretEnc.getSelectedSensorPosition()
            Constants.Feeder.rollerMotor.getSelectedSensorPosition();
            Constants.Transport.rampFront.getSelectedSensorPosition();
            Constants.Transport.rampBack.getSelectedSensorPosition();
            Constants.Transport.mixer.getSelectedSensorPosition();


            // not sure what to display for hood
        switch (state) {
            case 1:
                Drive.getInstance().outputToDrive(-0.25, 0.25);
//                Constants.Drive.left1.set(-0.5);
//                Constants.Drive.left2.set(-0.5);
//                Constants.Drive.right1.set(0.5);
//                Constants.Drive.right2.set(0.5);
                if (timer.get() >= 5) {
                    Constants.Drive.left1.set(0);
                    Constants.Drive.left2.set(0);
                    Constants.Drive.right1.set(0);
                    Constants.Drive.right2.set(0);
                    timer.reset();
                    state = 2;
                }
                break;
            case 2:
                Constants.Drive.left1.set(0.5);
                Constants.Drive.left2.set(0.5);
                Constants.Drive.right1.set(-0.5);
                Constants.Drive.right2.set(-0.5);
                if (timer.get() >= 5) {
                    Constants.Drive.left1.set(0);
                    Constants.Drive.left2.set(0);
                    Constants.Drive.right1.set(0);
                    Constants.Drive.right2.set(0);
                    timer.reset();
                    state = 3;
                }
                break;
            case 3:
                Constants.Flywheel.flywheelMotor.set(0.1);
                Constants.Flywheel.flywheelMotorI.set(0.1);
                if (timer.get() >= 5) {
                    Constants.Flywheel.flywheelMotor.set(0);
                    Constants.Flywheel.flywheelMotorI.set(0);
                    timer.reset();
                    state = 4;
                }
                break;
            case 4:
                Constants.Turret.turret.set(0.05);
                if (timer.get() >= 2){
                    Constants.Turret.turret.set(0);
                    timer.reset();
                    state = 5;
                }
                break;
            case 5:
                Constants.Turret.turret.set(-0.05);
                if (timer.get() >= 2){
                    Constants.Turret.turret.set(0);
                    timer.reset();
                    state = 6;
                }
                break;
            case 6:
                //todo
//                Constants.Feeder.leftSolenoid.set(DoubleSolenoid.Value.kForward);
//                Constants.Feeder.rightSolenoid.set(DoubleSolenoid.Value.kForward);
                Constants.Feeder.solenoid.set(DoubleSolenoid.Value.kForward);
                if (timer.get() >= 5){
                    timer.reset();
                    state = 7;
                }
                break;
            case 7:
                //todo
//                Constants.Feeder.leftSolenoid.set(DoubleSolenoid.Value.kReverse);
//                Constants.Feeder.rightSolenoid.set(DoubleSolenoid.Value.kReverse);
                if (timer.get() >= 5) {
//                    Constants.Feeder.leftSolenoid.set(DoubleSolenoid.Value.kOff);
//                    Constants.Feeder.rightSolenoid.set(DoubleSolenoid.Value.kOff);
                    Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 0.25);
                }
                if (timer.get() >=2){
                    Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 0);
                    timer.reset();
                    state = 8;
                }
            case 8:
                //todo
//                Constants.Transport.rampLeft.set(ControlMode.PercentOutput, 50);
//                Constants.Transport.rampRight.set(ControlMode.PercentOutput, 50);
                if(timer.get() >= 5) {
//                    Constants.Transport.rampLeft.set(ControlMode.PercentOutput, 0);
//                    Constants.Transport.rampRight.set(ControlMode.PercentOutput, 0);
                    Constants.Feeder.solenoid.set(DoubleSolenoid.Value.kReverse);
                }
                if (timer.get() >= 5){
                    Constants.Feeder.solenoid.set(DoubleSolenoid.Value.kOff);
                    timer.reset();
                    state = 9;
                }
                break;
            case 9:
                Constants.Transport.rampBack.set(ControlMode.PercentOutput, 50);
                Constants.Transport.rampFront.set(ControlMode.PercentOutput, 50);
                Constants.Transport.mixer.set(ControlMode.PercentOutput, 50);
                if(timer.get() >= 5){
                    Constants.Transport.rampBack.set(ControlMode.PercentOutput, 0);
                    Constants.Transport.rampFront.set(ControlMode.PercentOutput, 0);
                    Constants.Transport.mixer.set(ControlMode.PercentOutput, 0);
                    timer.reset();
                    state = 10;
                }
                break;
            case 10:
                hood.setAngle(1);
                state = 11;
                break;
            case 11:
                hood.setAngle(149);
                state = 12;
                break;
            case 12:
                timer.stop();
                break;
        }

    }

}*/
