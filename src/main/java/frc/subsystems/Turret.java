package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
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
    double setPoint;
    // Yaw-Pitch-Roll (ypr) is used to store the gyro data
    double[] ypr = new double[3];

    private Turret() {
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
//        turretPID.setReference(setPoint, ControlType.kPosition);
         turretPID.setReference(setPoint - ypr[0], ControlType.kPosition);
    }

    // Encoder not plugged directly into Spark Max, so update a 'fake' encoder with the actual value for the PID loops
    public void updateEncoder() {
        turretEncoder.setPosition(getAngle(false));
    }

    public void toSetpoint(double set) {
        setPoint = scaleSetpoint(set);
    }

    public double getRawTicks() {
        return -Constants.Turret.turretEnc.getSelectedSensorPosition();
    }

    // Angle is returned in radians, and can be relative to the robot's starting position
    public double getAngle(boolean fieldOriented) {
        if(fieldOriented) {
            Constants.Drive.pigeon.getYawPitchRoll(ypr);
//            SmartDashboard.putNumber("Gyro")
            ypr[0] *= Constants.degreesToRadians;
            ypr[0] = (ypr[0]) % (3.14159 * 2);
        }
        else {
            // If not field oriented, don't account for the robot's yaw
            ypr[0] = 0;
        }
        return (getRawTicks() - Constants.Turret.encoderOffset)/Constants.Turret.ticksPerRev + ypr[0];
//        return (getRawTicks() - Constants.Turret.encoderOffset)/Constants.Turret.ticksPerRev;
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

    // Setpoint is scaled to prevent damage to wires by going more than one rotation
    public double scaleSetpoint(double setpoint) {
        // Use modulus operator to keep setpoint under one full rotation
        double scaledSetpoint = setpoint % (3.14159*2);
        return scaledSetpoint;
    }

    public void panic() {

    }

    public void display() {
        turretDisplay.angle(getAngle(true)/Units.Angle.degrees);
        turretDisplay.setpoint(setPoint/Units.Angle.degrees);
        turretDisplay.atSetpoint(atSetpoint(false));
//        SmartDashboard.putNumber("Turret Angle", getAngle(true));
//        SmartDashboard.putNumber("Raw Turret Ticks", getRawTicks());
//        SmartDashboard.putNumber("Turret Setpoint", setPoint - ypr[0]);
    }
}
