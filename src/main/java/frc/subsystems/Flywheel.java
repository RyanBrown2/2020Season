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
        flywheelMotor.setSmartCurrentLimit(40);
        flywheelMotorI.setSmartCurrentLimit(40);


        flywheelEncoder = flywheelMotor.getEncoder();

        flywheelPID = flywheelMotor.getPIDController();
        flywheelPID.setFeedbackDevice(flywheelEncoder);

        flywheelPID.setP(Constants.Flywheel.kP);
        flywheelPID.setI(Constants.Flywheel.kI);
        flywheelPID.setD(Constants.Flywheel.kD);
        flywheelPID.setIZone(Constants.Flywheel.kIz);
        flywheelPID.setFF(Constants.Flywheel.kFF);
        flywheelPID.setOutputRange(Constants.Flywheel.kMinOutput, Constants.Flywheel.kMaxOutput);

//        flywheelPID.setSmartMotionMinOutputVelocity(Constants.Flywheel.minVel, 0);
//        flywheelPID.setSmartMotionAccelStrategy(CANPIDController.AccelStrategy.kSCurve, 0);
//        flywheelPID.setSmartMotionAllowedClosedLoopError(Constants.Flywheel.allowedErr, 0);
//        flywheelPID.setSmartMotionMaxVelocity(Constants.Flywheel.maxRPM, 0);
    }

    public void run() {
        // Update the PID velocity setpoint constantly
        flywheelPID.setReference(velocitySetpoint, ControlType.kVelocity);
    }

    public void setVelocity(double vel) {
        velocitySetpoint = vel;
    }

    public double getVelocity() {
        return flywheelEncoder.getVelocity();
    }

    public void panic() {
    }

    public void display() {
    }
}
