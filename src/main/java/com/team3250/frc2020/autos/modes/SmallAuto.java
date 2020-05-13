package com.team3250.frc2020.autos.modes;

import com.team3250.frc2020.autos.AutoEndedException;
import com.team3250.frc2020.autos.actions.DrivePath;
import com.team3250.frc2020.autos.actions.WaitAction;
import com.team3250.frc2020.drive.PositionTracker;
import com.team3250.frc2020.Constants;
import com.team3250.frc2020.subsystems.*;
import com.team3250.frc2020.utilPackage.TrapezoidalMp;
import com.team3250.frc2020.utilPackage.Units;

public class SmallAuto extends AutoMode{
    DrivePath grabLast;
    WaitAction waitForShooting, waitForTurret;

    Turret turret = Turret.getInstance();
    Feeder feeder = Feeder.getInstance();
    Mixer mixer = Mixer.getInstance();
    Transport transport = Transport.getInstance();
    Control controller = Control.getInstance();

    public SmallAuto() {
        TrapezoidalMp.constraints constraints = new TrapezoidalMp.constraints(0, 10, 6);

        waitForShooting = new WaitAction(4);
        waitForTurret = new WaitAction(1.25);

        grabLast = DrivePath.createFromFileOnRoboRio("SmallAuto", "grabLast", constraints);
        grabLast.setReverse(false);
        grabLast.setHorizontalThresh(1*Units.Length.feet);
        grabLast.setlookAhead(1.5*Units.Length.feet);

        setInitPos(0, 0);
    }

    @Override
    public void auto() throws AutoEndedException {
        PositionTracker.getInstance().robotForward();
        turret.toSetpoint(Constants.pi);
        runAction(waitForTurret);
        // Immediately start shooting
        controller.setEnabled(true);
        // Wait to finish shooting
        runAction(waitForShooting);
        controller.setEnabled(false);
        // Stop running subsystems
        mixer.rollers(Mixer.Rollers.off);
        transport.rollers(Transport.Rollers.off);
        feeder.rollers(Feeder.Rollers.off);
        // Move from line
        runAction(grabLast);
    }
}
