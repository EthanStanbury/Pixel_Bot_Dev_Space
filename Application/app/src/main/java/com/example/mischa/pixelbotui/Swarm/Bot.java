package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Color;
import android.graphics.Point; // Refer to: https://developer.android.com/reference/android/graphics/Point.html

/**
 * Created by Ethan on 16/03/2018.
 */

public class Bot {
    public String BotID;           // The ID of this bot. May be useful in identifying which bot to talk to by just calling ID. May also be completely useless.
    public Color Colour;
    public Point Location;
    public Path Path;
    public Boolean IsAvailable; //Created as false on start, as it shouldn't be moving or part of a picture. The way the swarm will work is it will look for available bots and move them.
                                //To become available a bot cannot be moving or part of the picture.

    // Location will need to be read in from another location, probably an XML sheet that the platformio device has sent or this has requested.
    // Currently will use  a default location to overcome this, however it will need to be updated.
    public Bot(String id, Color Colour, Point Location){

        this.BotID = id;
        this.Colour = Colour;
        this.Location = Location;
        IsAvailable = true;

    }

    public String getBotID() {
        return this.BotID;
    }

    public Boolean BotAvailable(){
        return IsBotMoving() && IsBotInPic();

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
