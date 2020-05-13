package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class PurePursuitDisplay {

    ShuffleboardTab tab;

    NetworkTableEntry eta, purePursuitCurvature, inputVel, outputVel;
    NetworkTableEntry followerHeading, followerHeadingFull;
    NetworkTableEntry goalPos;

    public PurePursuitDisplay() {
        tab = Shuffleboard.getTab("Pure Pursuit");

        eta =
                tab.add("ETA", 0)
                .withPosition(0,0)
                .getEntry();

        purePursuitCurvature =
                tab.add("Pure Pursuit Curvature", 0)
                .withSize(2,1)
                .withPosition(1, 0)
                .getEntry();

        inputVel =
                tab.add("Input Vel", 0)
                .withPosition(0,1)
                .getEntry();

        outputVel =
                tab.add("Output Vel", 0)
                .withPosition(1,1)
                .getEntry();

        followerHeading =
                tab.add("Follower Heading", 0)
                .withPosition(0,2)
                .getEntry();

        goalPos =
                tab.add("Goal Pos", 0)
                .withPosition(1,2)
                .getEntry();
    }

    public void setEta(double eta) {
        this.eta.setDouble(eta);
    }

    public void setPurePursuitCurvature(double curve) {
        purePursuitCurvature.setDouble(curve);
    }

    public void setInputVel(double vel) {
        inputVel.setDouble(vel);
    }

    public void setOutputVel(double vel) {
        outputVel.setDouble(vel);
    }

    public void setFollowerHeading(double head) {
        followerHeading.setDouble(head);
    }

    public void setGoalPos(String pos) {
        goalPos.setString(pos);
    }

}
