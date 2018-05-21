package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.UI.PBCanvas;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 16/03/2018.
 */

// This Class will be used to build a swarm which consists of individual pixels.

public class Swarm {
    public HashMap<String, Bot> SwarmList;
    // @param Colour, is the colour of the swarm that you are creating, each swarm is unique by its colour if a bot is the same as the colour as a swarm it should be in that swarm
    // @param NumberOfBots, This is the numberOfBots that you want to add to the swarm
    // Each bot is uniquely made for each swarm

    //@param {Bot} add // for each bot create an ID that is based on the colour of the bot, how many bots have been created.
    // Create a location for each bot. It is incremented for Y for each Bot as a Bot of the same colour cannot be spawned on the same square.
    //adds the bot to the current swarm
    // adds the bot to the Grid

    public Swarm(Integer Colour, Integer numberOfBots){

        HashMap<String, Bot> thisSwarmList = new HashMap<>();

            for (int i = 0; i < numberOfBots; i++){
                Point defaultLocation = new Point(0, 1+i);
                String id = (Colour.toString() +"/"+ i);
                Bot add = new Bot(id, Colour, defaultLocation);
                thisSwarmList.put(id, add);
                UIAdapter.destinationGrid.addBot(add);

            }

        this.SwarmList = thisSwarmList;


    }

}
