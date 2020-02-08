package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Constants;

public class Feeder {

    public enum Actuation {in, out};

    Actuation actuation;

    public Feeder() {
        actuation = Actuation.in;
    }

    public void rollers(boolean turnOn) {
        if(turnOn) {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 1);
        } else {
            Constants.Feeder.rollerMotor.set(ControlMode.PercentOutput, 0);
        }
    }

    public void actuate() {
        switch (actuation) {
            case in:
                Constants.Feeder.leftSolenoid.set(DoubleSolenoid.Value.kForward);
                Constants.Feeder.rightSolenoid.set(DoubleSolenoid.Value.kForward);
                actuation = Actuation.out;
                break;
            case out:
                Constants.Feeder.leftSolenoid.set(DoubleSolenoid.Value.kReverse);
                Constants.Feeder.rightSolenoid.set(DoubleSolenoid.Value.kReverse);
                actuation = Actuation.in;
                break;
        }
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
