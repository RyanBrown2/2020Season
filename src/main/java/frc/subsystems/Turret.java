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
    // smart motion slot allows us to change the PID/Motion Profile running onboard the spark max controllers
    public int smartMotionSlot;
    // setpoint is wherever you want the turret to go, as an angle in radians, relative to the field, which is set with the toSetpoint function
    double setPoint, processVariable;
    // ypr = yaw/pitch/roll, stored as an array with all 3 values since this is how the gyro outputs it
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

        // set the PID constants from the constants file
        turretPID.setP(Constants.Turret.kP);
        turretPID.setI(Constants.Turret.kI);
        turretPID.setD(Constants.Turret.kD);
        turretPID.setIZone(Constants.Turret.kIz);
        turretPID.setFF(Constants.Turret.kFF);
        turretPID.setOutputRange(Constants.Turret.kMinOutput, Constants.Turret.kMaxOutput);
        // set the motion profile and smart velocity settings from the constants file
        turretPID.setSmartMotionMaxVelocity(Constants.Turret.maxVel, smartMotionSlot);
        turretPID.setSmartMotionMinOutputVelocity(Constants.Turret.minVel, smartMotionSlot);
        turretPID.setSmartMotionMaxAccel(Constants.Turret.maxAcc, smartMotionSlot);
        turretPID.setSmartMotionAllowedClosedLoopError(Constants.Turret.allowedErr, smartMotionSlot);
    }

    // this function is run constantly, and is the main function for this class
    public void run() {
        updateEncoder(true);
        // % sign means modulus, take the modulus of the setpoint and 2pi to limit the setpoint to 1 full rotation, as 2pi radians is one full rotation, or 360 degrees
        setPoint %= (2*3.14159);
        Constants.Drive.pigeon.getYawPitchRoll(ypr);
        // convert yaw to radians as the gyro outputs degrees
        ypr[0] *= Constants.degreesToRadians;
        // again, modulus is used to limit to 2 full rotations as th
        ypr[0] = (ypr[0]/2) % (3.14159/2);
        turretPID.setReference(setPoint - ypr[0], ControlType.kPosition);
    }

    // im pretty sure this isn't used but im leaving it here for some reason
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

    // the spark max controllers' onboard PID and motion profiles only work with an encoder attached to that spark max,
    // but our encoders aren't compatible in brushless mode and so we make a fake encoder that the spark max will be able
    // to read and use by constantly updating the value of the fake encoder with the one from the real encoder, plugged into our talonsrx
    public void updateEncoder(boolean fieldOriented) {
        if(fieldOriented) {
            Constants.Drive.pigeon.getYawPitchRoll(ypr);
            turretEncoder.setPosition(getAngle() + ypr[0]*Constants.degreesToRadians);
        }
        else {
            turretEncoder.setPosition(getAngle());
        }
    }

    // used to adjust which direction the turret should be pointing relative to the field, in radians
    public void toSetpoint(double set) {
        setPoint = set % Constants.Turret.ticksPerRev;
    }

    // reads raw encoder ticks for use in other functions and for debugging purposes
    public double getRawTicks() {
        return -Constants.Turret.turretEnc.getSelectedSensorPosition();
    }

    public double getAngle() {
        return (getRawTicks() - Constants.Turret.encoderOffset)/Constants.Turret.ticksPerRev;
    }

    public void panic() {
        //TODO
    }

    // displays important information about the turret to the dashboard
    public void display() {
        // display angle in degrees because im lacking
        turretDisplay.angle(getAngle()/ Units.Angle.degrees);
        turretDisplay.setpoint(setPoint);
    }
}
