package frc.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class Turret {
    CANSparkMax turretMotor;
    CANPIDController turretPID;
    CANEncoder turretEncoder;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;
    public int smartMotionSlot;
    double setPoint, processVariable;

    public Turret() {
        turretMotor = Constants.Shooter.turret;
        turretPID = turretMotor.getPIDController();
        turretEncoder = turretMotor.getAlternateEncoder();

        Constants.Shooter.turretEnc.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        Constants.Shooter.turretEnc.setSelectedSensorPosition(0);

        turretEncoder.setPositionConversionFactor(1);

        turretMotor.restoreFactoryDefaults();

        smartMotionSlot = 0;

        //PID Constants
        kP = 5e-5;
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0;
        kMaxOutput = 0.25;
        kMinOutput = -0.25;
        maxRPM = 5700;

        //Smart Motion Coefficients
        maxVel = 20; // rpm
        maxAcc = 20;

        turretPID.setP(kP);
        turretPID.setI(kI);
        turretPID.setD(kD);
        turretPID.setIZone(kIz);
        turretPID.setFF(kFF);
        turretPID.setOutputRange(kMinOutput, kMaxOutput);

        turretPID.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        turretPID.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        turretPID.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        turretPID.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);
    }

    public void run() {
        turretPID.setReference(setPoint, ControlType.kPosition);
    }

    public void updateEncoder() {
        turretEncoder.setPosition(getAngle());
    }

    public void toSetpoint(double set) {
        setPoint = set;
    }

    public double getRawTicks() {
        return Constants.Shooter.turretEnc.getSelectedSensorPosition();
    }

    public double getAngle() {
        return (getRawTicks() - Constants.Shooter.encoderOffset)/Constants.Shooter.ticksPerRev;
    }

    public void display() {
        SmartDashboard.putNumber("Turret Raw Ticks", getRawTicks());
        SmartDashboard.putNumber("Turret Position", turretEncoder.getPosition());
        SmartDashboard.putNumber("Turret Setpoint", setPoint);
        SmartDashboard.putNumber("Turret Degrees", getAngle()*(180/3.14159));
        SmartDashboard.putNumber("Output", turretMotor.getAppliedOutput());
    }
}
