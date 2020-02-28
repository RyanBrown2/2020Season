package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.Constants;

public class Mixer {
    private static Mixer instance = null;
    public static Mixer getInstance() {
        if (instance == null) {
            instance = new Mixer();
        }
        return instance;
    }
    public enum Rollers {off, in, out, slowIn}

    private Mixer() {}

    public void rollers(Rollers rollers) {
        if (rollers == Rollers.off) {
            Constants.Transport.mixer.set(ControlMode.PercentOutput, 0);
        } else if (rollers == Rollers.in) {
            Constants.Transport.mixer.set(ControlMode.PercentOutput, 1);
        } else if (rollers == Rollers.out) {
            Constants.Transport.mixer.set(ControlMode.PercentOutput, -1);
        } else if (rollers == Rollers.slowIn) {
            Constants.Transport.mixer.set(ControlMode.PercentOutput, 0.25);
        }
    }
}
