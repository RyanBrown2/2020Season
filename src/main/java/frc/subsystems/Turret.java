package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.display.TurretDisplay;
import frc.robot.Constants;
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
    public int smartMotionSlot;
    double setPoint, tempSetpoint;
    // Yaw-Pitch-Roll (ypr) is used to store the gyro data
    double[] ypr = new double[3];

    private Turret() {
        turretMotor = Constants.Turret.turret;
        turretDisplay = new TurretDisplay();
        turretPID = turretMotor.getPIDController();
        turretEncoder = turretMotor.getAlternateEncoder();

        turretPID.setFeedbackDevice(turretEncoder);

        Constants.Turret.turretEnc.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
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

    public void run(boolean fieldOriented) {
        updateEncoder();
        tempSetpoint = (setPoint + Constants.Turret.encoderOffset) % (3.14159*2);
//        if((tempSetpoint - getYaw()) > Constants.Turret.upperRadianLimit) {
//            tempSetpoint = Constants.Turret.upperRadianLimit + getYaw() - Constants.Turret.encoderOffset;
//        } if((tempSetpoint - getYaw()) < Constants.Turret.lowerRadianLimit) {
//            tempSetpoint = Constants.Turret.lowerRadianLimit + getYaw() - Constants.Turret.encoderOffset;
//        }
        SmartDashboard.putNumber("Temp Setpoint", tempSetpoint);

         if(fieldOriented) {
             turretPID.setReference(tempSetpoint - getYaw(), ControlType.kPosition);
         } else {
             turretPID.setReference(setPoint, ControlType.kPosition);
         }
    }

    // Encoder not plugged directly into Spark Max, so update a 'fake' encoder with the actual value for the PID loops
    public void updateEncoder() {
        turretEncoder.setPosition(getAngle(false));
    }

    public void toSetpoint(double set) {
        setPoint = set;
    }

    public double getRawTicks() {
        return -Constants.Turret.turretEnc.getSelectedSensorPosition();
    }

    // Angle is returned in radians, and can be relative to the robot's starting position
    public double getAngle(boolean fieldOriented) {
        if(fieldOriented) {
            return (getRawTicks() / Constants.Turret.ticksPerRev) + getYaw() + Constants.Turret.encoderOffset;
        } else {
            return (getRawTicks() / Constants.Turret.ticksPerRev) /*+ Constants.Turret.encoderOffset*/;
        }
    }

    public double getYaw() {
        Constants.Drive.pigeon.getYawPitchRoll(ypr);
        ypr[0] *= Constants.degreesToRadians;
//        ypr[0] += Constants.Turret.encoderOffset;
        ypr[0] %= (3.14159*2);
//        if(ypr[0] < 0) {
//            ypr[0] = 360 - Math.abs(ypr[0]);
//        }
//        ypr[0] = (ypr[0]) % (3.14159*2) + Constants.Turret.encoderOffset;
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

    public void display() {
        updateEncoder();
        turretDisplay.angle(getAngle(false)/Units.Angle.degrees);
        turretDisplay.setpoint(tempSetpoint/Units.Angle.degrees);
        turretDisplay.atSetpoint(atSetpoint(false));
        SmartDashboard.putNumber("Encoder Angle", turretEncoder.getPosition());
        SmartDashboard.putNumber("Setpoint", setPoint);
        SmartDashboard.putNumber("TempSetpoint - yaw", tempSetpoint - getYaw());
    }
}
