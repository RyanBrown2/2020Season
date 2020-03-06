package frc.autos.modes;

import frc.autos.AutoEndedException;
import frc.autos.actions.DrivePath;
import frc.autos.actions.PointTurn;
import frc.autos.actions.Shoot;
import frc.autos.actions.WaitAction;
import frc.coordinates.Heading;
import frc.drive.PositionTracker;
import frc.subsystems.Control;
import frc.subsystems.Feeder;
import frc.subsystems.Mixer;
import frc.subsystems.Transport;
import frc.utilPackage.TrapezoidalMp;
import frc.utilPackage.Units;

public class ThreeThenFive extends AutoMode {
    DrivePath throughTunnel, reverse;
    WaitAction waitForFeeder, waitForShooting, waitForShootingAgain;
    PointTurn hardTurn;
    Shoot shooting;

    Feeder feeder = Feeder.getInstance();
    Mixer mixer = Mixer.getInstance();
    Transport transport = Transport.getInstance();
    Control controller = Control.getInstance();

    public ThreeThenFive() {
        waitForShooting = new WaitAction(4.25);  // 2.75
        waitForShootingAgain = new WaitAction(5);
        waitForFeeder = new WaitAction(0.75); // 0.75

        shooting = new Shoot();

        hardTurn = new PointTurn(new Heading(35*Units.Angle.degrees));

        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 14*Units.Length.feet, 9*Units.Length.feet);
        TrapezoidalMp.constraints revConstraints = new TrapezoidalMp.constraints(0, 14*Units.Length.feet, 9*Units.Length.feet);

        throughTunnel = DrivePath.createFromFileOnRoboRio("ThreeThenFive", "throughTunnel", constraints);
        throughTunnel.setReverse(false);
        throughTunnel.setlookAhead(3 * Units.Length.feet);
        throughTunnel.setHorizontalThresh(1 * Units.Length.feet);

        reverse = DrivePath.createFromFileOnRoboRio("ThreeThenFive", "reverse", revConstraints);
        reverse.setReverse(true);
        reverse.setlookAhead(1.5 * Units.Length.feet);
        reverse.setHorizontalThresh(1 * Units.Length.feet);

        setInitPos(0, 0);
    }

    @Override
    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        // Put feeder out then back in
        runAction(waitForFeeder);
        // Immediately start shooting
        controller.setEnabled(true);
        // Wait for shooting to finish
        runAction(waitForShooting);
        // Stop shooting after timer
        controller.setEnabled(false);
        // Slow subsystems to pull a single ball in, and run feeder
        mixer.rollers(Mixer.Rollers.slowIn);
        transport.rollers(Transport.Rollers.onlyFront);
        feeder.deploy();
        feeder.rollers(Feeder.Rollers.maxIn);
        // Drive underneath the tunnel
        runAction(throughTunnel);
        // Stop extra running subsystems
        mixer.rollers(Mixer.Rollers.off);
        transport.rollers(Transport.Rollers.off);
        // Rev up the flywheel in preparation for the next shot
        controller.autoOverride(true);
        controller.setVelocity(3900);
        // Retract and stop the feeder in preparation to shoot
        feeder.rollers(Feeder.Rollers.off);
        feeder.retract();
        // Reverse out from under the trench and go to final position
        runAction(reverse);
        // Start shooting
        controller.setEnabled(true);
        runAction(waitForShootingAgain);
        // Stop all running subsystems
        controller.setEnabled(false);
        controller.autoOverride(false);
        mixer.rollers(Mixer.Rollers.off);
        transport.rollers(Transport.Rollers.off);
        feeder.rollers(Feeder.Rollers.off);
    }
}
