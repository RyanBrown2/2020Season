package frc.robot;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.autos.modes.AutoMode;
import frc.autos.modes.PathTest;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.drive.*;
import frc.subsystems.*;
import frc.utilPackage.ScaledDrive;

public class Robot extends TimedRobot {
  private static IControlBoard cb = new ControlBoard();
  public static IControlBoard getControlBoard(){
    return cb;
  }

  int _smoothing = 0;

  AutoMode auto;

  Compressor compressor;

  Drive driveAuto;
  DriveController driveController;
  DriveOutput driveOutput;
  PositionTracker positionTracker;

  ScaledDrive scaledDrive;

  IntakeController intake;

  Transport transport;
  Flywheel flywheel;

  @Override
  public void robotInit() {
    Constants.Drive.pigeon.setYaw(0);

    auto = new PathTest();
    scaledDrive = new ScaledDrive();
    driveAuto = Drive.getInstance();
    driveController = DriveController.getInstance();
    driveOutput = DriveOutput.getInstance();
    driveOutput.start();

    positionTracker = PositionTracker.getInstance();

    scaledDrive = new ScaledDrive();
    scaledDrive.enabled(true);

    intake = IntakeController.getInstance();


    transport = new Transport();
    flywheel = new Flywheel();

//    compressor = new Compressor(0);
//    compressor.setClosedLoopControl(true);

    Constants.Drive.left1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.left2.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right2.setIdleMode(CANSparkMax.IdleMode.kBrake);

  }

  @Override
  public void robotPeriodic() {
//    turret.updateEncoder(false);
    display();
  }

  @Override
  public void autonomousInit() {
    auto.start();
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    flywheel.setVelocity(2400);
  }

  @Override
  public void teleopPeriodic() {
      flywheel.run();
//    if(cb.feederActuate()) {
//      intake.feederActuate();
//    }
//
//    if(cb.rollers()) {
//      intake.rollers();
//    }
  }

  @Override
  public void testPeriodic() {

  }

  @Override
  public void disabledInit() {
  }

  public void panic() {

  }

  public void display() {
    Drive.getInstance().display();
    positionTracker.display();
    flywheel.display();
  }
}
