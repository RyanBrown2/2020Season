package frc.display;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ShooterDisplay {

    ShuffleboardTab tab;

    NetworkTableEntry state, angle, distance;

    public ShooterDisplay(){
        tab = Shuffleboard.getTab("Shooter Control");

        state =
                tab.add("Shooter State", 0)
                .getEntry();

        angle =
                tab.add("Camera Angle", 0)
                .getEntry();

        distance =
                tab.add("Camera Distance", 0)
                .getEntry();
    }

    public void angle(double angle) {
        this.angle.setDouble(angle);
    }

    public void distance(double distance) {
        this.distance.setDouble(distance);
    }

//    public void state(ShooterControl.States state) {
//        this.state.data;
//    }

}
