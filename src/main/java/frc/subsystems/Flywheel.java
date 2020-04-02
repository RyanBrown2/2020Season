package frc.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
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

    private int pidSlot = 1;

    CANSparkMax flywheelMotor, flywheelMotorI;
    CANPIDController flywheelPID;
    CANEncoder flywheelEncoder;

    FlywheelDisplay flywheelDisplay;

    double holdVoltage, rampVoltage;

    private Flywheel() {
        flywheelDisplay = new FlywheelDisplay();

        flywheelMotor = Constants.Flywheel.flywheelMotor;
        flywheelMotorI = Constants.Flywheel.flywheelMotorI;
        flywheelMotor.restoreFactoryDefaults();
        flywheelMotorI.restoreFactoryDefaults();
        flywheelMotor.setSmartCurrentLimit(45);
        flywheelMotorI.setSmartCurrentLimit(45);

        flywheelMotorI.follow(flywheelMotor,true);

        flywheelEncoder = flywheelMotor.getEncoder();

        flywheelPID = flywheelMotor.getPIDController();
        flywheelPID.setFeedbackDevice(flywheelEncoder);

        flywheelPID.setP(Constants.Flywheel.kPSpooling, 1);
        flywheelPID.setI(Constants.Flywheel.kISpooling, 1);
        flywheelPID.setD(Constants.Flywheel.kDSpooling, 1);
        flywheelPID.setIZone(Constants.Flywheel.kIzSpooling, 1);
        flywheelPID.setOutputRange(Constants.Flywheel.kMinOutput, Constants.Flywheel.kMaxOutput, 1);

        flywheelPID.setP(Constants.Flywheel.kP, 2);
        flywheelPID.setI(Constants.Flywheel.kI, 2);
        flywheelPID.setD(Constants.Flywheel.kD, 2);
        flywheelPID.setIZone(Constants.Flywheel.kIz, 2);
        flywheelPID.setFF(Constants.Flywheel.kFF, 2);
        flywheelPID.setOutputRange(Constants.Flywheel.kMinOutput, Constants.Flywheel.kMaxOutput, 2);
    }

    public boolean jam = false;

    public void run() {
//        if (!jam) {
//             Update the PID velocity setpoint constantly
//            flywheelDisplay.setpoint(velocitySetpoint);
//            if (velocitySetpoint == 0) {
//                flywheelPID.setReference(velocitySetpoint, ControlType.kVelocity, pidSlot);
//            } else {
//                flywheelPID.setReference(velocitySetpoint + 150, ControlType.kVelocity, pidSlot);
//            }
//        } else {
//            flywheelMotor.setVoltage(-4);
//        }
//        flywheelMotor.set(10);
        error = velocitySetpoint - getVelocity();

        motorPower += gain * error;
        if (motorPower > 1) {
            motorPower = 1;
        } else if (motorPower < -1) {
            motorPower = -1;
        }

        if ((lastError > 0) != (error > 0)) {
            motorPower = 0.5 * (motorPower + tbh);
            tbh = motorPower;
            lastError = error;
        }

        flywheelMotor.set(motorPower);

    }

    public void setVelocity(double vel) {
        if (velocitySetpoint < vel) {
            lastError = 1;
        } else if (velocitySetpoint > vel) {
            lastError = -1;
        }
        tbh = (2*(vel/Constants.Flywheel.maxRPM)) -1;
        velocitySetpoint = vel;
    }

    public void setSlot(int slotNum) {
        pidSlot = slotNum;
    }

    public double getVelocity() {
        return flywheelEncoder.getVelocity();
    }

    public void panic() {
    }

    public void display() {
        flywheelDisplay.velocity(getVelocity());
        flywheelDisplay.setpoint(velocitySetpoint);
        flywheelDisplay.current(flywheelMotor.getOutputCurrent());
    }
}
