package frc.autos.actions;

import frc.subsystems.Control;

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
