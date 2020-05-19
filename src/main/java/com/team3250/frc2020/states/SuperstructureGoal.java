package com.team3250.frc2020.states;

/**
 * Represents a goal for the superstructure
 */
public class SuperstructureGoal {
    public final SuperstructureState state;

    public SuperstructureGoal(double turret, double hood, double flywheel) {
        this(new SuperstructureState(turret, hood, flywheel));
    }

    public SuperstructureGoal(SuperstructureState state) {
        this.state = new SuperstructureState(state);
    }

    public boolean equals(SuperstructureGoal other) {
        return this.state.turret == other.state.turret &&
                this.state.hood == other.state.hood &&
                this.state.flywheel == other.state.flywheel;
    }

    public boolean isAtDesiredState(SuperstructureState currentState) {
        double[] distances = {
                currentState.turret - state.turret,
                currentState.hood - state.hood,
                currentState.flywheel - state.flywheel
        };

        for (int i = 0; i < distances.length; i++) {
            if (Math.abs(distances[i]) > SuperstructureConstants.kPadding[i]) {
                return false;
            }
        }
        return true;
    }
}
