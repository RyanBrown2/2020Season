package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import frc.display.TurretDisplay;
import frc.robot.Constants;
import frc.utilPackage.Units;

public class Turret {
    TurretDisplay turretDisplay;
    CANSparkMax turretMotor;
    CANPIDController turretPID;
    CANEncoder turretEncoder;
    public int smartMotionSlot;
    double setPoint;
    double[] ypr = new double[3];

    public Turret() {
        turretMotor = Constants.Turret.turret;
        turretDisplay = new TurretDisplay();
        turretPID = turretMotor.getPIDController();
        turretEncoder = turretMotor.getAlternateEncoder();

        turretPID.setFeedbackDevice(turretEncoder);

        Constants.Turret.turretEnc.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        Constants.Turret.turretEnc.setSelectedSensorPosition(0);

        turretEncoder.setPositionConversionFactor(1);

        turretMotor.restoreFactoryDefaults();

        smartMotionSlot = 0;

        turretPID.setP(Constants.Turret.kP);
        turretPID.setI(Constants.Turret.kI);
        turretPID.setD(Constants.Turret.kD);
        turretPID.setIZone(Constants.Turret.kIz);
        turretPID.setFF(Constants.Turret.kFF);
        turretPID.setOutputRange(Constants.Turret.kMinOutput, Constants.Turret.kMaxOutput);

        turretPID.setSmartMotionMaxVelocity(Constants.Turret.maxVel, smartMotionSlot);
        turretPID.setSmartMotionMinOutputVelocity(Constants.Turret.minVel, smartMotionSlot);
        turretPID.setSmartMotionMaxAccel(Constants.Turret.maxAcc, smartMotionSlot);
        turretPID.setSmartMotionAllowedClosedLoopError(Constants.Turret.allowedErr, smartMotionSlot);
    }

    public void run() {
        updateEncoder();
        Constants.Drive.pigeon.getYawPitchRoll(ypr);
        ypr[0] *= Constants.degreesToRadians;
        ypr[0] = ypr[0] % (2*3.14159);
        turretPID.setReference(setPoint - ypr[0], ControlType.kPosition);
    }

    public void updateEncoder() {
        turretEncoder.setPosition(getAngle(false));
    }

    public void toSetpoint(double set) {
        setPoint = scaleSetpoint(set);
    }

    public double getRawTicks() {
        return -Constants.Turret.turretEnc.getSelectedSensorPosition();
    }

    public double getAngle(boolean fieldOriented) {
        if(fieldOriented) {
            Constants.Drive.pigeon.getYawPitchRoll(ypr);
            ypr[0] *= Constants.degreesToRadians;
            ypr[0] = (ypr[0] / 2) % (3.14159 / 2);
        }
        else {
            ypr[0] = 0;
        }
        return (getRawTicks() - Constants.Turret.encoderOffset)/Constants.Turret.ticksPerRev + ypr[0];
    }

    public double scaleSetpoint(double setpoint) {
        double scaledSetpoint = setpoint % (3.14159*2);
        return scaledSetpoint;
    }

    public void panic() {

    }

    public void display() {
        turretDisplay.angle(getAngle(true)/ Units.Angle.degrees);
        turretDisplay.setpoint(setPoint);
    }
}
