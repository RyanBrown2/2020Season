package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import org.apache.commons.math3.analysis.solvers.NewtonRaphsonSolver;

public class DriverDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry inDeadZone;
    NetworkTableEntry visionAngle, visionDistance;
    NetworkTableEntry ballInRamp;
    NetworkTableEntry flywheelRPM;
    NetworkTableEntry matchTime;

    public DriverDisplay() {
        tab = Shuffleboard.getTab("Driver");

        inDeadZone =
                tab.add("Turret In Deadzone", false)
                .withSize(3,3)
                .withPosition(0,0)
                .getEntry();

        visionAngle =
                tab.add("Vision Angle", 0)
                .withSize(2,1)
                .withPosition(6,0)
                .getEntry();

        visionDistance =
                tab.add("Vision Distance", 0)
                .withSize(2,1)
                .withPosition(6,1)
                .getEntry();

        ballInRamp =
                tab.add("Ball In Ramp", false)
                .withSize(3,3)
                .withPosition(3,0)
                .getEntry();

        flywheelRPM =
                tab.add("Flywheel RPM", 0)
                .withSize(2,1)
                .withPosition(6,2)
                .getEntry();

        matchTime =
                tab.add("Match Time", 0)
                .withSize(3,3)
                .withPosition(8,0)
                .getEntry();

    }

    public void setInDeadZone(boolean inDeadZone) {
        this.inDeadZone.setBoolean(inDeadZone);
    }

    public void setBallInRamp(boolean inRamp) {
        this.ballInRamp.setBoolean(inRamp);
    }

    public void setMatchTime(double time) {
        this.matchTime.setDouble(time);
    }


}
