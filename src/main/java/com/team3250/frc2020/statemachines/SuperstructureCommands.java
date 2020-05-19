package com.team3250.frc2020.statemachines;

import com.team3250.frc2020.states.SuperstructureGoal;
import com.team3250.frc2020.subsystems.Superstructure;

/**
 * Commands for the Superstructure to go to predetermined states or vision based
 * states
 */
public class SuperstructureCommands {

    private SuperstructureCommands() {}

    public static void setTurretPosition(double position) {
        Superstructure ss = Superstructure.getInstance();
        SuperstructureGoal lastCommand = ss.getGoal();
    }

}
