package com.example.mischa.pixelbotui.Intergration;

import android.graphics.Color;

import com.example.mischa.pixelbotui.Swarm.Grid;
import com.example.mischa.pixelbotui.UI.LayoutItem;
import com.example.mischa.pixelbotui.UI.MainActivity;
import com.example.mischa.pixelbotui.UI.PBCanvas;
import com.example.mischa.pixelbotui.UI.Pixel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 10/04/2018.
 */

public class UIAdapter {

    public static Grid destinationGrid = new Grid(PBCanvas.xDimension, PBCanvas.yDimension);



    public void createGridWpixel(){

        for (Pixel p : PBCanvas.uiGrid) {
            if(p.colour != Color.TRANSPARENT){
                destinationGrid.addDestination(p);
            }
        }

    }

     public HashMap<Integer, Integer> CreateGridBots(){
        return PBCanvas.BotAmounts;
    }

//    private ArrayList<ArrayList<LayoutItem>> GetuiGrid(){
//
//        ArrayList<ArrayList<LayoutItem>> returnArray = new ArrayList<>();
//
//            for (int i = 0; i < PBCanvas.xDimension; i++){
//                ArrayList<LayoutItem> add = new ArrayList<>();
//                int index = i;
//                for(int x = 0; x < PBCanvas.yDimension; x++){
//                    add.add(PBCanvas.uiGrid[index]);
//                    index += PBCanvas.xDimension;
//                }
//                returnArray.add(add);
//            }
//
//        return  returnArray;
//    }


}
