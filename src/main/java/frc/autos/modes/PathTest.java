package frc.autos.modes;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.TeleopControls;
import frc.subsystems.*;
import frc.utilPackage.Units;
import frc.utilPackage.TrapezoidalMp;

import frc.autos.actions.*;
import frc.drive.PositionTracker;
import frc.autos.AutoEndedException;

public class PathTest extends AutoMode {

    DrivePath underPanel, reverseToShoot, toFirstBall;
    WaitAction wait5;
    Feeder feeder;
    Mixer mixer;
    Transport transport;
    Turret turret;
    Timer timer;

    public PathTest() {
        feeder = new Feeder();
        mixer = new Mixer();
        transport = new Transport();
        turret = new Turret();
        timer = new Timer();

        timer.start();

        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 10*Units.Length.feet, 5*Units.Length.feet);
        TrapezoidalMp.constraints revConstraints = new TrapezoidalMp.constraints(0, 10*Units.Length.feet, 5*Units.Length.feet);

        underPanel = DrivePath.createFromFileOnRoboRio("TestAuto", "underPanel", constraints);
        underPanel.setReverse(false);
        underPanel.setHorizontalThresh(1*Units.Length.feet);
        underPanel.setlookAhead(1*Units.Length.feet);

        reverseToShoot = DrivePath.createFromFileOnRoboRio("TestAuto", "reverseToShoot", revConstraints);
        reverseToShoot.setReverse(true);
        reverseToShoot.setHorizontalThresh(1*Units.Length.feet);
        reverseToShoot.setlookAhead(1*Units.Length.feet);

        toFirstBall = DrivePath.createFromFileOnRoboRio("TestAuto", "toFirstBall", constraints);
        toFirstBall.setReverse(false);
        toFirstBall.setHorizontalThresh(1*Units.Length.feet);
        toFirstBall.setlookAhead(1*Units.Length.feet);

        setInitPos(0, 0);

    }

    public void auto() throws AutoEndedException {
        TeleopControls.flywheel.run();
        PositionTracker.getInstance().robotForward();
        feeder.actuate();
        feeder.rollers(Feeder.Rollers.maxIn);
        runAction(underPanel);
        if(underPanel.isFinished()) {
            feeder.rollers(Feeder.Rollers.off);
            TeleopControls.flywheel.setVelocity(2000);
        }
        runAction(reverseToShoot);
        if(reverseToShoot.isFinished()) {
            if(Math.abs(TeleopControls.flywheel.getVelocity() - 2000) < 200) {
                transport.runRamp(1, false);
                if ((timer.get() % 1.25) <= 1) {
                    mixer.rollers(Mixer.Rollers.in);
                } else if (timer.get() >= 1.5) {
                    feeder.rollers(Feeder.Rollers.in);
                    mixer.rollers(Mixer.Rollers.off);
                } else {
                    mixer.rollers(Mixer.Rollers.off);
                }
            }
        }
        runAction(wait5);
        runAction(toFirstBall);
    }
}