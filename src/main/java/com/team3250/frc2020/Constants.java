package com.team3250.frc2020;

import com.team3250.frc2020.subsystems.ServoMotorSubsystem.ServoMotorSubsystemConstants;

public class Constants {
    public static final double kLooperDt = 0.01;

    // wheels
    // Tuned 3/26/19 - 254
    public static final double kDriveWheelTrackWidthInches = 25.42;
    public static final double kDriveWheelDiameterInches = 5.5;
    public static final double kDriveWheelRadiusInches = kDriveWheelDiameterInches / 2.0;
    public static final double kDriveWheelTrackRadiusWidthMeters = kDriveWheelTrackWidthInches / 2.0 * 0.0254;
    public static final double kTrackScrubFactor = 1.0469745223;

    // tuned dynamics
    public static final double kDriveLinearVIntercept = 0.1801; // V
    public static final double kDriveLinearKv = 0.0919; // V per rad/s
    public static final double kDriveLinearKa = 0.03344; // V per rad/s^2
    public static final double kDriveAngularKa = 0.02897 * 2.0; // V per rad/s^2
    public static final double kRobotLinearInertia = 63.9565; // kg
    public static final double kRobotAngularInertia = kDriveAngularKa / kDriveLinearKa *
            kDriveWheelTrackRadiusWidthMeters * kDriveWheelTrackRadiusWidthMeters * kRobotLinearInertia;  // kg m^2
    public static final double kRobotAngularDrag = 0.0; // N*m / (rad/sec)

    public static final int kCANTimeoutMs = 10; // use for important on the fly updates
    public static final int kLongCANTimeoutMs = 100; // use for constructors

    public static final double kBumperHeight = 6.6 + 2.0; // inches to ground + 2 in buffer

    // drive
    public static final int kLeftDriveMasterId = 11;
    public static final int kLeftDriveSlave1Id = 12;
    public static final int kRightDriveMasterId = 13;
    public static final int kRightDriveSlaveId = 14;

    public static final int kLeftDriveEncoder = 19;
    public static final int kRightDriveEncoder = 16;

    public static final int kLeftDriveEncoderA = 0;
    public static final int kLeftDriveEncoderB = 1;
    public static final int kRightDriveEncoderA = 2;
    public static final int kRightDriveEncoderB = 3;

    public static final double kDriveEncoderPPR = 4096.0;

    public static final double kMinLookAhead = 12.0; // inches
    public static final double kMinLookAheadSpeed = 12.0; // inches per second
    public static final double kMaxLookAhead = 48.0; // inches
    public static final double kMaxLookAheadSpeed = 120.0; // inches per second
    public static final double kDeltaLookAhead = kMaxLookAhead - kMinLookAhead;
    public static final double kDeltaLookAheadSpeed = kMaxLookAheadSpeed - kMinLookAheadSpeed;

    public static final double kInertiaSteeringGain = 0.0; // angular velocity command is multiplied by this gain *
    // our speed
    // in inches per sec
    public static final double kPathFollowingMaxAccel = 80.0;  // inches per second ^ 2
    public static final double kPathFollowingMaxVel = 120.0; // inches per second
    public static final double kPathFollowingProfileKp = 0.3 / 12.0;  // % throttle per inch of error
    public static final double kPathFollowingProfileKi = 0.0;
    public static final double kPathFollowingProfileKv = 0.01 / 12.0;  // % throttle per inch/s of error
    public static final double kPathFollowingProfileKffv = 0.003889;  // % throttle per inch/s
    public static final double kPathFollowingProfileKffa = 0.001415;  // % throttle per inch/s^2
    public static final double kPathFollowingProfileKs = 0.1801 / 12.0;  // % throttle
    public static final double kPathFollowingGoalPosTolerance = 3.0;
    public static final double kPathFollowingGoalVelTolerance = 12.0;
    public static final double kPathStopSteeringDistance = 12.0;
    public static final double kDriveVoltageRampRate = 0.0;
    public static final int kDriveCurrentThrottledLimit = 30; // amps
    public static final int kDriveCurrentUnThrottledLimit = 80; // amps

