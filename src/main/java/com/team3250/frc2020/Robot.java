package com.team3250.frc2020;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.util.CrashTracker;
import com.team254.lib.wpilib.TimedRobot;
import com.team3250.frc2020.controlBoard.ControlBoard;
import com.team3250.frc2020.controlBoard.IControlBoard;
import com.team3250.frc2020.loops.Loop;
import com.team3250.frc2020.loops.Looper;
import com.team3250.frc2020.subsystems.Drive;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
  private final Looper mEnabledLooper = new Looper();
  private final Looper mDisabledLooper = new Looper();

  private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();

  // Subsystems
  private final RobotState mRobotState = RobotState.getInstance();

  private final Drive mDrive = Drive.getInstance();

  private boolean mHasBeenEnabled = false;
  private double mOffsetOverride = -1.0;

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

      mRobotState.reset(Timer.getFPGATimestamp(), Pose2d.identity(), Rotation2d.identity());
      mDrive.setHeading(Rotation2d.identity());

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

      mDrive.setBrakeMode(false);
    } catch (Throwable t) {
      CrashTracker.logThrowableCrash(t);
      throw t;
    }
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {

  }

  @Override
  public void testPeriodic() {

  }

}
