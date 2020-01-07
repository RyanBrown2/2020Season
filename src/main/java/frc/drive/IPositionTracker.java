package frc.drive;

import frc.coordinates.*;

public interface IPositionTracker{
    /**
     * Returns position and velocity
     */
    public Pos2D getPosition();

    /**
     * Returns velocity vector
     */
    public Heading getVelocity();
}