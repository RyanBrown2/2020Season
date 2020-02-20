package frc.robot;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autos.modes.AutoMode;
import frc.autos.modes.PathTest;
import frc.controlBoard.ControlBoard;
import frc.controlBoard.IControlBoard;
import frc.display.UtilDisplay;
import frc.drive.*;
import frc.subsystems.*;
import frc.utilPackage.ScaledDrive;

public class Robot extends TimedRobot {
  private static IControlBoard cb = new ControlBoard();
  public static IControlBoard getControlBoard(){
    return cb;
  }

  TeleopControls teleopControls;

  AutoMode auto;

  Compressor compressor;

  Drive driveAuto;
  DriveController driveController;
  DriveOutput driveOutput;
  PositionTracker positionTracker;

  IntakeController intake;

  Hood hood;
  Turret turret;

  ScaledDrive drive;

  PowerDistributionPanel pdp;
  UtilDisplay utilDisplay;

  @Override
  public void robotInit() {
    teleopControls = new TeleopControls();

    Constants.Drive.pigeon.setYaw(0);

    auto = new PathTest();
    driveAuto = Drive.getInstance();
    driveController = DriveController.getInstance();
    driveOutput = DriveOutput.getInstance();
    driveOutput.start();

    positionTracker = PositionTracker.getInstance();

    intake = IntakeController.getInstance();

    hood = new Hood();
    turret = new Turret();

    drive = new ScaledDrive();
    drive.enabled(true);

    pdp = new PowerDistributionPanel();

    compressor = new Compressor(0);
    compressor.setClosedLoopControl(true);

    utilDisplay = new UtilDisplay();

    Constants.Drive.left1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.left2.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right1.setIdleMode(CANSparkMax.IdleMode.kBrake);
    Constants.Drive.right2.setIdleMode(CANSparkMax.IdleMode.kBrake);

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
      TeleopControls.flywheel.run();
  }

  @Override
  public void teleopInit() {
//    flywheel.setVelocity(3500);
      turret.toSetpoint(0);
    teleopControls.resetTimers();
  }

  @Override
  public void teleopPeriodic() {
    turret.run();
    teleopControls.run();
    drive.run();
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
    SmartDashboard.putNumber("Raw Encoder Val", Constants.Turret.turretEnc.getSelectedSensorPosition());
    Drive.getInstance().display();
    positionTracker.display();
//    flywheel.display();
      teleopControls.display();
    turret.display();
    SmartDashboard.putNumber("Battery", pdp.getVoltage());
    utilDisplay.battery(pdp.getVoltage());
  }
}
