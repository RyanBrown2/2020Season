package frc.autos.modes;

import frc.autos.AutoEndedException;
import frc.autos.actions.DrivePath;
import frc.autos.actions.WaitAction;
import frc.drive.PositionTracker;
import frc.subsystems.Control;
import frc.subsystems.Feeder;
import frc.subsystems.Mixer;
import frc.subsystems.Transport;
import frc.utilPackage.TrapezoidalMp;
import frc.utilPackage.Units;

public class SmallAuto extends AutoMode{
    DrivePath grabLast, toShot;
    WaitAction waitForShooting, waitForShootingAgain;

    Feeder feeder = Feeder.getInstance();
    Mixer mixer = Mixer.getInstance();
    Transport transport = Transport.getInstance();
    Control controller = Control.getInstance();

    public SmallAuto() {
        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 10, 6);
        TrapezoidalMp.constraints revConstraints = new TrapezoidalMp.constraints(0, 10, 6);

        waitForShooting = new WaitAction(3);
        waitForShootingAgain = new WaitAction(3);

        grabLast = DrivePath.createFromFileOnRoboRio("SmallAuto", "grabLast", constraints);
        grabLast.setReverse(false);
        grabLast.setHorizontalThresh(1*Units.Length.feet);
        grabLast.setlookAhead(1.5*Units.Length.feet);

        toShot = DrivePath.createFromFileOnRoboRio("SmallAuto", "toShot", revConstraints);
        toShot.setReverse(true);
        toShot.setHorizontalThresh(1*Units.Length.feet);
        toShot.setlookAhead(1.5*Units.Length.feet);

        setInitPos(0, 0);
    }

    @Override
    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        // Immediately start shooting
        controller.setEnabled(true);
        // Wait to finish shooting
        runAction(waitForShooting);
        controller.setEnabled(false);
        // Stop running subsystems
        mixer.rollers(Mixer.Rollers.off);
        transport.rollers(Transport.Rollers.off);
        // Start up feeder and deploy to grab balls
        feeder.rollers(Feeder.Rollers.maxIn);
        feeder.deploy();
        // Go to the balls
        runAction(grabLast);
        // Start revving up flywheel early
        controller.autoOverride(true);
        controller.setVelocity(3900);
        // Stop the feeder and retract
        feeder.rollers(Feeder.Rollers.off);
        feeder.retract();
        // Go to the last position and shoot
        runAction(toShot);
        controller.setEnabled(true);
        // Wait to stop shooting
        runAction(waitForShootingAgain);
        // Stop shooting after the timer
        controller.autoOverride(false);
        controller.setVelocity(0);
    }
}
