package com.example.mischa.pixelbotui.Swarm;

import java.util.*;
import android.graphics.Point;

/**
 * Created by Ethan on 16/03/2018.
 */

// I am not too sure what this class will have but I feel it is important that each 'bot' (The class) have a path that is accessible by the bots(Platformio on the boards).
// This could be done by an API call from the bots
// Daniel: I think this should just stay as a custom type and Bot class should deal with all the communications.
class Path {
    // Store all the coordinates the bot will visit and all the corresponding directions it takes.
    List<Point> CurrentPathList;
    List<String> CurrentMoveSequence;

    // Code to run on class instantiation. The currentPosition must be inserted in order for a new instance to be created.
    Path(Point currentPosition) {
        this.CurrentPathList = new ArrayList<>();
        this.CurrentPathList.add(currentPosition); // Add current position to the known path history.
        this.CurrentMoveSequence = new ArrayList<>();
    }

    // Return currently stored path list in the instance.
    public List<Point> returnPathList() {
        return this.CurrentPathList;
    }

    // Return currently stored move sequence in the instance.
    public List<String> returnMoveSequence() {
        return this.CurrentMoveSequence;
    }

    // Add the provided direction, but only accept appropriate values. Accepts U, D, L and R as directions,
    // calculates and stores the resulting position of moving in certain direction.
    public void appendDirection(String direction) {
        // Get last known bot position in the list.
        Point tempPoint = this.CurrentPathList.get(this.CurrentPathList.size() - 1);

        // Depending on inputted direction, perform specific offsets to the last known position.
        switch (direction) {
            case "U":
                tempPoint.offset(0, 1);
                break;
            case "D":
                tempPoint.offset(0, -1);
                break;
            case "L":
                tempPoint.offset(-1, 0);
                break;
            case "R":
                tempPoint.offset(1, 0);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction provided! Can only accept U, D, L and R");
        }

        // Add all new values to the corresponding Lists.
        this.CurrentPathList.add(tempPoint);
        this.CurrentMoveSequence.add(direction);
    }
}
