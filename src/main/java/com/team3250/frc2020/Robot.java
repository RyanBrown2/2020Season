package com.team3250.frc2020;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.util.CrashTracker;
import com.team254.lib.util.LatchedBoolean;
import com.team254.lib.wpilib.TimedRobot;
import com.team3250.frc2020.auto.AutoModeExecutor;
import com.team3250.frc2020.auto.modes.AutoModeBase;
import com.team3250.frc2020.controlBoard.ControlBoard;
import com.team3250.frc2020.controlBoard.IControlBoard;
import com.team3250.frc2020.loops.Loop;
import com.team3250.frc2020.loops.Looper;
import com.team3250.frc2020.subsystems.Drive;
import com.team3250.lib.vision.AimingParameters;
import edu.wpi.first.wpilibj.Timer;

import java.util.Optional;

public class Robot extends TimedRobot {
  private final Looper mEnabledLooper = new Looper();
  private final Looper mDisabledLooper = new Looper();

  private final IControlBoard mControlBoard = ControlBoard.getInstance();

  private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();

  // Subsystems
  private final RobotState mRobotState = RobotState.getInstance();

  private final Drive mDrive = Drive.getInstance();

  private boolean mHasBeenEnabled = false;
  private double mOffsetOverride = -1.0;

  private LatchedBoolean mAutoSteerPressed = new LatchedBoolean();

  private AutoModeSelector mAutoModeSelector = new AutoModeSelector();
  private AutoModeExecutor mAutoModeExecutor;

  Robot() {
    CrashTracker.logRobotConstruction();
  }

  @Override
  public void robotInit() {
    try {
      CrashTracker.logRobotInit();

      mSubsystemManager.setSubsystems(
              mDrive
      );

      mSubsystemManager.registerEnabledLoops(mEnabledLooper);
      mSubsystemManager.registerDisabledLoops(mDisabledLooper);

      // Robot starts forwards
      mRobotState.reset(Timer.getFPGATimestamp(), Pose2d.identity(), Rotation2d.identity());
      mDrive.setHeading(Rotation2d.identity());
//      mDrive.resetEncoders();

      mAutoModeSelector.updateModeCreator();
    } catch (Throwable t) {
      CrashTracker.logThrowableCrash(t);
      throw t;
    }
  }

  @Override
  public void disabledInit() {
    try {
      CrashTracker.logDisabledInit();
      mEnabledLooper.stop();
      mDisabledLooper.start();

      mAutoModeSelector.reset();
      mAutoModeSelector.updateModeCreator();
      mAutoModeExecutor = new AutoModeExecutor();

      mDrive.setBrakeMode(false);
    } catch (Throwable t) {
      CrashTracker.logThrowableCrash(t);
      throw t;
    }
  }

  @Override
  public void autonomousInit() {
    try {
      CrashTracker.logAutoInit();
      mDisabledLooper.stop();

      // Robot starts forwards
      mRobotState.reset(Timer.getFPGATimestamp(), Pose2d.identity(), Rotation2d.identity());
      mDrive.setHeading(Rotation2d.identity());

      mEnabledLooper.start();

    } catch (Throwable t) {
      CrashTracker.logThrowableCrash(t);
      throw t;
    }
  }

  @Override
  public void teleopInit() {
    try {
      CrashTracker.logTeleopInit();
      mDisabledLooper.stop();

      if (mAutoModeExecutor != null) {
        mAutoModeExecutor.stop();
      }

      mHasBeenEnabled = true;

      mEnabledLooper.start();

      mOffsetOverride = -2.0;
    } catch (Throwable t) {
      CrashTracker.logThrowableCrash(t);
      throw t;
    }
  }

  @Override
  public void testInit() {
    try {
      CrashTracker.logTestInit();
      System.out.println("Starting check systems.");

      mDisabledLooper.stop();
      mEnabledLooper.stop();

      if (mSubsystemManager.checkSubsystems()) {
        System.out.println("ALL SYSTEMS PASSED");
      } else {
        System.out.println("CHECK ABOVE OUTPUT SOME SYSTEMS FAILED!!!");
      }
    } catch (Throwable t) {
      CrashTracker.logThrowableCrash(t);
      throw t;
    }
  }

  @Override
  public void robotPeriodic() {
    try {
      mSubsystemManager.outputToSmartDashboard();
      mRobotState.outputToSmartDashboard();
      mAutoModeSelector.outputToSmartDashboard();
    } catch (Throwable t) {
      CrashTracker.logThrowableCrash(t);
      throw t;
    }
  }

  @Override
  public void disabledPeriodic() {
    try {
      // Update auto modes
      mAutoModeSelector.updateModeCreator();

      Optional<AutoModeBase> autoMode = mAutoModeSelector.getAutoMode();
      if (autoMode.isPresent() && autoMode.get() != mAutoModeExecutor.getAutoMode()) {
        System.out.println("Set auto mode to: " + autoMode.get().getClass().toString());
        mAutoModeExecutor.setAutoMode(autoMode.get());
      }
    } catch (Throwable t) {
      CrashTracker.logThrowableCrash(t);
      throw t;
    }
  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopPeriodic() {
    try {
      telopControls();
    } catch (Throwable t) {
      CrashTracker.logThrowableCrash(t);
      throw t;
    }
  }

  public void telopControls() {
    double timestamp = Timer.getFPGATimestamp();
    double throttle = mControlBoard.getThrottle();

//    Optional<AimingParameters> turret_aim_params =

    // commands
//    boolean

    boolean driving = true;

    if (driving) {
        mDrive.setCheesyishDrive(throttle, mControlBoard.getWheel(), mControlBoard.getQuickTurn());
    }
  }

  @Override
  public void testPeriodic() {
  }

}
