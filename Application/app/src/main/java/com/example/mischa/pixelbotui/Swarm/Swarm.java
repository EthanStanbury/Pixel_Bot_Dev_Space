package com.example.mischa.pixelbotui.Swarm;

import android.bluetooth.BluetoothDevice;
import android.graphics.Point;

import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.UI.PBCanvas;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Ethan on 16/03/2018.
 */

// This Class will be used to build a swarm which consists of individual pixels.

public class Swarm {
    public static HashMap<String, Bot> currentSwarm = new HashMap<>();


    public static void SwarmCreate(Integer numberOfBots, LinkedHashMap<String, BluetoothDevice> devices){
        LinkedHashMap<String, BluetoothDevice> deviceList = devices;

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


            if (!deviceList.isEmpty()){
                String first = deviceList.keySet().iterator().next();
                System.out.println("Devices size: " + deviceList.size());
                System.out.println("Devices: " + deviceList.get(first).getAddress());
                String id = (deviceList.get(first).getAddress());
                Bot add = new Bot(id,defaultLocation);
                currentSwarm.put(id, add);
                UIAdapter.destinationGrid.addBot(add);
                deviceList.remove(first);

            }else {
                String id = numberOfBots.toString();
                Bot add = new Bot(id,defaultLocation);
                currentSwarm.put(id, add);
                UIAdapter.destinationGrid.addBot(add);
            }



            counter++;
            numberOfBots--;



        }




    }

}
