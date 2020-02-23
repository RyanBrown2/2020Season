package frc.robot;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autos.modes.AutoMode;
import frc.autos.modes.FiveThenFour;
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

  public static Feeder feeder = new Feeder();
  public static Mixer mixer = new Mixer();
  public static Transport transport = new Transport();
  public static Flywheel flywheel = new Flywheel();
  public static Turret turret = new Turret();
  public static Hood hood = new Hood();

  public static IntakeController intakeController = IntakeController.getInstance();

  public static ScaledDrive scaledDrive = new ScaledDrive();

  TeleopControls teleopControls;

  AutoMode auto;

  Compressor compressor;

  Drive driveAuto;
  DriveController driveController;
  DriveOutput driveOutput;
  PositionTracker positionTracker;

  PowerDistributionPanel pdp;
  UtilDisplay utilDisplay;

  FunctionTest functionTest;

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

    pdp = new PowerDistributionPanel();

    compressor = new Compressor(0);
    compressor.setClosedLoopControl(true);

    utilDisplay = new UtilDisplay();

    functionTest = new FunctionTest();

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
    intakeController.runIntake();
    flywheel.run();
  }

  @Override
  public void teleopInit() {
    turret.toSetpoint(0);
    intakeController.setEnabled(false);
    intakeController.setVelocity(0);
  }

  @Override
  public void teleopPeriodic() {
    intakeController.runIntake();
    flywheel.run();
    turret.run();
    teleopControls.run();
    scaledDrive.run();
  }

  @Override
  public void testPeriodic() {
    functionTest.reset();
    functionTest.run();
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
    SmartDashboard.putNumber("Battery", pdp.getVoltage());
    utilDisplay.battery(pdp.getVoltage());
  }
}
