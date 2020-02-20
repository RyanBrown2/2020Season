package frc.autos.modes;

import edu.wpi.first.wpilibj.Timer;
import frc.coordinates.Heading;
import frc.drive.Drive;
import frc.robot.TeleopControls;
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
        toFirstBall.setlookAhead(1*Units.Length.feet);

        setInitPos(0, 0);

    }

    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        TeleopControls.feeder.actuate();
        TeleopControls.feeder.rollers(Feeder.Rollers.maxIn);
        runAction(underPanel);
        if(underPanel.isFinished()) {
            TeleopControls.feeder.rollers(Feeder.Rollers.off);
            TeleopControls.flywheel.setVelocity(2000);
        }
        runAction(reverseToShoot);
        if(reverseToShoot.isFinished()) {
            TeleopControls.transport.runRamp(1, false);
            TeleopControls.mixer.rollers(Mixer.Rollers.in);
        }
        runAction(waitForFeeder);
        if(waitForFeeder.isFinished()) {
            TeleopControls.feeder.actuate();
            TeleopControls.feeder.rollers(Feeder.Rollers.maxIn);
        }
        runAction(waitForShooting);
        if(waitForShooting.isFinished()) {
            // Stop running mechs
            TeleopControls.transport.runRamp(0, false);
            TeleopControls.mixer.rollers(Mixer.Rollers.off);

            // Deploy feeder
            TeleopControls.feeder.actuate();
        }
        runAction(hard90);
        runAction(toFirstBall);
        if(toFirstBall.isFinished()) {
            TeleopControls.transport.runRamp(1, false);
            TeleopControls.mixer.rollers(Mixer.Rollers.in);
        }
        runAction(waitForFeeder);
        if(waitForFeeder.isFinished()) {
            TeleopControls.feeder.actuate();
            TeleopControls.feeder.rollers(Feeder.Rollers.maxIn);
        }
        runAction(waitForShooting);
        if(waitForShooting.isFinished()) {
            TeleopControls.feeder.rollers(Feeder.Rollers.off);
            TeleopControls.mixer.rollers(Mixer.Rollers.off);
            TeleopControls.flywheel.setVelocity(0);
            TeleopControls.transport.runRamp(0, false);
        }
    }
}