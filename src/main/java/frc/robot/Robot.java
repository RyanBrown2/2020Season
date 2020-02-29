package frc.robot;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autos.modes.AutoMode;
import frc.autos.modes.FiveThenFour;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.drive.Drive;
import frc.drive.DriveController;
import frc.drive.DriveOutput;
import frc.drive.PositionTracker;
import frc.subsystems.Climber;
import frc.subsystems.Control;
import frc.utilPackage.ScaledDrive;

public class Robot extends TimedRobot {
  private static IControlBoard cb = new ControlBoard();

  public static IControlBoard getControlBoard(){
    return cb;
  }

  public static ScaledDrive scaledDrive = new ScaledDrive();

  TeleopControls teleopControls;

  AutoMode auto;

  Compressor compressor;

  Drive driveAuto;
  DriveController driveController;
  DriveOutput driveOutput;
  PositionTracker positionTracker;

  Control controller;
  Climber climber;

//  FunctionTest functionTest;

  @Override
  public void robotInit() {
    teleopControls = new TeleopControls();

    Constants.Drive.pigeon.setYaw(0);

    climber = Climber.getInstance();

    auto = new FiveThenFour();
    driveAuto = Drive.getInstance();
    driveController = DriveController.getInstance();
    driveOutput = DriveOutput.getInstance();
    driveOutput.start();

    positionTracker = PositionTracker.getInstance();

    scaledDrive.enabled(true);

    compressor = new Compressor(0);
    compressor.setClosedLoopControl(true);

//    functionTest = new FunctionTest();

    controller = Control.getInstance();

    // Set drivebase motor idle modes to brake
    Constants.Drive.left1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.left2.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right2.setIdleMode(CANSparkMax.IdleMode.kBrake);

    Constants.Drive.left1.setSmartCurrentLimit(45);
    Constants.Drive.left2.setSmartCurrentLimit(45);
    Constants.Drive.right1.setSmartCurrentLimit(45);
    Constants.Drive.right2.setSmartCurrentLimit(45);

  }

  @Override
  public void robotPeriodic() {
    display();
  }

  @Override
  public void autonomousInit() {
    auto.start();

  }

  @Override
  public void autonomousPeriodic() {
    controller.run();
  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {
    controller.run();
    teleopControls.run();
//    scaledDrive.run();
    climber.run();
  }

  @Override
  public void testPeriodic() {
//    functionTest.reset();
//    functionTest.run();
  }

  @Override
  public void disabledInit() {
  }

  public void display() {
    Drive.getInstance().display();
    positionTracker.display();
    teleopControls.display();
    controller.display();
    SmartDashboard.putBoolean("Climb Limit State", Constants.Climber.climbLimit.get());
  }
}
