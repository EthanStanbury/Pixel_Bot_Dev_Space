package com.example.mischa.pixelbotui.Intergration;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;

import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 10/04/2018.
 */

public class SwarmAdapter {



    public static HashMap<Integer, Swarm> WholeSwarm = new HashMap<>();
    //@param initialSwarm the first in is the colour the second is how many of that bot you want to create
    //TODO add device address as the bot's id
    public static void SwarmCreate(HashMap<Integer, Integer> initialSwarm, HashMap<String, BluetoothDevice> devices){
        //For each object in the  initialSwarm create the swarm for the object, add each swarm to the Global Swarm list
        for (int key : initialSwarm.keySet()) {
            Swarm add = new Swarm(key, initialSwarm.get(key));
            WholeSwarm.put(key, add);

        }

    }
}
