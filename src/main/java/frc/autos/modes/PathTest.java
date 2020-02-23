package frc.autos.modes;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.coordinates.Heading;
import frc.robot.Robot;
import frc.subsystems.*;
import frc.utilPackage.Units;
import frc.utilPackage.TrapezoidalMp;

import frc.autos.actions.*;
import frc.drive.PositionTracker;
import frc.autos.AutoEndedException;

public class PathTest extends AutoMode {

    DrivePath underPanel, reverseToShoot, toFirstBall;
    WaitAction waitForFeeder, waitForShooting;
    PointTurn hard90;
    Timer timer;

    public PathTest() {
        timer = new Timer();

        timer.start();

        waitForFeeder = new WaitAction(1.5);
        waitForShooting = new WaitAction(3);

        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 14*Units.Length.feet, 9*Units.Length.feet);
        TrapezoidalMp.constraints revConstraints = new TrapezoidalMp.constraints(0, 14*Units.Length.feet, 9*Units.Length.feet);

        underPanel = DrivePath.createFromFileOnRoboRio("TestAuto", "underPanel", constraints);
        underPanel.setReverse(false);
        underPanel.setHorizontalThresh(1*Units.Length.feet);
        underPanel.setlookAhead(1.5*Units.Length.feet);

        reverseToShoot = DrivePath.createFromFileOnRoboRio("TestAuto", "reverseToShoot", revConstraints);
        reverseToShoot.setReverse(true);
        reverseToShoot.setHorizontalThresh(1*Units.Length.feet);
        reverseToShoot.setlookAhead(1.5*Units.Length.feet);

        hard90 = new PointTurn(new Heading(180*Units.Angle.degrees));

        toFirstBall = DrivePath.createFromFileOnRoboRio("TestAuto", "toFirstBall", constraints);
        toFirstBall.setReverse(false);
        toFirstBall.setHorizontalThresh(1*Units.Length.feet);
        toFirstBall.setlookAhead(2*Units.Length.feet);

        setInitPos(0, 0);

    }

    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        Robot.feeder.deploy();
        Robot.feeder.rollers(Feeder.Rollers.maxIn);
        runAction(underPanel);
        if(underPanel.isFinished()) {
            Robot.feeder.rollers(Feeder.Rollers.off);
            Robot.flywheel.setVelocity(3000);
        }
        runAction(reverseToShoot);
        if(reverseToShoot.isFinished()) {
            Robot.transport.rollers(Transport.Rollers.in);
            Robot.mixer.rollers(Mixer.Rollers.in);
        }
        runAction(waitForFeeder);
        if(waitForFeeder.isFinished()) {
            Robot.feeder.retract();
            Robot.feeder.rollers(Feeder.Rollers.maxIn);
        }
        runAction(waitForShooting);
        if(waitForShooting.isFinished()) {
            // Stop running mechs
            Robot.transport.rollers(Transport.Rollers.off);
            Robot.mixer.rollers(Mixer.Rollers.off);

            // Deploy feeder
            Robot.feeder.deploy();
        }
        runAction(hard90);
        if(hard90.isFinished()) {
            SmartDashboard.putBoolean("Turn Finished", hard90.isFinished());
        }
        runAction(toFirstBall);
        if(toFirstBall.isFinished()) {
            Robot.transport.rollers(Transport.Rollers.in);
            Robot.mixer.rollers(Mixer.Rollers.in);
        }
        runAction(waitForFeeder);
        if(waitForFeeder.isFinished()) {
            Robot.feeder.retract();
            Robot.feeder.rollers(Feeder.Rollers.maxIn);
        }
        runAction(waitForShooting);
        if(waitForShooting.isFinished()) {
            Robot.feeder.rollers(Feeder.Rollers.off);
            Robot.mixer.rollers(Mixer.Rollers.off);
            Robot.flywheel.setVelocity(0);
            Robot.transport.rollers(Transport.Rollers.off);
        }
    }
}