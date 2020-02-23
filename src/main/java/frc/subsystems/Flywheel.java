package frc.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import frc.display.FlywheelDisplay;
import frc.robot.Constants;

public class Flywheel {
    double velocitySetpoint;
    double error;
    double tbh = 0;
    double lastError = 10;
    double motorPower = 0;
    double gain = 0.00001;

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
    }

    public void run() {
        // Update the PID velocity setpoint constantly
        flywheelPID.setReference(velocitySetpoint, ControlType.kVelocity);
    }

    public void setVelocity(double vel) {
        //Set up values for optimized spinup to the target
        if(velocitySetpoint < vel) {
            lastError = 1;
        }
        else if(velocitySetpoint > vel) {
            lastError = -1;
        }
        tbh = (2 * (vel / Constants.Flywheel.maxRPM)) - 1;

        velocitySetpoint = vel;
    }

    public double getVelocity() {
        return flywheelEncoder.getVelocity();
    }

    public void tbhControl() {
        error = velocitySetpoint - getVelocity();
        motorPower += gain * error;
        motorPower = clamp(motorPower);

        if (isPositive(lastError) != isPositive(error)) {
            motorPower = 0.5 * (motorPower + tbh);
            tbh = motorPower;

            lastError = error;
        }

        flywheelMotor.setVoltage(motorPower*12);
    }

    public boolean isPositive(double input) {
        return input > 0;
    }

    public double clamp(double input) {
        if (input > 1) {
            return 1;
        }
        if (input < -1) {
            return -1;
        }
        return input;
    }

    public void panic() {
    }

    public void display() {
    }
}
