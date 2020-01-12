package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autos.actions.VisionPursuit;
import frc.autos.modes.AutoMode;
import frc.autos.modes.PathTest;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.display.ShuffleboardDisplay;
import frc.drive.Drive;
import frc.drive.DriveController;
import frc.drive.DriveOutput;
import frc.drive.PositionTracker;
import frc.util.Jevois;
import frc.utilPackage.ScaledDrive;
import frc.utilPackage.TeleopDrive;

public class Robot extends TimedRobot {
  private static IControlBoard cb = new ControlBoard();
  public static IControlBoard getControlBoard(){
    return cb;
  }

  int _smoothing = 0;

  AutoMode auto;

  ShuffleboardDisplay display;

  Drive driveAuto;
  DriveController driveController;
  DriveOutput driveOutput;
  PositionTracker positionTracker;
  ScaledDrive scaledDrive;

  boolean resetVision = false;

  @Override
  public void robotInit() {
    scaledDrive = new ScaledDrive();
    driveAuto = Drive.getInstance();
    driveController = DriveController.getInstance();
    driveOutput = DriveOutput.getInstance();
    driveOutput.start();
    auto = new PathTest();
    positionTracker = PositionTracker.getInstance();
    display = new ShuffleboardDisplay();
    scaledDrive.enabled(true);
    display.run();
  }

  @Override
  public void robotPeriodic() {
//    display.run();
    Drive.getInstance().display();
    positionTracker.display();
//    display();
  }

  @Override
  public void autonomousInit() {
    auto.start();
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopPeriodic() {

  }

  @Override
  public void testPeriodic() {

  }

  @Override
  public void disabledInit() {
  }

  public void display() {

  }
}
