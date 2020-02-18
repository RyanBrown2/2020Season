package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.Constants;

public class Mixer {
    public enum Rollers {off, in, out}

    public Mixer() {
    }

    public void rollers(Rollers rollers) {
        if (rollers == Rollers.off) {
            Constants.Transport.mixer.set(ControlMode.PercentOutput, 0);
        } else if (rollers == Rollers.in) {
            Constants.Transport.mixer.set(ControlMode.PercentOutput, 0.8);
        } else if (rollers == Rollers.out) {
            Constants.Transport.mixer.set(ControlMode.PercentOutput, -0.8);
        }
    }
}
