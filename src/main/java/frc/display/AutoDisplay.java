package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class AutoDisplay {

    ShuffleboardTab tab;
    NetworkTableEntry eta, purePursuitCurvature, inVel, outVel, followerHeading, followerHeadingFull;
    NetworkTableEntry followerMessage, goalPos, distanceToGoal;

    public AutoDisplay() {
        tab = Shuffleboard.getTab("Auto");

        eta =
                tab.add("ETA", 0)
                .getEntry();

        purePursuitCurvature =
                tab.add("Pure Pursuit Curvature", 0)
                .getEntry();

        inVel =
                tab.add("Input Vel", 0)
                .getEntry();

        outVel =
                tab.add("Output Vel", 0)
                .getEntry();

        followerHeading =
                tab.add("Follower Heading", 0)
                .getEntry();

        followerHeadingFull =
                tab.add("Follower Heading", 0)
                .getEntry();

        followerMessage =
                tab.add("Follower Message", 0)
                .getEntry();

        goalPos =
                tab.add("Goal Position", 0)
                .getEntry();

        distanceToGoal =
                tab.add("Distance To Goal",0)
                .getEntry();
    }

}
