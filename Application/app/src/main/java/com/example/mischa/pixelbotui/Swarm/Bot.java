package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Color;
import android.graphics.Point; // Refer to: https://developer.android.com/reference/android/graphics/Point.html

/**
 * Created by Ethan on 16/03/2018.
 */

public class Bot {
    public Color Colour;
    public Point Location;
    public Path Path;
    public Boolean IsAvailable; //Created as false on start, as it shouldn't be moving or part of a picture. The way the swarm will work is it will look for available bots and move them.
                                //To become available a bot cannot be moving or part of the picture.

    // Location will need to be read in from another location, probably an XML sheet that the platformio device has sent or this has requested.
    // Currently will use  a default location to overcome this, however it will need to be updated.
    Bot(Color Colour, Point Location){

        this.Colour = Colour;
        this.Location = Location;
        IsAvailable = true;

    }

    public Boolean BotAvailable(){
        return IsBotMoving() && IsBotInPic();

    }

    public Point getLocation(){
        return this.Location;
    }

    //This function will need data from the board
    private boolean IsBotMoving(){
      return true;
    }
    //This function will need data from Pixel's & it's Location to see if it is in the pic
    private  boolean IsBotInPic(){
        return  true;
    }
}
