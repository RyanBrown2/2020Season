package frc.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import frc.display.FlywheelDisplay;
import frc.robot.Constants;

public class Flywheel {
    private static Flywheel instance = null;
    public static Flywheel getInstance() {
        if (instance == null) {
            instance = new Flywheel();
        }
        return instance;
    }
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

    private Flywheel() {
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

        flywheelPID.setP(Constants.Flywheel.kP, 0);
        flywheelPID.setI(Constants.Flywheel.kI, 0);
        flywheelPID.setD(Constants.Flywheel.kD, 0);
        flywheelPID.setIZone(Constants.Flywheel.kIz, 0);
        flywheelPID.setFF(Constants.Flywheel.kFF, 0);
        flywheelPID.setOutputRange(Constants.Flywheel.kMinOutput, Constants.Flywheel.kMaxOutput, 0);

        flywheelPID.setP(Constants.Flywheel.rampkP, 1);
        flywheelPID.setI(Constants.Flywheel.rampkI, 1);
        flywheelPID.setD(Constants.Flywheel.rampkD, 1);
        flywheelPID.setIZone(Constants.Flywheel.rampkIz, 1);
        flywheelPID.setFF(Constants.Flywheel.rampkFF, 1);
        flywheelPID.setOutputRange(Constants.Flywheel.rampMinOutput, Constants.Flywheel.rampMaxOutput, 1);
    }

    public void run() {
        flywheelPID.setOutputRange(velocitySetpoint/473, Constants.Flywheel.rampMaxOutput, 0);

        // Update the PID velocity setpoint constantly
        if(velocitySetpoint == 0) {
            flywheelPID.setReference(velocitySetpoint, ControlType.kVelocity, 1);
        } else if(getVelocity() > velocitySetpoint - 400) {
            flywheelPID.setReference(velocitySetpoint, ControlType.kVelocity, 0);
        } else {
            flywheelPID.setReference(velocitySetpoint, ControlType.kVelocity, 1);
        }
    }

    public void setVelocity(double vel) {
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
        flywheelDisplay.velocity(getVelocity());
        flywheelDisplay.setpoint(velocitySetpoint);
    }
}
