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
import frc.subsystems.Controller;
import frc.util.udpServer;
import frc.utilPackage.ScaledDrive;

import java.io.IOException;

public class Robot extends TimedRobot {
  private static IControlBoard cb = new ControlBoard();

  public static IControlBoard getControlBoard(){
    return cb;
  }

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

  Controller controller;

//  FunctionTest functionTest;

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

    controller = Controller.getInstance();

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
  }
}
