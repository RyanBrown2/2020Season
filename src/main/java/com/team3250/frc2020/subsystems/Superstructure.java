package com.team3250.frc2020.subsystems;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team3250.frc2020.Constants;
import com.team3250.frc2020.Robot;
import com.team3250.frc2020.RobotState;
import com.team3250.frc2020.loops.ILooper;
import com.team3250.frc2020.loops.Loop;
import com.team3250.frc2020.states.SuperstructureGoal;
import com.team3250.frc2020.states.SuperstructureState;
import com.team3250.lib.vision.AimingParameters;

import java.util.Optional;

/**
 * The superstructure subsystem is the overarching class containing all components of the superstructure: the
 *  * turret, hood, and flywheel. The superstructure subsystem also uses info from the vision system.
 *  * <p>
 *  * Instead of interacting individually with subsystems like the elevator and arm, the {@link Robot} class sends commands
 *  * to the superstructure, which individually decides how to move each subsystem to get there.
 *  * <p>
 *  * The Superstructure class also adjusts the overall goal based on the turret control mode.
 *
 *  @see com.team3250.frc2020.statemachines.SuperstructureCommands
 *
 */
public class Superstructure extends Subsystem {
    private static Superstructure mInstance;

    private final Turret mTurret = Turret.getInstance();
    private final Hood mHood = Hood.getInstance();
    private final Flywheel mFlywheel = Flywheel.getInstance();
    private final RobotState mRobotState = RobotState.getInstance();

    // Current state = actual measured state of each DOF.
    private SuperstructureState mCurrentState = new SuperstructureState();
    // Current setpoint = output of the planner. May or may not be the final goal.
    private SuperstructureGoal mCurrentSetpoint = null;
    // The goal is the first desired state of the superstructure.
    private SuperstructureGoal mGoal;
    private Rotation2d mFieldRelativeTurretGoal = null;

    private SuperstructureGoal mLastValidGoal;

    enum TurretControlModes {
        ROBOT_RELATIVE, FIELD_RELATIVE, VISION_AIMED, OPEN_LOOP, JOGGING
    }

    private boolean mHasTarget = false;
    private boolean mOnTarget = false;
    private int mTrackId = -1;

    private double mTurretFeedforwardV = 0.0;
    private Optional<AimingParameters> mLatestAimingParameters = Optional.empty();

    private TurretControlModes mTurretMode = TurretControlModes.ROBOT_RELATIVE;

    public synchronized static Superstructure getInstance() {
        if (mInstance == null) {
            mInstance = new Superstructure();
        }
        return mInstance;
    }

    private Superstructure() {}

    @Override
    public void registerEnabledLoops(ILooper mEnabledLooper) {
        mEnabledLooper.register(new Loop() {
            @Override
            public void onStart(double timestamp) {
                synchronized (Superstructure.this) {

                }
            }

            @Override
            public void onLoop(double timestamp) {
                synchronized (Superstructure.this) {
                    if (mGoal == null) {
                        return;
                    }



                }
            }

            @Override
            public void onStop(double timestamp) {

            }
        });
    }

    public synchronized SuperstructureGoal getGoal() {
        return mGoal;
    }

    public synchronized SuperstructureState getCurrentState() {
        return mCurrentState;
    }

    public synchronized SuperstructureGoal getSetpoint() {
        return mCurrentSetpoint;
    }

    public synchronized void jogTurret(double delta) {
        if (mGoal == null || mGoal.state == null) {
            return;
        }

        mTurretMode = TurretControlModes.JOGGING;
        mGoal.state.turret = mCurrentState.turret + delta;
        mTurretFeedforwardV = 0.0;
    }

    public synchronized void setGoal(SuperstructureGoal goal) {
        if (mGoal == null) {
            mGoal = new SuperstructureGoal(goal.state);
        }

        if (mTurretMode == TurretControlModes.VISION_AIMED && mHasTarget) {
            // Keep existing setpoint
        } else {
            mGoal.state.turret = goal.state.turret;
            mGoal.state.hood = goal.state.hood;
            mGoal.state.flywheel = goal.state.flywheel;
        }
    }

    private synchronized void maybeUpdateGoalFromFieldRelativeGoal(double timestamp) {
        if (mTurretMode != TurretControlModes.FIELD_RELATIVE && mTurretMode != TurretControlModes.VISION_AIMED) {
            mFieldRelativeTurretGoal = null;
            return;
        }
        if (mTurretMode == TurretControlModes.VISION_AIMED && !mLatestAimingParameters.isEmpty()) {
            // Vision controls turret
            return;
        }
        if (mFieldRelativeTurretGoal == null) {
            mTurretMode = TurretControlModes.ROBOT_RELATIVE;
            return;
        }
        final double kLookaheadTime = 0.7;
        Rotation2d turret_error = mRobotState.getPredictedFieldToVehicle(kLookaheadTime)
                .transformBy(Pose2d.fromRotation(mRobotState.getVehicleToTurret(timestamp))).getRotation().inverse()
                .rotateBy(mFieldRelativeTurretGoal);
        mGoal.state.turret = mCurrentState.turret + turret_error.getDegrees();

        if (mGoal.state.turret < Constants.kTurretConstants.kMinUnitsLimit) {
            mGoal.state.turret += 360.0;
        }

        if (mGoal.state.turret > Constants.kTurretConstants.kMaxUnitsLimit) {
            mGoal.state.turret -= 360.0;
        }
    }

    public synchronized void resetAimingParameters() {
        mHasTarget = false;
        mOnTarget = false;
        mTurretFeedforwardV = 0.0;
        mTrackId = -1;
        mLatestAimingParameters = Optional.empty();
    }

    private synchronized void maybeUpdateGoalFromVision(double timestamp) {
        if (mTurretMode != TurretControlModes.VISION_AIMED) {
            mHasTarget = false;
            mOnTarget = false;
            mTurretFeedforwardV = 0.0;
            mTrackId = -1;
            mLatestAimingParameters = Optional.empty();
            return;
        }
//        mLatestAimingParameters = mRobotState.getA
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean checkSystem() {
        return false;
    }

    @Override
    public void outputTelemetry() {

    }
}
