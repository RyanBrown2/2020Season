package com.team3250.frc2020.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;
import com.team254.lib.drivers.LazySparkMax;
import com.team254.lib.drivers.SparkMaxFactory;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team3250.frc2020.Constants;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class Drive extends Subsystem {
    private static Drive mInstance;

    // Speed Controllers
    private final LazySparkMax mLeftMaster, mRightMaster, mLeftSlave, mRightSlave;
    // Encoders
    private final Encoder mLeftEncoder, mRightEncoder;
    // Gyro
    private PigeonIMU mPigeon;
    private Rotation2d mGyroOffset = Rotation2d.identity();

    // Control States
    private DriveControlState mDriveControlState;


    public synchronized static Drive getInstance() {
        if (mInstance == null) {
            mInstance = new Drive();
        }

        return mInstance;
    }

    private void configureSpark(LazySparkMax sparkMax, boolean left, boolean master) {
        sparkMax.setInverted(!left);
        sparkMax.enableVoltageCompensation(12.0);
        sparkMax.setClosedLoopRampRate(Constants.kDriveVoltageRampRate);
    }

    private Drive() {
        mPeriodicIO = new PeriodicIO();

        // start all Talons in open loop mode
        mLeftMaster = SparkMaxFactory.createDefaultSparkMax(Constants.kLeftDriveMasterId);
        configureSpark(mLeftMaster, true, true);

        mLeftSlave = SparkMaxFactory.createPermanentSlaveSparkMax(Constants.kLeftDriveSlave1Id, mLeftMaster);
        configureSpark(mLeftSlave, true, false);

        mRightMaster = SparkMaxFactory.createDefaultSparkMax(Constants.kRightDriveMasterId);
        configureSpark(mRightMaster, false, true);

        mRightSlave = SparkMaxFactory.createPermanentSlaveSparkMax(Constants.kRightDriveSlaveId, mRightMaster);
        configureSpark(mRightSlave, false, false);

        // burn flash so that when spark resets they have the same config
        // mLeftMaster.burnFlash();
        // mLeftSlave.burnFlash();
        // mRightMaster.burnFlash();
        // mRightSlave.burnFlash();

        mLeftEncoder = new Encoder(Constants.kLeftDriveEncoderA, Constants.kLeftDriveEncoderB, false);
        mRightEncoder = new Encoder(Constants.kRightDriveEncoderA, Constants.kRightDriveEncoderB, true);

        mPigeon = new PigeonIMU(Constants.kPigeonIMUId);
        mPigeon.setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR, 10, 10);
    }

    private PeriodicIO mPeriodicIO;

    public static class PeriodicIO {
        // INPUTS
        public double timestamp;
        public double left_voltage;
        public double right_voltage;
        public int left_position_ticks;
        public int right_position_ticks;
        public double left_distance;
        public double right_distance;
        public int left_velocity_ticks_per_100ms;
        public int right_velocity_ticks_per_100ms;
        public Rotation2d gyro_heading = Rotation2d.identity();
        public Pose2d error = Pose2d.identity();

        // OUTPUTS
        public double left_demand;
        public double right_demand;
        public double left_accel;
        public double right_accel;
        public double left_feedforward;
        public double right_feedforward;
    }

    @Override
    public synchronized void readPeriodicInputs() {
        mPeriodicIO.timestamp = Timer.getFPGATimestamp();
        double prevLeftTicks = mPeriodicIO.left_position_ticks;
        double prevRightTicks = mPeriodicIO.right_position_ticks;
        mPeriodicIO.left_voltage = mLeftMaster.getAppliedOutput() * mLeftMaster.getBusVoltage();
        mPeriodicIO.right_voltage = mRightMaster.getAppliedOutput() * mRightMaster.getBusVoltage();

        mPeriodicIO.left_position_ticks = mLeftEncoder.get();
        mPeriodicIO.right_position_ticks = mRightEncoder.get();
        mPeriodicIO.gyro_heading = Rotation2d.fromDegrees(mPigeon.getFusedHeading()).rotateBy(mGyroOffset);

        double deltaLeftTicks = ((mPeriodicIO.left_position_ticks - prevLeftTicks) / Constants.kDriveEncoderPPR) * Math.PI;
        mPeriodicIO.left_distance += deltaLeftTicks * Constants.kDriveWheelDiameterInches;

        double deltaRightTicks = ((mPeriodicIO.right_position_ticks - prevRightTicks) / Constants.kDriveEncoderPPR) * Math.PI;
        mPeriodicIO.right_distance += deltaRightTicks * Constants.kDriveWheelDiameterInches;

        mPeriodicIO.left_velocity_ticks_per_100ms = (int) (mLeftEncoder.getRate() / (10 * mLeftEncoder.getDistancePerPulse()));
        mPeriodicIO.right_velocity_ticks_per_100ms = (int) (mRightEncoder.getRate() / (10 * mRightEncoder.getDistancePerPulse()));



    }

    public enum DriveControlState {
        OPEN_LOOP, // open loop voltage control
        PATH_FOLLOWING // velocity PID control
    }

    public enum DriveCurrentLimitState {
        UNTHROTTLED, THROTTLED
    }
}
