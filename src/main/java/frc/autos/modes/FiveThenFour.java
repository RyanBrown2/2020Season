package frc.autos.modes;

import frc.autos.AutoEndedException;
import frc.autos.actions.DrivePath;
import frc.autos.actions.PointTurn;
import frc.autos.actions.WaitAction;
import frc.coordinates.Heading;
import frc.drive.PositionTracker;
import frc.robot.Robot;
import frc.subsystems.Feeder;
import frc.subsystems.Mixer;
import frc.subsystems.Transport;
import frc.utilPackage.TrapezoidalMp;
import frc.utilPackage.Units;

public class FiveThenFour extends AutoMode {
    DrivePath firstBalls, throughTunnel, reverse, lastBalls;
    WaitAction waitForShooting, waitForShootingAgain, waitForFeeder;
    PointTurn ninety;

    boolean shooting = false;

    public FiveThenFour() {
        waitForShooting = new WaitAction(5);
        waitForShootingAgain = new WaitAction(5);
        waitForFeeder = new WaitAction(2);

        ninety = new PointTurn(new Heading(180* Units.Angle.degrees));

        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 14*Units.Length.feet, 9*Units.Length.feet);
        TrapezoidalMp.constraints revConstraints = new TrapezoidalMp.constraints(0, 14*Units.Length.feet, 9*Units.Length.feet);

        firstBalls = DrivePath.createFromFileOnRoboRio("FiveThenFour", "firstBalls", constraints);
        firstBalls.setReverse(false);
        firstBalls.setlookAhead(1.5*Units.Length.feet);
        firstBalls.setHorizontalThresh(1*Units.Length.feet);

        throughTunnel = DrivePath.createFromFileOnRoboRio("FiveThenFour", "throughTunnel", constraints);
        throughTunnel.setReverse(false);
        throughTunnel.setlookAhead(1.5*Units.Length.feet);
        throughTunnel.setHorizontalThresh(1*Units.Length.feet);

        reverse = DrivePath.createFromFileOnRoboRio("FiveThenFour", "reverse", revConstraints);
        reverse.setReverse(true);
        reverse.setlookAhead(1.5*Units.Length.feet);
        reverse.setHorizontalThresh(1*Units.Length.feet);

        lastBalls = DrivePath.createFromFileOnRoboRio("FiveThenFour", "lastBalls", constraints);
        lastBalls.setReverse(false);
        lastBalls.setTurnCorrection(1);
        lastBalls.setlookAhead(1.5*Units.Length.feet);
        lastBalls.setHorizontalThresh(1*Units.Length.feet);

        setInitPos(0, 0);
    }

    @Override
    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        Robot.feeder.deploy();
        Robot.feeder.rollers(Feeder.Rollers.maxIn);
        Robot.shooterController.setFlywheel(3000);

        runAction(firstBalls);
        if(firstBalls.isFinished()) {
            Robot.feeder.retract();
            Robot.feeder.rollers(Feeder.Rollers.off);
            Robot.intakeController.setEnabled(true);
        }

        runAction(waitForShooting);
        if(waitForShooting.isFinished()) {
            Robot.intakeController.setEnabled(false);
            Robot.transport.rollers(Transport.Rollers.off);
            Robot.mixer.rollers(Mixer.Rollers.off);
            Robot.shooterController.setFlywheel(0);
            Robot.feeder.deploy();
            Robot.feeder.rollers(Feeder.Rollers.maxIn);
        }

        runAction(waitForFeeder);

        runAction(throughTunnel);
        runAction(reverse);
        runAction(ninety);
        if(ninety.isFinished()) {
            Robot.shooterController.setFlywheel(3000);
            Robot.feeder.deploy();
            Robot.feeder.rollers(Feeder.Rollers.maxIn);
        }

        runAction(lastBalls);

        if(lastBalls.isFinished()) {
            Robot.feeder.retract();
            Robot.intakeController.setEnabled(true);
        }

        runAction(waitForShootingAgain);
        if(waitForShootingAgain.isFinished()) {
            Robot.shooterController.setFlywheel(0);
            Robot.intakeController.setEnabled(false);
            Robot.feeder.rollers(Feeder.Rollers.off);
            Robot.transport.rollers(Transport.Rollers.off);
            Robot.mixer.rollers(Mixer.Rollers.off);
        }
    }
}
