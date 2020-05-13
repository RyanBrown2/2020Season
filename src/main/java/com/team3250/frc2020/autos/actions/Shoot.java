package com.team3250.frc2020.autos.actions;

import com.team3250.frc2020.subsystems.Control;

public class Shoot extends Action {
    Control controller = Control.getInstance();

    @Override
    public void start() {
        controller.setEnabled(true);
    }

    @Override
    public void update() {
        controller.run();
    }

    @Override
    public void done() {
        controller.setEnabled(false);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
