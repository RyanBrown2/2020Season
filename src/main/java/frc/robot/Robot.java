package frc.robot;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.autos.modes.AutoMode;
import frc.autos.modes.PathTest;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.drive.*;
import frc.subsystems.Turret;
import frc.utilPackage.ScaledDrive;

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
  ScaledDrive scaledDrive;
  Turret turret;
  Joystick stick;
//  TcpServer tcpServer;

  boolean resetVision = false;

  @Override
  public void robotInit() {
    auto = new PathTest();
    scaledDrive = new ScaledDrive();
    driveAuto = Drive.getInstance();
    driveController = DriveController.getInstance();
    driveOutput = DriveOutput.getInstance();
    driveOutput.start();

    positionTracker = PositionTracker.getInstance();

    scaledDrive = new ScaledDrive();
    scaledDrive.enabled(true);

    turret = new Turret();

    stick = new Joystick(0);

    Constants.Drive.left1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.left2.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right2.setIdleMode(CANSparkMax.IdleMode.kBrake);

//    tcpServer = new TcpServer(Constants.Tcp.port);
//    try {
//      tcpServer.start();
//    } catch (IOException e) {
//      e.printStackTrace();    }

  }

  @Override
  public void robotPeriodic() {
    turret.updateEncoder();
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

  }

  @Override
  public void teleopPeriodic() {
//      scaledDrive.run();
    turret.run();
  }

  @Override
  public void testPeriodic() {

  }

  @Override
  public void disabledInit() {
  }

  public void display() {
    Drive.getInstance().display();
    turret.display();
    positionTracker.display();
  }
}
