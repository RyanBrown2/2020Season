package frc.drive;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.Constants;

import java.util.List;

public class TrajectoryDrive {
    RamseteSetup m_robotDrive = new RamseteSetup();
    DifferentialDriveVoltageConstraint autoVoltageConstraint =
            new DifferentialDriveVoltageConstraint(
                    new SimpleMotorFeedforward(Constants.RamseteParams.ksVolts,
                            Constants.RamseteParams.kvVoltSecondsPerMeter,
                            Constants.RamseteParams.kaVoltSecondsSquaredPerMeter),
                    Constants.RamseteParams.kDriveKinematics,
                    10);

    // Create config for trajectory
    TrajectoryConfig config =
            new TrajectoryConfig(Constants.RamseteParams.kMaxSpeedMetersPerSecond,
                    Constants.RamseteParams.kMaxAccelerationMetersPerSecondSquared)
                    // Add kinematics to ensure max speed is actually obeyed
                    .setKinematics(Constants.RamseteParams.kDriveKinematics)
                    // Apply the voltage constraint
                    .addConstraint(autoVoltageConstraint);

    // An example trajectory to follow.  All units in meters.
    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
            // Start at the origin facing the +X direction
            new Pose2d(0, 0, new Rotation2d(0)),
            // Pass through these two interior waypoints, making an 's' curve path
            List.of(
                    new Translation2d(1, 1),
                    new Translation2d(2, -1)
            ),
            // End 3 meters straight ahead of where we started, facing forward
            new Pose2d(3, 0, new Rotation2d(0)),
            // Pass config
            config
    );

    Pose2d drivePose = m_robotDrive.getPose();
//    RamseteSetup ramseteCommand = new RamseteSetup(
//            exampleTrajectory,
//            drivePose,
//            new RamseteController(Constants.RamseteParams.kRamseteB, Constants.RamseteParams.kRamseteZeta),
//            new SimpleMotorFeedforward(Constants.RamseteParams.ksVolts,
//                    Constants.RamseteParams.kvVoltSecondsPerMeter,
//                    Constants.RamseteParams.kaVoltSecondsSquaredPerMeter),
//            Constants.RamseteParams.kDriveKinematics,
//            m_robotDrive::getWheelSpeeds,
//            new PIDController(Constants.RamseteParams.kPDriveVel, 0, 0),
//            new PIDController(Constants.RamseteParams.kPDriveVel, 0, 0),
//            // RamseteCommand passes volts to the callback
//            m_robotDrive::tankDriveVolts,
//            m_robotDrive
//    );

    // Run path following command, then stop at the end.
//    return ramseteSetup.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
}
