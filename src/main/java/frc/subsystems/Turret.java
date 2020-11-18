package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import frc.display.TurretDisplay;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.utilPackage.Units;

public class Turret {
    private static Turret instance = null;
    public static Turret getInstance() {
        if (instance == null) {
            instance = new Turret();
        }
        return instance;
    }
    TurretDisplay turretDisplay;
    CANSparkMax turretMotor;
    CANPIDController turretPID;
    CANEncoder turretEncoder;
    // smart motion slot allows us to change the PID/Motion Profile running onboard the spark max controllers
    public int smartMotionSlot;
    // default setpoint
    double setPoint = Constants.pi/6;
    double tempSetpoint;
    double tempSetpointFieldOriented;
    // Yaw-Pitch-Roll (ypr) is used to store the gyro data
    double[] ypr = new double[3];

    private Turret() {
        turretMotor = Constants.Turret.turret;
        turretDisplay = new TurretDisplay();
        turretPID = turretMotor.getPIDController();
        turretEncoder = turretMotor.getAlternateEncoder();

        // make a fake encoder for the onboard PID on the spark max to use
        turretPID.setFeedbackDevice(turretEncoder);

        // add the real encoder from the talonSRX
        Constants.Turret.turretEnc.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        Constants.Turret.turretEnc.setSelectedSensorPosition(6936);

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

    // main function for the turret, runs constantly and handles all movement
    public void run(boolean fieldOriented) {
        // call update encoder constantly so that our fake encoder has the real encoder values stored within it
        updateEncoder();
        // use a modulus function on the provided setpoint that keeps it under one full rotation (2pi radians)
        tempSetpoint = (setPoint) % (Constants.pi*2);
        // make the setpoint relative to the field by subtracting yaw-pi since the yaw is flipped 180 degrees for some reason
        tempSetpointFieldOriented = tempSetpoint - (getYaw() - Constants.pi);
        // use modulus again to ensure the setpoint is not above a full rotation
        tempSetpointFieldOriented %= (2*Constants.pi);

        // if the setpoint is below the deadzone, set it to the lower limit of the deadzone
        if(tempSetpointFieldOriented < 0) {
            tempSetpointFieldOriented = (2*Constants.pi + tempSetpointFieldOriented);
        }
        // if the setpoint is above the deadzone, stop all motion until setpoint returns to somewhere good
        if(tempSetpointFieldOriented > (3*Constants.pi/2)){
            turretPID.setOutputRange(0, 0);
            // displays whether the turret's setpoint is in the deadzone or not
            Robot.driverDisplay.setInDeadZone(true);
        } else {
            turretPID.setOutputRange(-1, 1);
            Robot.driverDisplay.setInDeadZone(false);
        }

        // final movement statement, the motion profile reference is set and the onboard PID handles the movement to the point
         if(fieldOriented) {
             turretPID.setReference(tempSetpointFieldOriented, ControlType.kPosition);
         } else {
             turretPID.setReference(setPoint, ControlType.kPosition);
         }
    }

    // Encoder not plugged directly into Spark Max, so update a 'fake' encoder with the actual value for the PID loops
    public void updateEncoder() {
        turretEncoder.setPosition(getAngle(false));
    }

    // used to adjust which direction the turret should be pointing relative to the field, in radians
    public void toSetpoint(double set) {
        setPoint = set;
    }

    // reads raw encoder ticks for use in other functions and for debugging purposes
    public double getRawTicks() {
        return -Constants.Turret.turretEnc.getSelectedSensorPosition();
    }

    // Angle is returned in radians, and can be relative to the robot's starting position
    public double getAngle(boolean fieldOriented) {
        if(fieldOriented) {
            return (getRawTicks() / Constants.Turret.ticksPerRev) + getYaw();
        } else {
            return (getRawTicks() / Constants.Turret.ticksPerRev) + Constants.pi;
        }
    }

    // its the same as getting everything but all we need is yaw, which is stored as value 0 in the array output by the gyro
    public double getYaw() {
        Constants.Drive.pigeon.getYawPitchRoll(ypr);
        // convert to radians cause they're better
        ypr[0] *= Constants.degreesToRadians;
        // offset it by pi cause it makes more sense
        ypr[0] += Constants.pi;
        // modulus it for obvious reasons
        ypr[0] %= (Constants.pi*2);
        // if it is negative make it not negative
        if(ypr[0] < 0) {
            ypr[0] = (Constants.pi*2) + ypr[0];
        }
        // modulus it again because i have an obsession
        ypr[0] %= (Constants.pi*2);
        return ypr[0];
    }

    // Returns true if turret is at setpoint
    public boolean atSetpoint(boolean fieldOriented) {
        double setpoint = this.setPoint;
        if (Math.abs(setpoint - getAngle(fieldOriented)) < Constants.Turret.acceptedError) {
            return true;
        } else {
            return false;
        }
    }

    public double getVelocity() {
        return turretEncoder.getVelocity() / Constants.Turret.ticksPerRev;
    }

    // displays important information about the turret to the dashboard
    public void display() {
        updateEncoder();
        turretDisplay.angle(getAngle(false)/Units.Angle.degrees);
        turretDisplay.fieldOrientedAngle(getAngle(true)/Units.Angle.degrees);
        turretDisplay.setpoint(tempSetpoint/Units.Angle.degrees);
        turretDisplay.atSetpoint(atSetpoint(true));

        if(turretDisplay.turretReset()) {
            Constants.Turret.turretEnc.setSelectedSensorPosition(6936);
            Constants.Drive.pigeon.setYaw(0);
        }
        turretDisplay.untoggleButtons();
    }
}
