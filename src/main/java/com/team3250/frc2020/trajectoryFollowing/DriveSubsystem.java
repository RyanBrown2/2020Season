package com.team3250.frc2020.trajectoryFollowing;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.util.Units;
import com.team3250.frc2020.Constants;

import java.util.List;

public class DriveSubsystem {
    DifferentialDrive m_drive = new DifferentialDrive(Constants.Drive.left1, Constants.Drive.right1);
    DifferentialDriveOdometry m_odometry;
    DifferentialDriveKinematics m_kinematics;
    RamseteController ramseteController;
    Timer timer;
    double ypr[];

    TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(4), Units.feetToMeters(2));
    Trajectory exampleTrajectory;


    public DriveSubsystem() {
        m_kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(26));
        ramseteController = new RamseteController(2.0, 0.7);
        m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()), new Pose2d(0, 0, new Rotation2d(0)));
        Constants.Drive.right2.follow(Constants.Drive.right1, false);
        Constants.Drive.left2.follow(Constants.Drive.left1, false);
        timer.start();

        exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d(0)),
                // Pass through these two interior waypoints, making an 's' curve path
                List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(3, 0, new Rotation2d(0)),
                // Pass config
                config
        );

        Constants.Drive.left1.getPIDController().setP(1);
        Constants.Drive.right1.getPIDController().setP(1);
        m_drive.setSafetyEnabled(false);
    }

    public void run() {
        m_odometry.update(Rotation2d.fromDegrees(getHeading()), getLeftEncoderDistance(), getRightEncoderDistance());
        SmartDashboard.putNumber("Current X Pose", m_odometry.getPoseMeters().getTranslation().getX());
        SmartDashboard.putNumber("Currrent Y Pose", m_odometry.getPoseMeters().getTranslation().getY());
//        Trajectory.State goal = exampleTrajectory.sample(timer.get() + 0.05);
//        ChassisSpeeds adjustedSpeeds = ramseteController.calculate(getPose(), goal);
//        DifferentialDriveWheelSpeeds wheelSpeeds = m_kinematics.toWheelSpeeds(adjustedSpeeds);
//        Constants.Drive.left1.getPIDController().setReference(wheelSpeeds.leftMetersPerSecond, ControlType.kVelocity);
//        Constants.Drive.right1.getPIDController().setReference(wheelSpeeds.rightMetersPerSecond, ControlType.kVelocity);
//        m_drive.feed();
    }

    public double getLeftEncoderDistance() {
        double rawTicks = Constants.Drive.leftEncoder.getSelectedSensorPosition();
        return rawTicks*(360/4096)*5.2*2*3.14159;
    }

    public double getRightEncoderDistance() {
        double rawTicks = Constants.Drive.rightEncoder.getSelectedSensorPosition();
        return rawTicks*(360/4096)*5.2*2*3.14159;
    }

    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }



    /**
     * Resets the odometry to the specified pose.
     *
     * @param pose The pose to which to set the odometry.
     */
    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
    }

    /**
     * Drives the robot using arcade controls.
     *
     * @param fwd the commanded forward movement
     * @param rot the commanded rotation
     */
    public void arcadeDrive(double fwd, double rot) {
        m_drive.arcadeDrive(fwd, rot);
    }

    /**
     * Controls the left and right sides of the drive directly with voltages.
     *
     * @param leftVolts  the commanded left output
     * @param rightVolts the commanded right output
     */
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        Constants.Drive.left1.setVoltage(leftVolts);
        Constants.Drive.right1.setVoltage(-rightVolts);
        m_drive.feed();
    }

    /**
     * Resets the drive encoders to currently read a position of 0.
     */
    public void resetEncoders() {
        Constants.Drive.leftEncoder.setSelectedSensorPosition(0);
        Constants.Drive.rightEncoder.setSelectedSensorPosition(0);
    }

    /**
     * Gets the average distance of the two encoders.
     *
     * @return the average of the two encoder readings
     */
    public double getAverageEncoderDistance() {
        return (Units.feetToMeters(getLeftEncoderDistance() + getRightEncoderDistance()) / 2.0);
    }

    /**
     * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
     *
     * @param maxOutput the maximum output to which the drive will be constrained
     */
    public void setMaxOutput(double maxOutput) {
        m_drive.setMaxOutput(maxOutput);
    }

    /**
     * Zeroes the heading of the robot.
     */
    public void zeroHeading() {
        Constants.Drive.pigeon.setYaw(0);
    }

    public double getGyroAngle() {
        Constants.Drive.pigeon.getYawPitchRoll(ypr);
        return ypr[0];
    }

    /**
     * Returns the heading of the robot.
     *
     * @return the robot's heading in degrees, from -180 to 180
     */
    public double getHeading() {
        return Math.IEEEremainder(getGyroAngle(), 360);
    }
}
