package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autos.actions.VisionPursuit;
import frc.autos.modes.AutoMode;
import frc.autos.modes.PathTest;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.drive.Drive;
import frc.drive.DriveController;
import frc.drive.DriveOutput;
import frc.drive.PositionTracker;
import frc.util.Jevois;
import frc.utilPackage.TeleopDrive;

public class Robot extends TimedRobot {
  private static IControlBoard cb = new ControlBoard();
  public static IControlBoard getControlBoard(){
    return cb;
  }

  int _smoothing = 0;

  AutoMode auto;

  Drive driveAuto;
  DriveController driveController;
  DriveOutput driveOutput;
  PositionTracker positionTracker;

  boolean resetVision = false;

  @Override
  public void robotInit() {
    driveAuto = Drive.getInstance();
    driveController = DriveController.getInstance();
    driveOutput = DriveOutput.getInstance();
    driveOutput.start();
    auto = new PathTest();
   positionTracker = PositionTracker.getInstance();
  }

  @Override
  public void robotPeriodic() {
   Drive.getInstance().display();
   positionTracker.display();
//    display();
  }

  @Override
  public void autonomousInit() {
//    auto.start();
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopPeriodic() {
      driveAuto.outputToDrive(1, 1);

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
