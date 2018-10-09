package com.example.mischa.pixelbotui.Swarm;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.UI.MainActivity;
import com.example.mischa.pixelbotui.UI.PBCanvas;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Ethan on 16/03/2018.
 */

// This Class will be used to build a swarm which consists of individual pixels.

public class Swarm {
    public static HashMap<String, Bot> currentSwarm = new HashMap<>();
    // @param Colour, is the colour of the swarm that you are creating, each swarm is unique by its colour if a bot is the same as the colour as a swarm it should be in that swarm
    // @param NumberOfBots, This is the numberOfBots that you want to add to the swarm
    // Each bot is uniquely made for each swarm

    //@param {Bot} add // for each bot create an ID that is based on the colour of the bot, how many bots have been created.
    // Create a location for each bot. It is incremented for Y for each Bot as a Bot of the same colour cannot be spawned on the same square.
    //adds the bot to the current swarm
    // adds the bot to the Grid

    public static void SwarmCreate(Integer numberOfBots, LinkedHashMap<String, BluetoothDevice> devices){
        LinkedHashMap<String, BluetoothDevice> deviceList = devices;
//        if (numberOfBots > devices.size()){
//            throw new IllegalStateException("FATAL ERROR: The number of bots you have spawned do not match the physical bots that are connected");
//        }

        HashMap<String, Bot> thisSwarmList = new HashMap<>();

        int side = 1;
        int counter = 0;

        while ( numberOfBots > 0){
            System.out.println("#botsleft: " + numberOfBots);



            if (side == 1 && counter == PBCanvas.yDimension - 2){
                side = 2;
                System.out.println("side: " +side);
                counter = 0;
            }else if(side == 2 && counter == PBCanvas.xDimension - 2) {
                side = 3;
                counter = 0;
                System.out.println("side: " +side);
            }else if (side == 3 && counter == PBCanvas.yDimension - 2){
                side = 4;
                counter = 0;
                System.out.println("side: " +side);


            }

            Point defaultLocation = new Point(0,0);
            if (side == 1 ) {
                defaultLocation.set(0, 1 + counter);
                System.out.println("Creating a bot in side 1 with a location of: " + defaultLocation);

            }else if( side == 2) {
                defaultLocation.set(1 +  counter, PBCanvas.yDimension - 1 );
                System.out.println("Creating a bot in side 2 with a location of: " + defaultLocation);
            }else if( side == 3){
                defaultLocation.set( PBCanvas.xDimension - 1, 1 + counter);
                System.out.println("Creating a bot in side 3 with a location of: " + defaultLocation);
            }else if(side == 4){
                defaultLocation.set( 1 + counter, 0);
                System.out.println("Creating a bot in side 4 with a location of: " + defaultLocation);
            }

            String id = (Colour.toString() +"/"+ numberOfBots);
            Bot add = new Bot(id, Colour, defaultLocation);
            thisSwarmList.put(id, add);
            UIAdapter.destinationGrid.addBot(add);

            counter++;
            numberOfBots--;



        }
        this.SwarmList = thisSwarmList;


    }

}
