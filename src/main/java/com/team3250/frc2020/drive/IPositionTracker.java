package com.team3250.frc2020.drive;

import com.team3250.frc2020.coordinates.*;

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