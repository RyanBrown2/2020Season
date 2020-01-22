package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardComponent;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autos.modes.AutoMode;
import frc.autos.modes.PathTest;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.drive.*;
import frc.utilPackage.ScaledDrive;

public class Robot extends TimedRobot {
  private static IControlBoard cb = new ControlBoard();
  public static IControlBoard getControlBoard(){
    return cb;
  }

  int _smoothing = 0;

  TalonSRX testTalon, testTalon2;

  AutoMode auto;

  Drive driveAuto;
  DriveController driveController;
  DriveOutput driveOutput;
  PositionTracker positionTracker;
  ScaledDrive scaledDrive;
  Joystick stick;
  PigeonIMU pigeon;
  double[] ypr;

  Display display;

  boolean resetVision = false;

  @Override
  public void robotInit() {
    pigeon = new PigeonIMU(Constants.Drive.gyro);
    scaledDrive = new ScaledDrive();
    driveAuto = Drive.getInstance();
    driveController = DriveController.getInstance();
    driveOutput = DriveOutput.getInstance();
    driveOutput.start();
    auto = new PathTest();
    positionTracker = PositionTracker.getInstance();
    display = new Display();
    scaledDrive.enabled(true);
    ypr = new double[3];

    stick = new Joystick(0);

    Constants.Drive.left1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.left2.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right2.setIdleMode(CANSparkMax.IdleMode.kBrake);
  }

  @Override
  public void robotPeriodic() {
   Drive.getInstance().display();
   positionTracker.display();
   display();
//   ramseteSetup.periodic();
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
      scaledDrive.run();
  }

  @Override
  public void testPeriodic() {

  }

  @Override
  public void disabledInit() {
  }

  public void display() {
    pigeon.getYawPitchRoll(ypr);
      SmartDashboard.putNumber("yaw", ypr[0]);
  }
}
