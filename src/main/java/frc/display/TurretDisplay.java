package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class TurretDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry turretAngle;
    NetworkTableEntry turretAngleFieldOriented;
    NetworkTableEntry turretSetpoint;
    NetworkTableEntry atSetpoint;
    NetworkTableEntry resetTurret;

    public TurretDisplay() {
        tab = Shuffleboard.getTab("Shooter");

        turretAngle =
                tab.add("Turret Angle", 0)
                        .withPosition(0, 0)
                        .getEntry();

        turretSetpoint =
                tab.add("Turret Setpoint", 0)
                        .withPosition(1, 0)
                        .getEntry();

        turretAngleFieldOriented =
                tab.add("Field Oriented Angle", 0)
                .withPosition(2, 0)
                .getEntry();

        atSetpoint =
                tab.add("At Setpoint", false)
                .withPosition(3,0)
                .getEntry();

        resetTurret =
                tab.add("Reset Turret", false)
                .withPosition(4, 0)
                .getEntry();
    }

    public void angle(double angle) {
        turretAngle.setDouble(angle);
    }

    public void fieldOrientedAngle(double angle) {
        turretAngleFieldOriented.setDouble(angle);
    }

    public void setpoint(double setpoint) {
        turretSetpoint.setDouble(setpoint);
    }

    public void atSetpoint(boolean atSetpoint) {
        this.atSetpoint.setBoolean(atSetpoint);
    }

    public boolean turretReset() {
        return resetTurret.getBoolean(false);
    }

    public void untoggleButtons() {
        if(turretReset()) {
            resetTurret.setBoolean(false);
        }
    }
}

