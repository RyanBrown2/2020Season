package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Constants;

public class Feeder {

    public enum Actuation {in, out};
    public enum Rollers {off, in, out}


    Actuation actuation;

    public Feeder() {
        actuation = Actuation.in;
    }

    public void rollers(Rollers rollers) {
        if (rollers == Rollers.off) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 0);
        } else if (rollers == Rollers.in) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 1);
        } else if (rollers == Rollers.out) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, -1);
        }
    }

    public void actuate() {
        Constants.Feeder.solenoid.set(DoubleSolenoid.Value.kReverse);
//        switch (actuation) {
//            case in:
//                Constants.Feeder.solenoid.set(DoubleSolenoid.Value.kForward);
//                actuation = Actuation.out;
//                break;
//            case out:
//                Constants.Feeder.solenoid.set(DoubleSolenoid.Value.kReverse);
//                actuation = Actuation.in;
//                break;
//        }
    }

    public void panic() {

    }

    public boolean isIn() {
        if (actuation == Actuation.in) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isOut() {
        if (actuation == Actuation.out) {
            return true;
        } else {
            return false;
        }
    }

    public void display() {

    }

}