    public static final double kFollowTargetSamplingDistance = 1.0;
    public static final double kFollowTargetLookahead = 30.0;
    public static final double kFollowTargetTolerance = 0.1;
    public static final double kFollowTargetSteeringScale = 0.05 * 24;
    public static final double kFollowTargetMaxThrottle = 0.8;
    public static final double kFollowTargetExtraWaypointDistance = 30.0;
    public static final double kPathKX = 4.0; // units/s per unit of error
    public static final double kPathLookaheadTime = 0.4; // seconds to look ahead along the path for steering
    public static final double kPathMinLookaheadDistance = 24.0; // inches

    // pigeon imu
    public static final int kPigeonIMUId = 15;

    //vision
    public static final double kHorizontalFOV = 59.6; // degrees
    public static final double kVerticalFOV = 49.7; // degrees
    public static final double kVPW = 2.0 * Math.tan(Math.toRadians(kHorizontalFOV / 2.0));
    public static final double kVPH = 2.0 * Math.tan(Math.toRadians(kVerticalFOV / 2.0));
    public static final double kImageCaptureLatency = 11.0 / 1000.0; // seconds

    public static final double kMaxTrackerDistance = 9.0;
    public static final double kMaxGoalTrackAge = 2.5;
    public static final double kMaxGoalTrackAgeNotTracking = 0.1;
    public static final double kMaxGoalTrackSmoothingTime = 0.5;
    public static final double kTrackStabilityWeight = 0.0;
    public static final double kTrackAgeWeight = 10.0;
    public static final double kTrackSwitchingWeight = 100.0;

    public static final double kCameraFrameRate = 90.0;
    public static final double kMinStability = 0.5;
    public static final int kPortPipeline = 0;
    public static final int kBallPipeline = 2;
    public static final double kPortTargetHeight = 39.125;
    public static final double kHatchTargetHeight = 31.5;

    public static final ServoMotorSubsystemConstants kTurretConstants = new ServoMotorSubsystemConstants();
    static {
        kTurretConstants.kName = "Turret";

        kTurretConstants.kMasterConstants.id = 21;
        kTurretConstants.kMasterConstants.invert_motor = false;
        kTurretConstants.kMasterConstants.invert_sensor_phase = true;

        // Unit == Degrees
        kTurretConstants.kHomePosition = 0.0;  // CCW degrees from forward
        kTurretConstants.kTicksPerUnitDistance = 4096.0 * 72.0 / 18.0 * 54.0 / 16.0 / 360.0;
        kTurretConstants.kKp = 2.0;
        kTurretConstants.kKi = 0;
        kTurretConstants.kKd = 10.0;
        kTurretConstants.kKf = 0.08;
        kTurretConstants.kKa = 0.0;
        kTurretConstants.kMaxIntegralAccumulator = 0;
        kTurretConstants.kIZone = 0; // Ticks
        kTurretConstants.kDeadband = 0; // Ticks

        kTurretConstants.kPositionKp = 0.35;
        kTurretConstants.kPositionKi = 0.0;
        kTurretConstants.kPositionKd = 0.0;
        kTurretConstants.kPositionKf = 0.0;
        kTurretConstants.kPositionMaxIntegralAccumulator = 0;
        kTurretConstants.kPositionIZone = 0; // Ticks
        kTurretConstants.kPositionDeadband = 0; // Ticks

        kTurretConstants.kMinUnitsLimit = -135.0;
        kTurretConstants.kMaxUnitsLimit = 315.0;

        kTurretConstants.kCruiseVelocity = 5000; // Ticks / 100ms
        kTurretConstants.kAcceleration = 16000; // Ticks / 100ms / s
        kTurretConstants.kRampRate = 0.0; // s
        kTurretConstants.kContinuousCurrentLimit = 20; // amps
        kTurretConstants.kPeakCurrentLimit = 30; // amps
        kTurretConstants.kPeakCurrentDuration = 10; // milliseconds
        kTurretConstants.kMaxVoltage = 12.0;

        kTurretConstants.kStatusFrame8UpdateRate = 50;
        kTurretConstants.kRecoverPositionOnReset = true;
    }
}