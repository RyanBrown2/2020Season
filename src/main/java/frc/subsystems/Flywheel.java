package frc.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import frc.display.FlywheelDisplay;
import frc.robot.Constants;

public class Flywheel {
    double velocitySetpoint;

    CANSparkMax flywheelMotor, flywheelMotorI;
    CANPIDController flywheelPID;
    CANEncoder flywheelEncoder;

    FlywheelDisplay flywheelDisplay;

    public Flywheel() {
        flywheelDisplay = new FlywheelDisplay();

        flywheelMotor = Constants.Flywheel.flywheelMotor;
        flywheelMotorI = Constants.Flywheel.flywheelMotorI;
        flywheelMotor.restoreFactoryDefaults();
        flywheelMotorI.restoreFactoryDefaults();
        flywheelMotor.setSmartCurrentLimit(80);
        flywheelMotorI.setSmartCurrentLimit(80);

        flywheelMotorI.follow(flywheelMotor, true);

        flywheelEncoder = flywheelMotor.getEncoder();

        flywheelPID = flywheelMotor.getPIDController();
        flywheelPID.setFeedbackDevice(flywheelEncoder);

        flywheelPID.setP(Constants.Flywheel.kP);
        flywheelPID.setI(Constants.Flywheel.kI);
        flywheelPID.setD(Constants.Flywheel.kD);
        flywheelPID.setIZone(Constants.Flywheel.kIz);
        flywheelPID.setFF(Constants.Flywheel.kFF);
        flywheelPID.setOutputRange(Constants.Flywheel.kMinOutput, Constants.Flywheel.kMaxOutput);
    }

    public void run() {
        flywheelPID.setReference(velocitySetpoint, ControlType.kVelocity);
        flywheelMotorI.setVoltage(-flywheelMotor.getAppliedOutput());
//        if(flywheelMotorI.getAppliedOutput() == -flywheelMotor.getAppliedOutput()) {
//            //good
//        }
//        else {
//            flywheelPID.setP(0);
//            flywheelMotor.setVoltage(0);
//            flywheelMotorI.setVoltage(0);
//        }
    }

    public void setVelocity(double vel) {
        velocitySetpoint = vel;
    }

    public void display() {
        flywheelDisplay.setVelocity(flywheelEncoder.getVelocity());
        flywheelDisplay.setpoint(velocitySetpoint);
    }
}
