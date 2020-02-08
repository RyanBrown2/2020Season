package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import frc.display.TurretDisplay;
import frc.robot.Constants;
import frc.utilPackage.Units;

import static frc.robot.Constants.Turret.fieldOriented;

public class Turret {
    TurretDisplay turretDisplay;
    CANSparkMax turretMotor;
    CANPIDController turretPID;
    CANEncoder turretEncoder;
    public int smartMotionSlot;
    double setPoint, processVariable;
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
//        keepInRange();
        updateEncoder(true);
        setPoint %= (2*3.14159);
        Constants.Drive.pigeon.getYawPitchRoll(ypr);
        ypr[0] *= Constants.degreesToRadians;
        ypr[0] = (ypr[0]/2) % (3.14159/2);
        turretPID.setReference(setPoint - ypr[0], ControlType.kPosition);
//        turretPID.setReference(setPoint, ControlType.kPosition);
    }

    public void keepInRange() {
        if(fieldOriented) {
            if(getRawTicks() > Constants.Turret.ticksPerRev) {
                double val = setPoint/Constants.Turret.ticksPerRev;
                val -= (val % 1);
                setPoint -= val*Constants.Turret.ticksPerRev;
            }
            if(getRawTicks() < 0) {
                double val = setPoint/Constants.Turret.ticksPerRev;
                val -= (val % 1);
                setPoint += val*Constants.Turret.ticksPerRev;
            }
        }
        if(!fieldOriented) {
            setPoint = Math.abs(setPoint % Constants.Turret.ticksPerRev);
        }
    }

    public void updateEncoder(boolean fieldOriented) {
        if(fieldOriented) {
            double[] ypr = new double[3];
            Constants.Drive.pigeon.getYawPitchRoll(ypr);
            turretEncoder.setPosition(getAngle() + ypr[0]*Constants.degreesToRadians);
        }
        else {
            turretEncoder.setPosition(getAngle());
        }
    }

    public void toSetpoint(double set) {
        setPoint = set % Constants.Turret.ticksPerRev;
    }

    public double getRawTicks() {
        return -Constants.Turret.turretEnc.getSelectedSensorPosition();
    }

    public double getAngle() {
        return (getRawTicks() - Constants.Turret.encoderOffset)/Constants.Turret.ticksPerRev;
    }

    public void panic() {

    }

    public void display() {
        turretDisplay.angle(getAngle()/ Units.Angle.degrees);
        turretDisplay.setpoint(setPoint);
    }
}
