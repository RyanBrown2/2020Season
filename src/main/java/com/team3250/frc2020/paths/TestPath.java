package com.team3250.frc2020.paths;

import com.team254.lib.control.Path;
import org.junit.Test;

import java.util.ArrayList;

public class TestPath implements PathContainer {

    public TestPath() {}

    @Override
    public Path buildPath() {
        ArrayList<PathBuilder.Waypoint> sWaypoints = new ArrayList<PathBuilder.Waypoint>();

        sWaypoints.add(new PathBuilder.Waypoint(1,1,1,0));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }

    @Override
    public boolean isReversed() {
        return false;
    }
}
