package frc.robot;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;
import frc.autos.modes.AutoMode;
import frc.autos.modes.FiveThenFour;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.display.UtilDisplay;
import frc.drive.*;
import frc.subsystems.*;
import frc.util.udpServer;
import frc.utilPackage.ScaledDrive;

import java.io.IOException;

public class Robot extends TimedRobot {
  private static IControlBoard cb = new ControlBoard();

  public static IControlBoard getControlBoard(){
    return cb;
  }

  public static Feeder feeder = new Feeder();
  public static Mixer mixer = new Mixer();
  public static Transport transport = new Transport();
  public static Flywheel flywheel = new Flywheel();
  public static Turret turret = new Turret();
  public static Hood hood = new Hood();

  public static IntakeController intakeController = IntakeController.getInstance();
  public static ShooterControl shooterController = ShooterControl.getInstance();

  public static ScaledDrive scaledDrive = new ScaledDrive();

//  tcpServer server = tcpServer.getInstance();

  TeleopControls teleopControls;

  AutoMode auto;

  Compressor compressor;

  Drive driveAuto;
  DriveController driveController;
  DriveOutput driveOutput;
  PositionTracker positionTracker;

  UtilDisplay utilDisplay;

//  FunctionTest functionTest;
  ColorWheel colorWheel;

  frc.util.udpServer udpServer;

  @Override
  public void robotInit() {
    teleopControls = new TeleopControls();

    Constants.Drive.pigeon.setYaw(0);

    auto = new FiveThenFour();
    driveAuto = Drive.getInstance();
    driveController = DriveController.getInstance();
    driveOutput = DriveOutput.getInstance();
    driveOutput.start();

    positionTracker = PositionTracker.getInstance();

    scaledDrive.enabled(true);

    compressor = new Compressor(0);
    compressor.setClosedLoopControl(true);

    utilDisplay = new UtilDisplay();

//    functionTest = new FunctionTest();

    colorWheel = new ColorWheel();

    try {
      udpServer = new udpServer(5100);
      Thread thread = new Thread(udpServer);
      thread.start();
    } catch (IOException e) {
      e.printStackTrace();
    }

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
//    try {
//      double data = server.getData()[0];
//      SmartDashboard.putNumber("TestData", data);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    SmartDashboard.putString("Color", colorWheel.getColor());
  }

  @Override
  public void autonomousInit() {
    auto.start();
  }

  @Override
  public void autonomousPeriodic() {
    intakeController.runIntake();
    flywheel.run();
  }

  @Override
  public void teleopInit() {
    turret.toSetpoint(0);
    intakeController.setEnabled(false);
    shooterController.setEnabled(false);
    shooterController.setFlywheel(0);
  }

  @Override
  public void teleopPeriodic() {
    intakeController.runIntake();
    shooterController.run();
//    shooterController.trackVision();
    flywheel.run();
    turret.run();
    teleopControls.run();
    scaledDrive.run();
  }

  @Override
  public void testPeriodic() {
//    functionTest.reset();
//    functionTest.run();
  }

  @Override
  public void disabledInit() {
  }

  public void panic() {

  }

  public void display() {
    Drive.getInstance().display();
    positionTracker.display();
    teleopControls.display();
    turret.display();
    shooterController.display();
  }
}
