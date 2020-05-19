package com.team3250.frc2020.planners;

import com.team3250.frc2020.states.SuperstructureGoal;

public interface ISuperstructureMotionPlanner {
    boolean isValidGoal(SuperstructureGoal goal);

    /**
     * @return the new setpoint produced by the planner.
     */
    SuperstructureGoal plan(SuperstructureGoal prevSetpoint, SuperstructureGoal desiredState);
}