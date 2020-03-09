package frc.robot;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.autos.modes.AutoMode;
import frc.autos.modes.SmallAuto;
import frc.autos.modes.ThreeThenFive;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.display.DriverDisplay;
import frc.drive.Drive;
import frc.drive.DriveController;
import frc.drive.DriveOutput;
import frc.drive.PositionTracker;
import frc.subsystems.Climber;
import frc.subsystems.Control;
import frc.subsystems.Flywheel;
import frc.subsystems.Turret;
import frc.utilPackage.ScaledDrive;

public class Robot extends TimedRobot {
  private static IControlBoard cb = new ControlBoard();

  public static IControlBoard getControlBoard(){
    return cb;
  }

  public static ScaledDrive scaledDrive = new ScaledDrive();

  public static DriverDisplay driverDisplay = new DriverDisplay();

  TeleopControls teleopControls;

  AutoMode auto;


  Compressor compressor;

  Drive driveAuto;
  DriveController driveController;
  DriveOutput driveOutput;
  PositionTracker positionTracker;

  Control controller;
  Climber climber;

  Turret turret = Turret.getInstance(); // todo

//  FunctionTest functionTest;

  @Override
  public void robotInit() {

    teleopControls = new TeleopControls();

    Constants.Drive.pigeon.setYaw(0);

    climber = Climber.getInstance();

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
    driverDisplay.setMatchTime(DriverStation.getInstance().getMatchTime());
    display();
  }

  @Override
  public void autonomousInit() {
    auto = new SmallAuto();
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
    scaledDrive.run();
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
//    flywheel.display();
    Drive.getInstance().display();
    positionTracker.display();
    teleopControls.display();
    controller.display();
  }
}
